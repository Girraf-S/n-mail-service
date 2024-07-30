FROM openjdk:20-slim
RUN apt-get update && apt-get install -y curl ca-certificates
RUN mkdir -p /app/ca-certificates
COPY gmail1.pem /app/ca-certificates/cert1.crt
COPY gmail2.pem /app/ca-certificates/cert2.crt

RUN keytool -import -alias cert1 -file /app/ca-certificates/cert1.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt \
    && keytool -import -alias cert2 -file /app/ca-certificates/cert2.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

WORKDIR /app
COPY target/n-mail-service-0.0.1-SNAPSHOT.jar /app/n-mail-service.jar
COPY src/main/resources/application.yml /app/application.yml

ENTRYPOINT ["java", "-jar", "n-mail-service.jar", "--spring.config.location=file:/app/application.yml"]