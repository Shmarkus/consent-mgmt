# Project prerequisites

## Platforms
 * AdoptOpenJDK 11
 * Postgre SQL 11.5
 
## Frameworks
 * Spring Framework 5.2.5.RELEASE
 * Spring Cloud (Eureka, Gateway) 2.2.2.RELEASE
 * Spring (Boot, Data) 2.2.6.RELEASE
 * Hibernate 5.4.13.Final
 * Liquibase 3.8.1
 * Mapstruct 1.2.0
 * Lombok 1.18.12
 * OpenAPI v3 (generator) 4.3.0
 
# Project setup
This is a gradle multi-project build to ease the running of multiple services in early development stage. Idealy all projects have their individual repositories

Multi-project build consists of:
 * **eureka** Netflix Eureka service discovery;
 * **gateway** Spring Cloud Gateway;
 * **service** Service Declaration microservice (depends on PostgreSQL database).
 
Eureka server is necessary for service discovery, Gateway takes care of proxying services using the service discovery.

Each project has folder called **ext** which holds Docker compose and OpenApi files. OpenApi specifications are used to generate service server API during **compileJava** Gradle task

The build files are modular, so add-ons like OpenApi codegen, Docker and Mapstruct can be found in projects **gradle** directory

Each project has **/actuator/health** endpoint for checking the service health
# Installation

To build all the services, run in the root folder:

```./gradlew clean build```

To build a specific service, navigate into sub-project directory and run:

```./gradlew clean build```

The project artifacts can be found in projects build/libs directory. To run the projects use the following command:
```java -Djavax.net.ssl.trustStore=[path-to-project-root]/jssecacerts -jar [artifact].jar```. To use externalized configuration use the following command:

```java -Djavax.net.ssl.trustStore=[path-to-project-root]/jssecacerts -jar [artifact].jar --spring.config.location=classpath:config/application.yml,[path-to-external-config-file].yml```

# Docker

For docker setup, first create network:

```docker network create consent_net```

To build all docker containers, run:

```./gradlew jibDockerBuild```

To start the containers, run:

```docker-compose  up -d```

Check the services health from these links:
 * [Eureka](https://localhost:8761/actuator/health)
 * [Gateway](https://localhost:8443/actuator/health)
 * [Service](https://localhost:8010/actuator/health)


Call the service: 
```bash
curl -X POST "https://localhost:8443/SERVICE/service" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}" -k -v
```

# Etc

Creating certificate for a service:
```bash
openssl genrsa -out [service].key 2048
openssl req -new -x509 -key [service].key -out [service].crt -days 3650 -subj /CN=[service]-app/OU=GDEV/O=Helmes
openssl pkcs12 -export -in [service].crt -inkey [service].key -name [service] -out [service].p12
keytool -importkeystore -destkeystore [service].jks -srckeystore [service].p12 -srcstoretype PKCS12
```
And import the certificate to truststore (in project root) used by other services
```keytool -import -file [service].crt -alias [service] -keystore jssecacerts```
