## Fiserv Test Application
Return the total number of times that the #topic appears in the article's text field.
Using an HTTP GET method it retrieve information from Wikipedia using a given topic.

Query
https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=topic to get the topic Wikipedia article. 

## To build and run TC's
mvn clean install

## To run application
1. cd target
2. java -jar fiserv-coding-challenge-1.0.0-SNAPSHOT.jar

It will start the application on port 8080. In case of Port in use error, run the application by specifying a different port
java -jar fiserv-coding-challenge-1.0.0-SNAPSHOT.jar --server.port=8081

## Get endpoint to call the wiki topic service
/fiserv-application/wiki/topic?name=pasta

## Sample curl command to call the endpoint
For Linux
curl --request GET 'localhost:8080/fiserv-application/wiki/topic?name=pasta'

For Windows
curl --request GET localhost:8080/fiserv-application/wiki/topic?name=pasta
