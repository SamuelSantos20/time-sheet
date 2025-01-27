FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /build

COPY . .

RUN mvn clean package  -DskipTests

FROM openjdk:21

WORKDIR /app

COPY --from=build /build/target/*.jar  ./time-sheet.jar

ENTRYPOINT ["java", "-jar", "time-sheet.jar"]


LABEL authors="Samuel Santos Miranda"