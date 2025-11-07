FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY /ms-discovery/build/libs/ms-discovery-1.0.0.jar /app/app.jar
EXPOSE 9502
ENTRYPOINT ["java", "-jar", "app.jar"]