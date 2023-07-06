FROM --platform=linux/x86_64 bellsoft/liberica-openjdk-alpine-musl:19
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
