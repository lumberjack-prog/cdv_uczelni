To configure REDIS in Spring Boot application add `application-prod.properties` file under `src/main/resources/` with the following content:
```
spring.redis.host=<your host>
spring.redis.port=6379
spring.redis.password=<your password>
```

`Get All` universities
```
http://localhost:8080/api/v1/getAll
```
`Get by id`
```
http://localhost:8080/api/v1/getById/1
```
`Add/Update` all universities
```
http://localhost:8080/api/v1/saveAll

Json example:
{
    "uczelnie": [
        {
            "name": "UAM",
            "type": "uczelnia",
            "miasto": "Poznan",
        },
        {
            "name": "CDV",
            "type": "uczelnia",
            "miasto": "Poznan",
            "score": 5
        },
        {
            "name": "Politechnika Poznanska",
            "type": "uczelnia",
            "miasto": "Poznań",
            "score": 2
        },
        {
            "name": "Politechnika Gdańska",
            "type": "uczelnia",
            "miasto": "Gdansk",
            "score": 1
        }
    ]
}
```
`Add` a new university if doesn't exist or `Update` existing one
```
http://localhost:8080/api/v1/save

Json example:

    {
        "name": "UAM",
        "type": "uczelnia",
        "miasto": "Warsaw"
        "score": 6
    }
```