## **Project 0 - Loan Management System**

1. **How to run the application:**
    - The application can be run in two ways.
        - **Using `mvn exec:java`** : Inside the project, the command "`mvn exec:java`" can be executed and the service will start working in the localhost at port 7000, the logging of the service could be observed in the console.
        - **Making the package**: It will be necessary to create the jar package to run the program. In order to do this, you have to go to the project and execute the command `mvn package`". Maven will go through the first 4 phases of the maven lifecycle. Validate, compile, test and package. Once it has finished, inside the target folder, a file called "loasn-project-1.0-SNAPSHOT-jar-with-dependencies.jar" will be found. You have to use the next command to execute the project "`java -jar target/loasn-project-1.0-SNAPSHOT-jar-with-dependencies.jar`". This will start the service in the port 7000 of the localhost and the logging will be shown in the console.
    
2. **Database connection/configuration details:**
    - A Postrgres database is needed to run the service. The requirements are:
        - **Database name**: loans_db
        - **URL and port of DB**: localhost/5432
        - **User**: postgres
        - **Password**: password

3. **Instructions for running unit tests:**
    - To run the unit test of the the project, it is only needed to execute the command **`mvn test`** inside the project. The tests that will run are for the layer service for the user, and the layer DAO for the loans. 