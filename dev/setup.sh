#docker run -d \
#--name customer-review-postgres \
#-e POSTGRES_PASSWORD=root \
#-p 5432:5432 \
#postgres:11.5-alpine

docker run -d \
--name=customer-review-consul \
-p 8500:8500 \
-p 8600:8600/udp \
consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0

docker run -d -v \
--name=customer-review-keycloak \
-p 8888:8080 \
-e KEYCLOAK_USER=keycloak \
-e KEYCLOAK_PASSWORD=keycloak \
-e DB_VENDOR=h2 \
-e JAVA_OPTS='-Dkeycloak.migration.action=import -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/tmp/keycloak-dump.json' \
--mount type=bind,source="$(pwd)"/tmp/keycloak-dump.json,target=/tmp/keycloak-dump.json \
jboss/keycloak:11.0.0