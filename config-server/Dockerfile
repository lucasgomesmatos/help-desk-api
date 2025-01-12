FROM openjdk:23-jdk

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]




#docker run -p 8888:8888 --name helpdesk-config-server -e CONFIG_SERVER_URI=https://github.com/lucasgomesmatos/config-server-backend -e CONFIG_SERVER_USERNAME=lucasgomesmatos -e CONFIG_SERVER_PASSWORD=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 lucasgomesmatos/helpdesk-config-server:latest