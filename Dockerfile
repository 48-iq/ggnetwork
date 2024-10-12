FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY ./pom.xml ./
COPY ./src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/ggnetwork-*.jar /ggnetwork.jar

EXPOSE 8080
CMD ["java", "-jar", "ggnetwork.jar"]

