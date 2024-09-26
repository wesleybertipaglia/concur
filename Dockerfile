FROM maven:latest AS build

WORKDIR /api

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21 AS runtime

COPY --from=build /api/target/*-SNAPSHOT.jar api.jar

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]