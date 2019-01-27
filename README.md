Smartcar Integration with GM APIs
1. Uses Spring Boot and Gson
2. From the project directory, run \
`mvn clean install` \
to build the project and run tests
3. To run the application, run the following from the project directory \
`java -jar target\smartcar-integration-1.0-SNAPSHOT.jar` \

Sample Request/Responses \
\
Request: \
GET http://localhost:8080/vehicles/1234\

Response:\
`{
    "vin": "123123412412",
    "color": "Metallic Silver",
    "driveTrain": "v8",
    "doorCount": 4
}`
\
Request: \
GET http://localhost:8080/vehicles/1234/doors\

Response:\
`[
    {
        "location": "backRight",
        "locked": true
    },
    {
        "location": "backLeft",
        "locked": true
    },
    {
        "location": "frontLeft",
        "locked": false
    },
    {
        "location": "frontRight",
        "locked": false
    }
]`
\
Request: \
GET http://localhost:8080/vehicles/1234/fuel\

Response:\
`{
    "percent": 58.41
}`
\
Request: \
GET http://localhost:8080/vehicles/1234/battery\

Response:\
`{
    "percent": null
}`
\
Request: \
POST http://localhost:8080/vehicles/1235/engine \
`{
  "action": "START"
}`
\
Response: \
`{
    "status": "error"
}`
\