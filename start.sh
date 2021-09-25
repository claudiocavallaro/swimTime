echo "--------------Maven clean install -> creating jar -----------------"
mvn clean install -DskipTests;
echo "--------------Creating Database container--------------------------"
docker-compose -p project -f docker-compose.yml up -d db;
echo "--------------Waiting for database instance -----------------------"
sleep 10;
echo "--------------Creating application container-----------------------"
docker-compose -p project -f docker-compose.yml up -d app;
