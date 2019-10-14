FROM amazoncorretto:11

EXPOSE 8080
ADD ./build/libs/customer-service-0.0.1-SNAPSHOT.jar springbootapp.jar
ENTRYPOINT ["java", "-jar", "-Xms1024m", "-Xmx1024m", "springbootapp.jar"]