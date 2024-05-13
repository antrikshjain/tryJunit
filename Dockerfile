# Use an existing base image with JDK installed
FROM ubuntu:20.04

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get clean;

# Set JAVA_HOME environment variable
#ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

#ENV PATH $JAVA_HOME/bin:$PATH

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files into the container
COPY gradlew .
COPY gradle ./gradle

# Copy the build script and other necessary files
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
COPY gradle .
COPY gradlew .
COPY input.txt .

# Run the Gradle build command
RUN ./gradlew build

# Specify the command to run your application (if applicable)
CMD ["./gradlew", "build"]

