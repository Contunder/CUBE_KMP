FROM bellsoft/liberica-openjdk-alpine-musl:17
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080