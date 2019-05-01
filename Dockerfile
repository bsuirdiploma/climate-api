FROM openjdk:8-jre-alpine

WORKDIR /climate-api/

ADD target/climate-api-0.0.1-SNAPSHOT.jar climate-api.jar
ADD target/swagger.json swagger/climate-api.json

ENTRYPOINT java -Drun.jvmArguments=$JAVA_OPTS -jar climate-api.jar
