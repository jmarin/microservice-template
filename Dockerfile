# Microservice Template Dockerfile
# Version: 1.0.0

# Image builds from the official Docker Java Image

FROM java:8

MAINTAINER Juan Marin Otero <juan.marin.otero@gmail.com>

WORKDIR /

USER daemon

ENTRYPOINT ["java", "-jar", "/opt/microservice-template.jar"]

EXPOSE 8082

COPY target/scala-2.11/microservice-template.jar /opt/microservice-template.jar