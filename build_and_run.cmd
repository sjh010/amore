@echo off
call mvnw package -Dmaven.test.skip=true
call java -jar .\target\PMS-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
pause