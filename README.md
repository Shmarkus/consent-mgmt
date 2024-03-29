# Projekti info

## Platvormid
 * AdoptOpenJDK 11
 * Postgre SQL 11.5
 * curl 7.46.0
 * Docker 19.03.6
 * Docker Compose 1.17.1
 * Openssl 1.0.2e
 * Gradle 5.2.1
 
## Raamistikud
 * Spring Framework 5.2.5.RELEASE
 * Spring Cloud (Eureka, Gateway) 2.2.2.RELEASE
 * Spring (Boot, Data) 2.2.6.RELEASE
 * Hibernate 5.4.13.Final
 * Liquibase 3.8.1
 * Mapstruct 1.2.0
 * Lombok 1.18.12
 * OpenAPI v3 (generator) 4.3.0
 * JIB 2.1.0
 
# Projekti ülesehitus
Tegemist on Gradle multi-project projektiga, et hõlbustada proovitöö esitamist. Ideaalmaailmas on iga teenus oma koodivaramus.

Projekt koosneb:
 * **eureka** Netflix Eureka declaration discovery;
 * **gateway** Spring Cloud Gateway;
 * **declaration** Service Declaration mikroteenus (sõltub PostgreSQL andmebaasist).
 * **provider** Service Provider mikroteenus (sõltub PostgreSQL andmebaasist).
 
Eureka server on hädavajalik teenuste avastamisel, Gateway kasutab Eurekat, et leida teenused ning päringud õige teenuse pihta proxyda.

Igas projektis on **ext** kaust, kus asuvad nii Docker Compose kui ka OpenApi failid. OpenApi spetsifikatsiooni kasutatakse
serveri teenuste koodigenereerimisel Gradle **compileJava** taski sees.
 * **declaration** teenuse OpenApi spec: ```declaration/ext/api/openapi.yml```
 * **provider** teenuse OpenApi spec: ```provider/ext/api/openapi.yml```

Gradle build failid on ehitatud modulaarsetena. Lisad, nagu OpenApi, Docker või MapStruct on leitavad teenuse **gradle** kaustast

Andmebaas paigaldatakse ning muudatusi hallatakse läbi Liquibase skriptide. Rakenduse käivitamisel kontrollitakse andmebaasi seisu
ning vajadusel uuendatakse andmebaasi struktuur. Liquibase skriptid on leitavad: ```provider/src/main/resources/liquibase``` ja 
```declaration/src/main/resources/liquibase```

# Paigaldusjuhend

Juhend on kirjutatud eeldades et keskkonnaks on Linux. Windowsi jaoks kasutada Gradle käivituseks ```gradlew.bat``` ja kaustadesse navigeerimisel vahetada ```/``` Windowsi ```\ ``` vastu

Paki lahti ZIP fail

```unzip consent-mgmt-master.zip```

Navigeeri projekti kausta

```cd consent-mgmt-master```

Et veenduda paki korrektses toimimises käivita integratsioonitestid:

```./gradlew integrationTest```

Ühiktestide jaoks käivita:

```./gradlew test```

Kõikide teenuste ehitamiseks käivita (kindla teenuse ehitamiseks käivita sama käsk teenuse kaustas):

```./gradlew clean build```

## Hosts faili muutmine

Enne projektide käivitamist lisada hosts faili (C:\Windows\System32\drivers\etc\hosts, /etc/hosts), **vastasel juhul sertifikaadikontroll ebaõnnestub**:
```
127.0.0.1 eureka-app
127.0.0.1 gateway-app
127.0.0.1 provider-app
127.0.0.1 declaration-app
```
Lisaks on hostide resolvimist vaja ka Swagger UI toimimiseks.

## Ilma Dockerita

Teenuse kokkuehitatud pakk asub teenuse kaustas **[teenus]/build/libs**. Teenuse lokaalseks käivitamiseks kasuta käsku:

```java -Djavax.net.ssl.trustStore=[teekond projektijuurkaustani]/jssecacerts -jar [teekond projektijuurkaustani]/build/libs/[teenus].jar``` 

Välise konfiguratsiooni kasutamiseks kasuta järgmist käsku:

```java -Djavax.net.ssl.trustStore=[teekond projektijuurkaustani]/jssecacerts -jar [teekond projektijuurkaustani]/build/libs/.jar --spring.config.location=classpath:config/application.yml,[teekond konfiguratsioonifailini].yml```

Välise teenuse konfiguratsiooni näide:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://[andmebaas]:5432/declaration
    username: [kasutajanimi]
    password: [parool]
server:
  port: [teenuse port]
  ssl:
    enabled: [kas kasutada TSLi]
    key-store: [teekond sertifikaadi keystoreni]
    key-alias: [võtme alias keystores]
    key-store-password: [keystore parool]
