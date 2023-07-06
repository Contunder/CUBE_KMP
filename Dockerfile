FROM --platform=linux/arm64/v8 bellsoft/liberica-openjdk-alpine-musl:19
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
