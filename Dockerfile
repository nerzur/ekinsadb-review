# Maven build container
FROM maven:3.8.7-eclipse-temurin-17-alpine AS maven_build

RUN rm -r /tmp/

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mvn clean package

FROM eclipse-temurin:17-jdk-alpine
RUN addgroup -S demo && adduser -S demo -G demo
MAINTAINER gabrielpga20@gmail.com

EXPOSE 8091

COPY --from=maven_build /tmp/target/ekinsadbReview-service.jar /ekinsadbReview-service/app.jar

ENTRYPOINT ["java", "-jar", "/ekinsadbReview-service/app.jar"]