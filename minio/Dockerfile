FROM bellsoft/liberica-openjdk-alpine:17.0.8
ADD target/minio-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/minio-0.0.1-SNAPSHOT.jar"]
WORKDIR /app
RUN mkdir -p /app/logs
EXPOSE 8081