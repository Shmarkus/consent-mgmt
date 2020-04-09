# Projekti eeldused

## Platvormid
 * AdoptOpenJDK 11
 * Postgre SQL 11.5
 
## Raamistikud
 * Spring Framework 5.2.5.RELEASE
 * Spring Cloud (Eureka, Gateway) 2.2.2.RELEASE
 * Spring (Boot, Data) 2.2.6.RELEASE
 * Hibernate 5.4.13.Final
 * Liquibase 3.8.1
 * Mapstruct 1.2.0
 * Lombok 1.18.12
 * OpenAPI v3 (generator) 4.3.0
 
# Projekti ülesehitus
Tegemist on Gradle multi-project projektiga, et hõlbustada proovitöö esitamist. Ideaalmaailmas on iga teenus oma koodivaramus.

Projekt koosneb:
 * **eureka** Netflix Eureka declaration discovery;
 * **gateway** Spring Cloud Gateway;
 * **declaration** Service Declaration mikroteenus (sõltub PostgreSQL andmebaasist).
 
Eureka server on hädavajalik teenuste avastamisel, Gateway kasutab Eurekat, et leida teenused ning päringud õige teenuse pihta proxyda.

Igas projektis on **ext** kaust, kus asuvad nii Docker Compose kui ka OpenApi failid. OpenApi spetsifikatsiooni kasutatakse
serveri teenuste koodigenereerimisel Gradle **compileJava** taski sees.
 * declaration teenuse OpenApi spec: ```declaration\ext\api\openapi.yml```


Gradle build failid on ehitatud modulaarsetena. Lisad, nagu OpenApi, Docker või MapStruct on leitavad teenuse **gradle** 
kaustast

# Paigaldus

Integratsioonitestide jaoks käivita:

```./gradlew integrationTest```

Ühiktestide jaoks käivita:

```./gradlew test```

Kõikide teenuste ehitamiseks käivita (kindla teenuse ehitamiseks käivita sama käsk teenuse kaustas):

```./gradlew clean build```

Teenuse kokkuehitatud pakk asub teenuse kaustas **[teenus]/build/libs**. Teenuse lokaalseks käivitamiseks kasuta käsku:

```java -Djavax.net.ssl.trustStore=[teekond projektijuurkaustani]/jssecacerts -jar [teenus].jar``` 

Välise konfiguratsiooni kasutamiseks kasuta järgmist käsku:

```java -Djavax.net.ssl.trustStore=[teekond projektijuurkaustani]/jssecacerts -jar [teenus].jar --spring.config.location=classpath:config/application.yml,[teekond konfiguratsioonifailini].yml```

# Docker

Dockeri seadistuseks loo esmalt väline võrk:

```docker network create consent_net```

Kõikide teenuste Dockeri konteinerite ehitamiseks käivita:

```./gradlew jibDockerBuild```

Konteinerite käivitamiseks käivita:

```docker-compose  up -d```
> See on proovitöö hindamise eesmärgil loodud otsetee kõikide teenuste käivitamiseks ning selles failis on avatud ka kõikide teenuste pordid. Iga teenuse juures ext/docker kaustas asub teenuse toodangu Compose fail, kus ainult 8443 port on maailmale nähtav

Kontrolli teenuste toimimist:
 * [Eureka](https://localhost:8761/actuator/health)
 * [Gateway](https://localhost:8443/actuator/health)
 * [Service](https://localhost:8010/actuator/health)

Kutsu teenus välja järgmise käsuga (esimene kord õnnestub, teisel korral tuleb duplicate_declaration viga): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```

# Arendajale

Teenuse sertifikaadi genereerimine:
```bash
openssl genrsa -out [declaration].key 2048
openssl req -new -x509 -key [declaration].key -out [declaration].crt -days 3650 -subj /CN=[declaration]-app/OU=GDEV/O=Helmes
openssl pkcs12 -export -in [declaration].crt -inkey [declaration].key -name [declaration] -out [declaration].p12
keytool -importkeystore -destkeystore [declaration].jks -srckeystore [declaration].p12 -srcstoretype PKCS12
```
Genereeritud sertifikaadi import:

```keytool -import -file [declaration].crt -alias [declaration] -keystore jssecacerts```

Jssecacerts parool on ```changeit```
