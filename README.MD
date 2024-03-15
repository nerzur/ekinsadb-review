# EkinDB - Review - Backend

This project has been developed with the purpose of detecting a set of anomalies present in the database of the Filling-Emptying plant of the San José rum factory. It has been developed in Java (with Maven) using the environment provided by SpringBoot. It has support for security using jwt (oauth2) through a direct connection to a Keycloak service.
It is made up of a main project and a set of subprojects that make up the web services ecosystem. These projects mentioned are:
- **ekinsadb-review-service:** Main service that performs the different analyzes in the database. Perform queries on an external SQL Server database.
- **registry-service:** Service in charge of registering each of the services that make up the project to facilitate discovery and load balancing tasks. It is developed with Eureka Server.
- **admin-service:** Service in charge of monitoring the status of the various projects that make up the project. It is developed with Spring Admin.
- **api-gateway:** Service that works as a reverse proxy to group the different services that make up this project into a single port. Use load balancing.
- **user-service:** Service that allows the administration of Keycloak users. Through this service users can be consulted, added, modified or deleted.

> [!IMPORTANT]  
> Keycloack has been used to control and register users. To use the services implemented in this project it is necessary to start the keycloack service.

When starting services it is important to note the boot order. These should start in the following order:
1. keycloak-service
2. registry-service
2. admin-service
3. ekinsadb-review-service
4. user-service
5. api-gateway

These services have been developed with <code>Java 17</code> and <code>Spring Boot 3.1.2</code>. A root project is used that brings together each of the services as a module incorporated through Maven. All projects are named in the <em>pom.xml</em> file of the root project.

## Compiling and starting the services

To compile any of these services you can use Maven tasks. These tasks can be executed individually by being located in each one of the subprojects or from the root of the main project (the main project includes all the subprojects since they are incorporated as submodules of it). You can use the following command:
```bash
$ mvn clean package install
```

To run the services you can run the following command:
```bash
$ mvn spring-boot:run
```

On the other hand, you can also run the .jar files generated from each of the projects with the following command:
```bash
$ java -jar target\<filename>
```

## Docker Deploy

### Deploy with DockerFile
In the root directory of each of the projects you can find two DockerFile files with which you can generate a container to run the service. In the case of DockerFile files, they contain the compilation process inside a temporary container and in the case of DockerFileDev files, the process must be carried out outside the container to later move the compilation to the container.
To run these files you should run the following commands:
- **registry-service:** `docker run -p 8761:8761 <DockerFileName>`
- **admin-service:** `docker run -p 8086:8086 <DockerFileName>`
- **ekinsadb-review-service:** `docker run -p 0:0 <DockerFileName>`
- **user-service:** `docker run -p 0:0 <DockerFileName>`
- **api-gateway:** `docker run -p 8091:8091 <DockerFileName>`

### Deploy with Docker-Compose
One way to simplify this process is by using DockerCompose. In the root directory of the project there are the files docker-compose and docker-compose-dev through which the project can be deployed using docker in a simplified way. In the first case, the 3 services will be deployed by compiling them in a temporary container and in the second case, the compiled files are selected from the root of the project and executed on a virtual server.
```shell
docker-compose up --build docker-compose.yml
```

## Services Execution

To access the service monitoring interfaces you can use the following URL:
- http://localhost:8761: Contains the website of the Eureka Server, which allows you to follow the status of the drone-service..
- http://localhost:8086 : Contains the Spring Admin web interface. This service allows you to view the drone-service status.

To test the implemented services you can use the following URLs:
- http://localhost:8090/api/ekinsadb-review-service/v1/swagger-ui/index.html : This URL contains a web page created with SwaggerUI that allows you to check each of the web services implemented in this service. In this case all endpoints developed for ekinsadb-review-service can be checked.
- http://localhost:8090/api/user-service/v1/swagger-ui/index.html : This URL contains a web page created with SwaggerUI that allows you to check each of the web services implemented in this service. In this case all endpoints developed for user-service can be checked.

