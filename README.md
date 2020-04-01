# Project setup
This is a gradle multi-project build to ease the running of multiple services in early development stage. Idealy all projects have their individual repositories

Multi-project build consists of:
 * **eureka** Netflix Eureka service discovery;
 * **gateway** Spring Cloud Gateway;
 * **service** Service Declaration microservice.
 
Eureka server is necessary for service discovery, Gateway takes care of proxying services using the service discovery.

Each project has folder called **ext** which holds Docker compose and OpenApi files. 
# Installation

To build all the services, run in the root folder:

```./gradlew clean build```

To build a specific service, navigate into sub-project directory and run:

```./gradlew clean build```

# Docker

For docker setup, first create network:

```docker network create consent_net```

To build all docker containers, run:

```./gradlew jibDockerBuild```

To start the containers, run:

```docker-compose -f eureka/ext/docker/app.yml up -d```

```docker-compose -f gateway/ext/docker/app.yml up -d```

```docker-compose -f service/ext/docker/postgres.yml up -d```

```docker-compose -f service/ext/docker/app.yml up -d```

Try the service: 
```bash
curl -X POST "http://localhost:8080/SERVICE/service" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderId\":\"spId\",\"serviceDeclarationId\":\"dId\",\"name\":\"Name\",\"description\":\"description in different langs\",\"technicalDescription\":\"technical stuff\",\"consentMaxDurationSeconds\":0,\"needSignature\":false,\"validUntil\":1901307432,\"maxCacheSeconds\":0}"
```
