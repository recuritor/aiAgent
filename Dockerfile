FROM openjdk:17
WORKDIR /app
COPY . .
RUN ./gradlewbuild
CMD ["java", "-jar", "build/libs/*.jar]
