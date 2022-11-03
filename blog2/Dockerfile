FROM openjdk:11
EXPOSE 8084
WORKDIR /app
COPY build/libs/blog2-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "blog2-0.0.1-SNAPSHOT.jar" ]


#FROM openjdk:11-jre
#EXPOSE 8084
## 컴퓨터 안에서 빌드된 jar파일을 컨테이너 안으로 복사
##COPY build/libs/blog2-0.0.1-*.jar app.jar
#COPY build/libs/blog2-0.0.1-SNAPSHOT.jar app.jar
## 컨테이너 안에서 애플리케이션 실행시 명령어 실행
##RUN ["java","-jar","app.jar"]
#ENTRYPOINT ["java","-jar","app.jar"]
#
#
#
