#!/bin/bash
./mvnw package -Dmaven.test.skip=true
java -jar ./target/PMS-0.0.1-SNAPSHOT.jar --spring.profiles.active=local