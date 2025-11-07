FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY /ms-gateway/build/libs/ms-gateway-1.0.0.jar /app/app.jar
EXPOSE 9500
ENTRYPOINT ["java", "-jar", "app.jar"]