FROM openjdk:8
WORKDIR /
ADD build/libs/sample-akka-docker-cluster-1.0-SNAPSHOT-all.jar sample-akka-docker-cluster-1.0-SNAPSHOT-all.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "sample-akka-docker-cluster-1.0-SNAPSHOT-all.jar"]
