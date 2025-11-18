FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /home/app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /home/app

COPY --from=build /home/app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
