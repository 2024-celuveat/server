FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} celuveat.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "/celuveat.jar"]
