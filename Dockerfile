# 基本イメージとしてOpenJDK 17を使用
FROM openjdk:17

# アプリケーションのjarファイルをコンテナ内にコピー
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# コンテナ起動時にアプリケーションを実行
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar"]
