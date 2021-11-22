# RDD
Registro Digitale Distribuito

## Il progetto
Questo repository contiene un progetto discusso nel libro **"Blockchain e appalti"** 
di [Luca Barbieri](https://www.linkedin.com/in/luca-barbieri-213157139/)
e [Giuseppe Vincenzi](https://www.linkedin.com/in/giuseppevincenzi/).
Questo codice ha l'obiettivo di fornire una base su cui implementare il proprio Registro Digitale Distribuito, secondo i prncipi descritti nel libro, ma adattato al proprio singolo caso d'uso.

## Tecnologie utilizzate
Le tecnologie e i framework utilizzati in questo progetto sono principalmente:
- Maven
- Spring Boot
- Spring Cloud (Netflix Eureka Server, Zuul, Hystrix, Stream Rabbit)
- Spring Data REST
- Spring Security
- Swagger

## Build Information
![example workflow](https://github.com/gvincenzi/RDD/actions/workflows/maven.yml/badge.svg)

## Schema architetturale
Il Registro Digitale Distribuito si basa sui principi del pattern di un'[Architettura Atomica di Giuseppe Vincenzi](https://www.linkedin.com/feed/update/urn:li:activity:6791100763025219584/).
In particolare lo schema molecolare di un RDD sarà il seguente :

![Schema molecolare di un RDD](src/main/resources/img/schema.png?raw=true)

### Sistema di accesso qualificato
Questo sistema è il punto di accesso alla macromolecola di un Registro Digitale Distribuito.
Il Sistema di accesso qualificato si compone di due moduli:
- **Service registry**, per registrare tutti i microservizi che contribuiscono al funzionamento del Registro Digitale Distribuito;
- Un **Gateway service** che realizza, aprendo un accesso all'API esposta dal modulo ***alpha*** (nello schema è rappresentata dalla freccia entrante dal Sistema di accesso qualificato al dispositivo di distribuzione), lo ***Spike*** del pattern dell'Architettura Atomica, ovvero il servizio grazie al quale dall'esterno si potranno realizzare chiamate su protocollo HTTP verso la macromolecola.

### Dispositivo di distribuzione
Questa molecola rappresenta il dispositivo che ha il compito di raccogliere le richieste dall'esterno della macromolecola e di smistarle verso l'interno.
La molecola si compone di due atomi:
- L'atomo di Dominio, Domain atom (Do)
- L'atomo di distribuzione, Delivery atom (De)

![Interazioni con l'esterno del dispositivo di distribuzione](src/main/resources/img/distribution_schema.png?raw=true)

