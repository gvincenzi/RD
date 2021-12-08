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
- Spring Cloud (Netflix Eureka Server, Zuul, Hystrix, Stream Rabbit)
- Spring Data REST
- Spring Security
- Swagger

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
 
