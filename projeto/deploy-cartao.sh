# Cria rede docker
docker network create TrabalhoFinalMicroServices

# start projeto Cartao + postgress
cd Cartao
docker run -d --name bancocartao -p 5106:5432 --network=TrabalhoFinalMicroServices -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=cartao postgres:9.6.18-alpine
mvn package -DskipTests
docker build -f src\main\docker\Dockerfile.jvm -t cartao-grupo-a-jvm .
docker run -d --name cartaoServer --rm -p 8182:8182 -p 5007:5007 --network=TrabalhoFinalMicroServices -e JAVA_ENABLE_DEBUG="true" cartao-grupo-a-jvm .