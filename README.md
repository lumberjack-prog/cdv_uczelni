## Cassandra

To configure Cassandra in  Spring Boot application add `application-prod.properties` file under `src/main/resources/` with the following content:
```
#Cassandra
spring.data.cassandra.contact-points=<your host>
spring.data.cassandra.port=9042
spring.data.cassandra.schema-action=create_if_not_exists
spring.data.cassandra.keyspace-name=universities
spring.data.cassandra.local-datacenter=<your data center>
spring.data.cassandra.request.timeout=10s
spring.data.cassandra.connection.connect-timeout=10s
spring.data.cassandra.connection.init-query-timeout=10s
spring.main.allow-bean-definition-overriding=true
```
Create keyspace `universities` manually:
```
CREATE KEYSPACE IF NOT EXISTS universities WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };
```

`http://localhost:8080/login`

```
    {
        "username": "admin",
        "password": "pass"
    }
```

`http://localhost:8080/registration`
```
{
    "username": "daniel",
    "email": "deniel@gmail.com",
    "password": "pass"
}
```

`http://localhost:8080/api/v1/cs/university/save`
```
        {
            "name": "Politechnika Poznanska",
            "type": "uczelnia",
            "city": "Poznań",
            "score": 0,
            "faculties": [
                {
                    "name": "WYDZIAŁ AUTOMATYKI, ROBOTYKI I ELEKTROTECHNIKI",
                    "majors": [
                        {
                            "name": "Automatyka i robotyka",
                            "type": "techniczne"
                        },
                        {
                            "name": "Matematyka w technice",
                            "type": "techniczne"
                        }
                    ]
                },
                {
                    "name": "LOGO WYDZIAŁU WYDZIAŁ INFORMATYKI I TELEKOMUNIKACJI",
                    "majors": [
                        {
                            "name": "Bioinformatyka",
                            "type": "techniczne"
                        },
                        {
                            "name": "Informatyka",
                            "type": "techniczne"
                        },
                        {
                            "name": "Sztuczna inteligencja",
                            "type": "techniczne"
                        }
                    ]
                }
            ]
        }
```

```http://localhost:8080/api/v1/cs/university/getAll```
```http://localhost:8080/api/v1/cs/university/deleteById/b8d6c237-f259-46b0-9d62-eff084034b63```
```http://localhost:8080/api/v1/cs/university/getById/2f1cd9c8-d0f8-4b17-a0d8-b48babf12e94```
```http://localhost:8080/api/v1/cs/user/addToFavorites/f6cc051f-cdd7-4a4a-bfd1-7c07fb9d3888```
```http://localhost:8080/api/v1/cs/user/voteFor/f6cc051f-cdd7-4a4a-bfd1-7c07fb9d3888```
```http://localhost:8080/api/v1/cs/favorites/getAll```