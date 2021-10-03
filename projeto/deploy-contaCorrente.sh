# Cria rede docker
docker network create TrabalhoFinalMicroServices

# start projeto Conta Corrente + postgress
cd ContaCorrente
docker run -d --name bancoconta -p 5432:5432 --network=TrabalhoFinalMicroServices -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=contacorrente postgres:9.6.18-alpine
mvn package -DskipTests
docker build -f src\main\docker\Dockerfile.jvm -t conta-corrente-grupo-a-jvm .
docker run -d --name contacorrenteServer --rm -p 8180:8180 -p 5005:5005 --network=TrabalhoFinalMicroServices -e JAVA_ENABLE_DEBUG="true" conta-corrente-grupo-a-jvm .