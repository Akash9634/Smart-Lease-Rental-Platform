FROM eclipse-temurin:21-jre-alpine

#create working directory inside container
WORKDIR /app

#copy your jar into the container
COPY target/smartlease-backend-0.0.1-SNAPSHOT.jar app.jar

#tell docker your app runs on port 8080
EXPOSE 8080

#command to start the app
ENTRYPOINT ["java", "-jar", "app.jar"]