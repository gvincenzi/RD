# RDD
Registro Digitale Distribuito

## Il progetto
Questo repository contiene un progetto discusso nel libro **"Blockchain e appalti"** 
di [Luca Barbieri](https://www.linkedin.com/in/luca-barbieri-213157139/)
e [Giuseppe Vincenzi](https://www.linkedin.com/in/giuseppevincenzi/).
Questo codice ha l'obiettivo di fornire una base su cui implementare il proprio Registro Digiale Distribuito, secondo i prncipi descritti nel libro, ma adattato al proprio singolo caso d'uso.

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
- Un **Gateway service** che realizza lo ***Spike*** del pattern dell'Architettura Atomica, ovvero il servizio grazie al quale dall'esterno si potranno realizzare chiamate su protocollo HTTP verso la macromolecola.
 
