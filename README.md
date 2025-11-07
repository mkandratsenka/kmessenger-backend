Экспериментальный мессенджер на реактивном стеке.

1. Запуск окружения для локальной разработки (из корня проекта):
1.1 Запуск
docker compose --project-name kmessenger-backend -f ./deployment/docker-compose.yml -f ./deployment/docker-compose.local.yml --env-file ./deployment/.env.local up -d

1.2 Отключение и удаление окружения (не удаляет volumes):
docker compose --project-name kmessenger-backend down


2. Сборка образов микросервисов:
gradle clean

ms-profile:
./gradlew :ms-profile:bootJar
docker build -t ms-profile:1.0.0 -f deployment/services/ms-profile.Dockerfile .

ms-conversation:
./gradlew :ms-conversation:bootJar
docker build -t ms-conversation:1.0.0 -f deployment/services/ms-conversation.Dockerfile .

ms-gateway:
./gradlew :ms-gateway:bootJar
docker build -t ms-gateway:1.0.0 -f deployment/services/ms-gateway.Dockerfile .

ms-discovery (только для локальной разработки, где нету оркестратора Docker Swarm):
./gradlew :ms-discovery:bootJar
docker build -t ms-discovery:1.0.0 -f deployment/services/ms-discovery.Dockerfile .


3. Запуск окружения STAGE (из корня проекта):
3.1 Включение Docker Swarm (разово, проверка с помощью docker info)
docker swarm init

3.2 Запуск одной командой
env $(cat ./deployment/.env.stage | grep -v '^#' | xargs) \
docker stack deploy -c ./deployment/docker-compose.yml -c ./deployment/docker-compose.stage.yml kmessenger

3.3 Точечный перезапуск сервиса
docker service update --force service_name
docker service update --force kmessenger_nginx-proxy

3.4 Точечное удаление (отключение) сервиса в Docker Swarm
docker service scale <имя_сервиса>=0
для сервисов с "mode: global":
docker service rm kmessenger_nginx-proxy

3.5 Удаление окружения (не удаляет volumes)
docker stack rm kmessenger

4. Keycloak
4.1 Секреты не экспортируются, поэтому при первом запуске необходимо зайти
в https://kmessenger.net/auth/admin/master/console/#/kmessenger (http://localhost:8443/admin/master/console/#/kmessenger) ->
Clients -> ms-conversation -> Credentials -> Regenerate Client Secret
и заменить на новое значение в зависимости от контура:
Для STAGE/PROD: CONVERSATION_KEYCLOAK_CLIENT_SECRET (.env.stage/prod).
Для LOCAL: conversation.keycloak.client.secret в application-local.yml (ms-conversation).

4.2 Поскольку master realm не импортируется, то для защиты от подбора пароля следует вручную
настроить: Realm Settings (master) → Security Defenses → Brute Force Detection


5. MongoDB
5.1 Проверка режима Replica Set. В контейнере mongo Exec:
mongosh --username ЛОГИН --password ПАРОЛЬ
mongosh --username admin --password mongoLocal
rs.status()


6. Docker Hub

docker tag ms-discovery:1.0.0 kandratsenka94/ms-discovery:latest
docker push kandratsenka94/ms-discovery:latest

docker tag ms-gateway:1.0.0 kandratsenka94/ms-gateway:latest
docker push kandratsenka94/ms-gateway:latest

docker tag ms-profile:1.0.0 kandratsenka94/ms-profile:latest
docker push kandratsenka94/ms-profile:latest

docker tag ms-conversation:1.0.0 kandratsenka94/ms-conversation:latest
docker push kandratsenka94/ms-conversation:latest