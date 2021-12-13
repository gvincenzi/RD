# RDC
Registro Digitale Contribuito

## Il progetto
Questo repository contiene un progetto discusso nel libro **"Blockchain e appalti"** 
di [Luca Barbieri](https://www.linkedin.com/in/luca-barbieri-213157139/)
e [Giuseppe Vincenzi](https://www.linkedin.com/in/giuseppevincenzi/).
Questo codice ha l'obiettivo di fornire una base su cui implementare il proprio Registro Digitale Contribuito, secondo i prncipi descritti nel libro, ma adattato al proprio singolo caso d'uso.

## Tecnologie utilizzate
Le tecnologie e i framework utilizzati in questo progetto sono principalmente:
- Maven
- Spring Boot
- Spring Cloud (Netflix Eureka Server, Zuul, Hystrix, Stream Rabbit, Feign Client)
- Spring Data REST
- Spring Security
- Swagger

## Tecnologie di terze parti usate per far funzionare un POC basata su questa architettura
Il sistema necessita di due componenti infrastrutturali per rendere operativa un'instanza di un RDC :
- Un server per le comunicazioni AMQP tra atomi e tra molecole
> Per il POC ho usato [CloudAMQP](https://www.cloudamqp.com/)
- Un database NoSQL
> Per il POC ho usato un [MongoDB](https://www.mongodb.com/)

## Build Information
![example workflow](https://github.com/gvincenzi/RDC/actions/workflows/maven.yml/badge.svg)

## Schema architetturale
Il Registro Digitale Contribuito si basa sui principi del pattern di un'[Architettura Atomica di Giuseppe Vincenzi](https://www.linkedin.com/feed/update/urn:li:activity:6791100763025219584/).
In particolare lo schema molecolare di un RDC sarà il seguente :

![Schema molecolare di un RDC](src/main/resources/img/schema.png?raw=true)

### Sistema di accesso qualificato
Questo sistema è il punto di accesso alla macromolecola di un Registro Digitale Contribuito.
Il Sistema di accesso qualificato si compone di due moduli:
- **Service registry**, per registrare tutti i microservizi che contribuiscono al funzionamento del Registro Digitale Contribuito;
- Un **Gateway service** che realizza, aprendo un accesso all'API esposta dal modulo ***alpha*** (nello schema è rappresentata dalla freccia entrante dal Sistema di accesso qualificato al dispositivo di distribuzione), lo ***Spike*** del pattern dell'Architettura Atomica, ovvero il servizio grazie al quale dall'esterno si potranno realizzare chiamate su protocollo HTTP verso la macromolecola.

### Dispositivo di distribuzione
Questa molecola rappresenta il dispositivo che ha il compito di raccogliere le richieste dall'esterno della macromolecola e di smistarle verso l'interno.
La molecola si compone di due atomi e uno spike:
- Lo spike della molecola (che si realizza tramite lo sviluppo di un'API REST di accesso al modulo)
- L'atomo di Dominio, Domain atom (Do)
- L'atomo di distribuzione, Delivery atom (De)

![Interazioni con l'esterno del dispositivo di distribuzione](src/main/resources/img/distribution_schema.png?raw=true)

[Qui l'implementazione della molecola di distribuzione](https://github.com/gvincenzi/RDC/tree/master/rdc-distribution).

### Dispositivo di programmazione
Questa molecola rappresenta il dispositivo che ha il compito di inviare notifiche in caso di eventi particolari (aggiunta di un documento nel regisro, per esempio), 
ma anche per eseguire delle istruzioni programmate che possono a loro volta generare notifiche verso gli utenti.
La molecola si compone di due atomi :
- L'atomo delle istruzioni, Instruction atom (In)
- L'atomo di notifica, Notifier atom (No)

![Interazioni con l'esterno del dispositivo di programmazione](src/main/resources/img/scheduler_schema.png?raw=true)

[Qui l'implementazione della molecola di programmazione](https://github.com/gvincenzi/RDC/tree/master/rdc-scheduler).

### Nodo del registro
Questa molecola è il cuore del Registro Digitale Contribuito : è la molecola che contiene il registro nella sue interezza e potrà essere istanziata un numero di volte pari al numero di attori coinvolti nella vita del registro.
Nel funzionamento di base proposto per un RDC non si procederà mai al fork del registro: ogni volta che uno dei nodi prenderà in carico l'inserimento di un nuovo documento, gli altri si forzeranno in uno stato di "freeze" in attesa che l'operazione sia stata completato dalla loro molecola gemella.

In questa implementazione Open Source di base di un RDC utilizziamo un database embedded relazionale: in una versione di produzione invece si invita all'utilizzo di soluzioni NoSQL per rendere l'RDC capace di salvare qualsiasi tipo di informazione, qualsiasi sia la sua struttura.

Si potrà anche integrare sistemi di ricerca (come Apache Lucene, per fornire un esempio) per rendere possibile, agevole ed efficiente una ricerca nel contenuto dei documenti salvati nell'RDC. 

La molecola si compone di un solo atomo :
- L'atomo del cuore delle operazioni, Core atom (Co)

![Interazioni con l'esterno del nodo di un RDC](src/main/resources/img/node_schema.png?raw=true)

[Qui l'implementazione del nodo di un RDC](https://github.com/gvincenzi/RDC/tree/master/rdc-node).

##Dispositivo di flusso documentale
###Protocollo AMQP
Il dispositivo di flusso documentale si realizza attraverso l'utiliszzo di un server che dia la possibilità di scambiare messaggi tramite il protocollo [AMQP](https://it.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol).
Lo scopo è quello di far comunicare le molecole tra di loro, non attraverso una comunicazione sincrona, ma attraverso lo scambio di messaggi su exchanges dedicati.

###Exchanges e queues utilizzate
Il Sistema di comunicazione tra i dispositivi si basa, nella sua implementazione di base, su 4 exchanges:
- RequestChannel
>Il dispositivo di distribuzione scrive in questo canale le proposte di inserimento di nuovi documenti. Una richiesta sarà corredata da un identificativo per riconoscerne la risposta sul canale dedicato. Il dispositivo di distribuzione attende la risposta prima di inviare altre richieste di questo tipo.
>>Tutti i nodi dell'RDC sono in ascolto del topic ma un messaggio sarà consumato da una solo istanza (condividono la stessa coda)

- RequestIntegrityChannel
>Il dispositivo di distribuzione scrive in questo canale le richiesta di verifica dell'integrità della catena. Una richiesta sarà corredata da un identificativo per riconoscerne la risposta sul canale dedicato. Il dispositivo di distribuzione attende la risposta prima di inviare altre richieste di questo tipo.
>>Tutti i nodi dell'RDC sono in ascolto del topic ma un messaggio sarà consumato da una solo istanza (condividono la stessa coda)

- ResponseChannel
>I noi dell'RDC inviano le risposte alla richieste, sia di inserimento di nuovi documenti che di validazione dell'integrità del registro, in questo canale.
>Il dispositivo di distribuzione e il dispositivo di programmazione consumano i messaggi che transitano in questo exchange.

- DistributionChannel
>Il dispositivo di distribuzione scrive in questo canale le informazioni che devono essere distribuite a tutti i nodi dell'RDC. Nell'implementazione di base ad ogni inseriemento di un nuovo documento ***(RDCItem)***, l'informazione viene distribuita a tutti i nodi perché possano inserirlo nelle loro copie della base di dati.

![Exchanges del dispositivo di flusso documentale (RabbitMQ nella versione del POC)](src/main/resources/img/flow_exchanges.png?raw=true)

##Startup di un nodo
Quando un nuovo nodo viene lanciato, nelle proprietà Yammel della molecola si puo' precisare che il nodo necessità di un processo di startup: la molecola utilizzerà lo spike della macromolecola per richiedere una verifica dell'integrità della catena; in risposta riceverà un CorrelationID con cui potrà richiedere subito dopo il risultato della verifica che contiene anche l'intera copia del registro.
Il nodo verificherà che la parte del registro che eventualmente conosce già deve essere rimasta intatta (in caso contrario il registro sarà definito corrotto) e inserirà il resto del registro che ancora non conosce. 
![Due database di due nodi A e B di uno stesso RDC (due DB nello stesso Cluster nella versione del POC)](src/main/resources/img/mongodb-databases.png?raw=true)

##Inserimento di un nuovo documento
Quando un nodo riceve un messaggio con una proposta di inserimento di un nuovo documento ***(ItemPoposition)*** reagisce costruendo un nuovo identificativo univoco per il documento ***(RDCItem)*** secondo l'algoritmo definito nell'implementazione della interfaccia [RDCItemService](https://github.com/gvincenzi/RDC/tree/master/rdc-node/src/main/java/org/rdc/node/service/RDCItemService.java).
Nella versione di base dell'RDC l'algoritmo proposto è il seguente:
```java
package org.rdc.node.service.impl.base;

import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.service.impl.RDCItemServiceImpl;
import org.rdc.node.util.NodeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseRDCItemServiceImpl extends RDCItemServiceImpl {
	private static final String REGEX_DIGIT = "[0-9].*";

	@Override
	public boolean isHashResolved(RDCItem rdcItem, Integer difficultLevel) {
		List<Integer> digits = new ArrayList<>(difficultLevel);

		Integer index = 0;
		String hash = rdcItem.getId();
		while (index < hash.length() && digits.size() < difficultLevel) {
			String s = hash.substring(index, ++index);
			if (s.matches(REGEX_DIGIT)) {
				digits.add(Integer.parseInt(s));
			}
		}

		Integer sum = digits.parallelStream().reduce(0, Integer::sum);
		return sum % difficultLevel == 0;
	}

	@Override
	public String calculateHash(RDCItem rdcItem) throws RDCNodeException {
		return NodeUtils.applySha256(
				rdcItem.getPreviousId() +
						rdcItem.getTimestamp().toEpochMilli() +
						rdcItem.getNonce() +
						rdcItem.getDocument().getTitle() +
						rdcItem.getOwner().getMail()
		);
	}
}
```
[Qui l'implementazione dell'algoritmo](https://github.com/gvincenzi/RDC/tree/master/rdc-node/src/main/java/org/rdc/node/service/impl/base/BaseRDCItemServiceImpl.java)
