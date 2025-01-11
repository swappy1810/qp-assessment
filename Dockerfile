# Use official OpenJDK 11 base image
FROM openjdk:17-jdk-slim as base

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/grocerynew-0.0.1-SNAPSHOT.jar /app/groceryapp.jar

# Expose port 8080 to allow outside traffic to access the app
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/groceryapp.jar"]
