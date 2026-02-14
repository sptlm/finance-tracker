FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Кеш зависимостей
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Сборка
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- stage 2: run in Tomcat 9 (JRE 21) ----------
FROM tomcat:9.0-jre21-temurin

# Чтобы приложение открывалось по /
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Кладём WAR как ROOT.war в webapps
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]