# spring-rest-example
Spring Rest + Spring Data (H2->hibernate) + Swagger

The Spring Rest example was used as the starting point to create `/greeting` api.<BR>
Then Spring Data was added using an h2 in memory database front ended by hibernate.<BR>
Finally, Swagger was added to document the REST api.

type `localhost:8080/greeting` in your browser
type `http://localhost:8080/swagger-ui.html` for the swagger ui

Try with name `User` or `Tester`

# changes to MySql on Cloud Foundry
## Add manifest file
## Change to MySql
### update application.yml
## Setup Database
Install and start mysql:

```bash
brew install mysql
mysql.server start
mysql -u root
```

Create a database user and table in the MySQL REPL you just opened:

```sql
CREATE USER 'spring-rest'@localhost IDENTIFIED BY 'spring-rest-secret';
CREATE DATABASE cf_spring_rest_development;
GRANT ALL ON cf_spring_rest_development.* TO 'spring-rest'@localhost;
exit
```

### check that it works
```
mysql -uspring-rest -pspring-rest-secret
```

## build it
```gradle
gradle clean build
```

## setup cloud foundry
### login
```
cf login -a https://api.run.pivotal.io
```
OR for PEZ
```
cf login -a https://api.run.pez.pivotal.io --sso
```

### create MySql service
```
cf create-service cleardb spark mysql
```
OR
'cf markplace' and find the MySql service and options like
for PEZ 
``` 
cf create-service p-mysql 100mb-dev mysql
```

### check the service
```
cf services
```

### push it
```
cf push
```
#### Oops... Host Taken error???
```
Server error, status code: 400, error code: 210003, message: The host is taken:
```

Try:
```
cf push --random-route
```

### try it out... (PEZ Heritage CF worked as expected)

#### bummer its not working on cloud foundry with cleardb... did you create the database user?
You need to get the database user and password from Cloud Foundry
and put those as the User defined properties DB_USER and DB_USER_PASSWORD

