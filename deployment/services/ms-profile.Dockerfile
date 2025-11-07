FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY /ms-profile/build/libs/ms-profile-1.0.0.jar /app/app.jar
EXPOSE 9501
ENTRYPOINT ["java", "-jar", "app.jar"]