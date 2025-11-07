## Экспериментальный мессенджер на реактивном стеке!

<p>
  <img src="https://github.com/user-attachments/assets/29ff2c2d-b393-4f2a-8075-764205d42609" width="600"/>
</p>
<p>
  <img src="https://github.com/user-attachments/assets/d3234b8b-1cc1-40cd-b62b-a12f07525529" width="600"/>
</p>

### Технологии:
**Backend:** Java 17, Microservices, Spring Framework, Spring Boot, Spring Data, Spring Cloud, Spring Webflux, Spring Security, Keycloak, MongoDB, RSocket, Kafka, Redis, Gradle, IntelliJ IDEA.

**Frontend:** Javascript, React, OIDC Client, RSocket, HTML, CSS(Tailwind, Shadcn), VSCode.

**Deployment:** Docker, Docker Swarm, Nginx, DigitalOcean, Linux(Ubuntu).

**AI:** ChatGPT, Grok, Gemini.

## 1. Запуск окружения для локальной разработки 
*(из корня проекта)*
### 1.1 Запуск
```bash
docker compose --project-name kmessenger-backend -f ./deployment/docker-compose.yml -f ./deployment/docker-compose.local.yml --env-file ./deployment/.env.local up -d
```

### 1.2 Отключение и удаление окружения
*(не удаляет volumes)*
```bash
docker compose --project-name kmessenger-backend down
```

## 2. Сборка образов микросервисов:
```bash
gradle clean
```

### ms-profile:
```bash
./gradlew :ms-profile:bootJar
```
```bash
docker build -t ms-profile:1.0.0 -f deployment/services/ms-profile.Dockerfile .
```

### ms-conversation:
```bash
./gradlew :ms-conversation:bootJar
```
```bash
docker build -t ms-conversation:1.0.0 -f deployment/services/ms-conversation.Dockerfile .
```

### ms-gateway:
```bash
./gradlew :ms-gateway:bootJar
```
```bash
docker build -t ms-gateway:1.0.0 -f deployment/services/ms-gateway.Dockerfile .
```

### ms-discovery:
```bash
./gradlew :ms-discovery:bootJar
```
```bash
docker build -t ms-discovery:1.0.0 -f deployment/services/ms-discovery.Dockerfile .
```

## 3. Запуск окружения STAGE 
*(из корня проекта)*
### 3.1 Включение Docker Swarm *(разово, проверка с помощью docker info)*
```bash
docker swarm init
```

### 3.2 Запуск одной командой
```bash
env $(cat ./deployment/.env.stage | grep -v '^#' | xargs) \
docker stack deploy -c ./deployment/docker-compose.yml -c ./deployment/docker-compose.stage.yml kmessenger
```

### 3.3 Точечный перезапуск сервиса
```bash
docker service update --force service_name
```
```bash
docker service update --force kmessenger_nginx-proxy
```

### 3.4 Точечное удаление (отключение) сервиса в Docker Swarm
```bash
docker service scale <имя_сервиса>=0
```
*для сервисов с "mode: global":*
```bash
docker service rm kmessenger_nginx-proxy
```

### 3.5 Удаление окружения (не удаляет volumes)
```bash
docker stack rm kmessenger
```

## 4. Keycloak
4.1 Секреты не экспортируются, поэтому при первом запуске необходимо зайти
в https://kmessenger.net/auth/admin/master/console/#/kmessenger (http://localhost:8443/admin/master/console/#/kmessenger) ->
Clients -> ms-conversation -> Credentials -> Regenerate Client Secret
и заменить на новое значение в зависимости от контура:
Для STAGE/PROD: CONVERSATION_KEYCLOAK_CLIENT_SECRET (.env.stage/prod).
Для LOCAL: conversation.keycloak.client.secret в application-local.yml (ms-conversation).

4.2 Поскольку master realm не импортируется, то для защиты от подбора пароля следует вручную
настроить: Realm Settings (master) → Security Defenses → Brute Force Detection

## 5. MongoDB
### 5.1 Проверка режима Replica Set. В контейнере mongo Exec:
```bash
mongosh --username ЛОГИН --password ПАРОЛЬ
```
```bash
mongosh --username admin --password mongoLocal
```
```bash
rs.status()
```

## 6. Docker Hub
### 6.1 ms-discovery
```bash
docker tag ms-discovery:1.0.0 kandratsenka94/ms-discovery:latest
```
```bash
docker push kandratsenka94/ms-discovery:latest
```
### 6.2 ms-gateway
```bash
docker tag ms-gateway:1.0.0 kandratsenka94/ms-gateway:latest
```
```bash
docker push kandratsenka94/ms-gateway:latest
```
### 6.3 ms-profile
```bash
docker tag ms-profile:1.0.0 kandratsenka94/ms-profile:latest
```
```bash
docker push kandratsenka94/ms-profile:latest
```
### 6.4 ms-conversation
```bash
docker tag ms-conversation:1.0.0 kandratsenka94/ms-conversation:latest
```
```bash
docker push kandratsenka94/ms-conversation:latest
```
