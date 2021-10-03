# Cria rede docker
docker network create TrabalhoFinalMicroServices

#start projeto Investimento + postgress
cd Investimento
docker run -d --name bancoinvestimento -p 5105:5432 --network=TrabalhoFinalMicroServices -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=investimento postgres:9.6.18-alpine
mvn package -DskipTests
docker build -f src\main\docker\Dockerfile.jvm -t investimento-grupo-a-jvm .
docker run -d --name investimentoServer --rm -p 8181:8181 -p 5006:5006 --network=TrabalhoFinalMicroServices -e JAVA_ENABLE_DEBUG="true" investimento-grupo-a-jvm .