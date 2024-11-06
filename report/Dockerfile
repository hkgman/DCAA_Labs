From openjdk:17-oracle

ADD target/report-0.0.1-SNAPSHOT.jar /app/
CMD ["java","-Xmx200m","-jar","-Dspring.profiles.active=dev", "/app/report-0.0.1-SNAPSHOT.jar"]
WORKDIR /app
RUN mkdir -p /app/logs
EXPOSE 8082