FROM openjdk:16-slim-buster

WORKDIR /app

# Copy the Maven wrapper into the Docker image
COPY mvnw mvnw
COPY .mvn .mvn

# Download all dependencies
COPY pom.xml pom.xml
RUN ./mvnw dependency:resolve

# Copy source code into the image
COPY . .

# Set up the run command
CMD ["./mvnw", "spring-boot:run"]
