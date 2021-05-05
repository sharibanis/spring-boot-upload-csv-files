# Spring Boot Upload/Download CSV Files with MySQL database example

In controller package, we create CSVController class for Rest APIs.

– @CrossOrigin("http://localhost:8081") is for configuring allowed origins.

– @Controller annotation indicates that this is a controller.

– @PostMapping annotation is for mapping HTTP POST requests onto specific handler methods:

POST /upload: uploadFile()

Let’s define the maximum file size that can be uploaded in application.properties as following:

```
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB
```
The MySQL DB schema can be created by running the `schema.sql` script.

## Run Spring Boot application
```
mvn spring-boot:run
```