eureka:
  instance:
    hostname: [teenuse hostname]
    non-secure-port-enabled: false #kas kasutatakse lisaks TSLile ka tavalist HTTP ühendust?
    non-secure-port: [HTTP port, kui TSL pole kasutuses] 
    secure-port: [TSL puhul port]
    secure-port-enabled: true #kas kasutatakse TSLi
  client:
    serviceUrl:
      defaultZone: [URL Eureka serverini]
application:
  logging:
    use-json-format: false #true kui kasutada Logstashi (ELK)
    logstash:
      enabled: false # true kui kasutada Logstashi (ELK)
      host: [logstashi URL]
      port: [logstashi port]
      queue-size: 512
```

## Docker

Dockeri seadistuseks loo esmalt väline võrk:

```docker network create consent_net```

Kõikide teenuste Dockeri konteinerite ehitamiseks käivita:

```./gradlew jibDockerBuild```

Konteinerite käivitamiseks käivita:

```docker-compose  up -d```
> See on proovitöö hindamise eesmärgil loodud otsetee kõikide teenuste käivitamiseks ning selles failis on avatud ka kõikide teenuste pordid. Iga teenuse juures ext/docker kaustas asub teenuse toodangu Compose fail, kus ainult 8443 port on maailmale nähtav

### OpenAPI
Et lihtsustada liidestamist teenustega, on Docker Compose faili lisatud ka [Swagger UI](http://localhost:8888/swagger-ui.html) kõikide teenuste spec'idega. Täiendavaid teenuseid saab lisada projekti juurkataloogis asuvas ```swagger-ui.yml``` failis. Selleks, et Swagger UI suudaks teenuse API spec'i kuvada, **peab kasutaja browseris olema declaration ja provider teenuse sertifikaadid aksepteeritud**! Sertifikaadid saab aksepteerida siit: [provider](https://provider-app:8011/v3/api-doc), [declaration](https://declaration-app:8010/v3/api-doc).
Hosts fail peab olema muudetud (vaata: Hosts faili muutmine)

### Logimine
> Linuxi süsteemidel tuleb Kibana kasutamiseks suurendada avatud failide arvu: ```sudo sysctl -w vm.max_map_count=262144```

Kõik teenused on seadistatud oma logisid saatma Logstashi. Kui teenused fail on käivitunud siis: 
  * oota, kuni Kibana käivitub ja [ava](http://localhost:5601)
  * vali *Explore on my own*
  * vali *Connect to your Elasticsearch index*
  * Index patterniks sisesta *syslog*
  * "Time Filter field name" väärtuseks vali *@timestamp*
  * seejärel vajuta "Create index pattern"
  
Ava vasakult menüüst "Discover" ja lülita sisse *app_name*, *app_port*, *level*, *message* väljad. Kogu rakenduste hingeelu on siit nähtav


## Puhastus

Kui teenuste käivitamisel midagi ebaõnnestub, siis enne uuesti proovimist on oluline ära koristada teenused, mis võivad probleeme põhjustada.
 * ```docker-compose down``` projekti juurkataloogis, et panna seisma töötavad konteinerid
 * ```./gradlew clean```  projekti juurkataloogis, et eemaldada projekti build failid
 * ```docker network remove consent_net``` eemaldamaks Dockeri võrgu
 * ```docker system prune -a``` eemaldamaks kõik genereeritud konteinerid. See eemaldab ka varasemalt ehtatud imaged ja konteinerid!
 * eemalda hosts failist lisatud hostid


# Testimisjuhend

## Masinloetavad heartbeat liidesed

Kontrolli teenuste toimimist:
 * [Eureka](https://localhost:8761/actuator/health)
 * [Gateway](https://localhost:8443/actuator/health)
 * [Service](https://localhost:8010/actuator/health)
 * [Provider](https://localhost:8011/actuator/health)

Kui teenus on töökorras, on vastuseks:
```json
{
"status": "UP"
}
```

## Teenuste test
Kutsu teenus välja järgmise käsuga, mis tekitab uue kirje andmebaasi (response: ok):
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Päring tundmatu teenuspakkujaga (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"unknown\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Topeltdeklaratsioon (duplicate_declaration): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Kehtivusaeg minevikus (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId-1\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1101307432,\"maxCacheSeconds\":0}" -k -v
```
Cache negatiivne (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId-2\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":-1}" -k -v
```
Teenusepakkuja ID puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":null,\"serviceDeclarationId\":\"dId-3\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Teenuse ID puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":null,\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Nimi puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId-4\",\"name\":null,\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Kirjeldus puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId-5\",\"name\":\"Name\",\"description\":null,\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Tehniline kirjeldus puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId-6\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":null,\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```
Nõusoleku maksimaalne kestvus puudu (invalid_request): 
```bash
curl -X POST "https://localhost:8443/DECLARATION/declaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":null,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```

## Arendajale
### Uute teenuste lisamine
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
