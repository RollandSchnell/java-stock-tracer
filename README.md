# java-stock-tracer application

Spring boot 2.0 application using H2 in memory db for user and stock rule management.

The API uses the as external stock provider open source https://www.alphavantage.co/

In order to access application resource endpoints create a user, authenticate and use the received JWT access token in future requests in the Authorization header as: Bearer <token>
  
### to run the project -> mvn install -> mvn spring-boot:run
