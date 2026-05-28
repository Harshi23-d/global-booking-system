# Step 1: Use a Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Use the official eclipse-temurin image to run the application
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/booking-system-0.0.1-SNAPSHOT.jar booking-system.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "booking-system.jar"]