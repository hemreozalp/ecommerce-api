# Base image (Java Runtime)
FROM openjdk:21-jdk

# Uygulama jar dosyasını konteynıra kopyala
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java","-jar","/app.jar"]
