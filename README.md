# java-stock-tracer application

Spring boot 2.0 application using H2 in memory db for user and stock rule management.

The API uses the as external stock provider open source https://www.alphavantage.co/

In order to access application resource endpoints create a user, authenticate and use the received JWT access token in future requests in the Authorization header as: Bearer <token>
  
In application.properties configure:

#### spring.mail.username=<user.email>
#### spring.mail.password=<user.password>

With prefered user email and password as a source email from where the notifications will be delivered.

Also you can configure the scheduled job stock check delay:

#### stock.schedule.delay.milliseconds = 10000
#### stock.schedule.initialDelay.milliseconds = 10000

More then this the jwt token signing key is configurable as well:

#### jwt.secret = secret
  
### To run the project -> mvn install -> mvn spring-boot:run
