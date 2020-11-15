FROM openjdk:11

ADD target/distributed-job.jar /usr/local/distributed-job/

WORKDIR /usr/local/distributed-job

CMD ["java", "-jar", "distributed-job.jar"]