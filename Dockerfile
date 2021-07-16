# start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="soungho"

# Add a volume to /tmp
VOLUME /tmp

# Make prot 8080 available to thie world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/BlogAPIServer-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} to-do-springboot.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/to-do-springboot.jar"]