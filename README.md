# First Build and RUN
docker-compose up --build

# Build and update app
mvn clean package && \
  docker cp target/simplers.war  docker_payara-simplers_1:/opt/payara/glassfish/domains/domain1/autodeploy/

## gets all persons
curl -H "Accept: application/json" http://localhost:8080/simplers/resources/persons

## gets one person by id
curl -H "Accept: application/json" http://localhost:8080/simplers/resources/persons/2

## gets the list of ids
curl -v -H "Accept: application/json" "http://localhost:8080/simplers/resources/persons/ids"

## adds a new person
curl -v -H "Content-Type: application/json" \
  --request POST \
  --data '{"email":"x.y@z.fr", "firstname":"x", "lastname":"y"}' \
  http://localhost:8080/simplers/resources/persons

## gets persons paginated 
curl -v "http://localhost:8080/simplers/resources/persons?pagenumber=1&perpage=2"
curl -v "http://localhost:8080/simplers/resources/persons?pagenumber=2&perpage=2"
