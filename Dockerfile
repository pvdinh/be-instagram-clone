# Use a base image with Java and Maven installed
FROM maven:3.8.3-openjdk-11-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Clone or pull the code from GitHub
RUN apt-get update && apt-get install -y git
RUN git clone -b develop https://github.com/pvdinh/be-instagram-clone.git

# Build the JAR file
WORKDIR /app/be-instagram-clone
RUN mvn clean package -DskipTests

# Use a lightweight base image with Java installed
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/be-instagram-clone/target/*.jar app.jar

# Set the command to run when the container starts
CMD ["java", "-jar", "app.jar"]