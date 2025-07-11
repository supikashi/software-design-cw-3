# Большая домашняя работа  №3 по дисциплине конструирование программного обеспечения
# Инструкция по запуску и отчёт по проекту

## Содержание репозитория

```
/project-root
├── eureka-server/                 # Eureka Service Discovery
│   ├── src/                       
│   ├── build.gradle.kts
│   └── Dockerfile
├── api-gateway/                   # Базовый API Gateway (Spring Cloud Gateway)
│   ├── src/                      
│   ├── build.gradle.kts
│   └── Dockerfile
├── my-api-gateway/                # Основной шлюз-прокси с бизнес-логикой
│   ├── src/                       
│   ├── build.gradle.kts
│   └── Dockerfile
├── payments-service/          # Сервис для работы с оплатой заказов
│   ├── src/                       
│   ├── build.gradle.kts
│   └── Dockerfile
├── orders-service/         # Сервис для создания заказов
│   ├── src/                       
│   ├── build.gradle.kts
│   └── Dockerfile
└── docker-compose.yml             # Оркестрация всех контейнеров
```

---

## Технологии

* **Java 17**
* **Spring Boot** 
* **Spring Cloud Netflix Eureka** — сервис регистрации и discovery
* **Spring Cloud Gateway** — API Gateway (WebFlux)
* **Spring WebMVC** блокирующих контроллеров (RestTemplate)
* **Spring Data JPA** + **H2** — для хранения данных во время исполнения
* **SpringDoc OpenAPI** (Swagger UI)
* **Docker** & **Docker Compose**
* **RabbitMQ** & **Spring AMQP** — брокер сообщений для асинхронного взаимодействия сервисов

---

## Архитектура микросервисов

1. **eureka-server** (порт **8761**) — централизованный сервер Discovery.
2. **api-gateway** (порт **8081**) — маршрутизирует внешние запросы.
3. **my-api-gateway** (порт **8082**) — пользовательский шлюз, отвечающий за:

   * Приём запросов по Swagger UI
   * Пересылку на payments-service и orders-service
   * Обработку ошибок
4. **payments-service** (порт **8083**) — отвечает за работу с пользовательскими счетами и производит авто-оплату заказов
5. **orders-service** (порт **8084**) — отвечает за работу с заказами (создание, просмотр статуса)

**Семантика оплаты заказа**

Реализована семантика exactly once

**OrdersService**: После получения order сервисом запроса на создание заказа в одной транзакции в базу данных записывается новый заказ со статусом NEW, а также в outbox таблицу записывается задача на оплату заказа.

**orders-service/OutboxPublisher**: Воркер раз в секунду читает outbox таблицу и отправляет в очередь сообщений все непомеченные записи, после чего помечает их как отправленные. То есть сообщения по каждой записи будут отправлены хотя бы один раз.

**payments-service/InboxListener**: Слушает очередь сообщений, хранит базу данных прочитанных сообщений и игнорирует сообщения id которых уже есть в базе данных. 
В рамках транзакции записывает сообщение в базу прочитанных сообщений, списывает деньги (если возможно) и в outbox таблицу записывает задачу на изменение статуса заказа. 
Если транзакция удалась помечает сообщение прочитанным, а если нет, то возвращает его в очередь.

**payments-service/OutboxPublisher**: Такой же воркер на отправку сообщений назад в сервис заказов.

**orders-service/InboxListener**: Слушает очередь сообщений, и также после записи в базу данных нового статуса заказа, помечает сообщение прочитанным.

## Инструкция по запуску

### 1. Предварительные требования

* Установлён **Docker** (Docker Engine) и **Docker Compose**
* Убедитесь, что порты **8761**, **8081**, **8082**, **8083**, **8084** свободны

### 2. Сборка и запуск контейнеров

В корне проекта выполните:

```bash
# Сборка образов и старт сервисов в фоне
docker-compose up --build -d
```

### 3. Проверка сервисов (запускаются не моментально, иногда нужно немного подождать, чтобы сервисы стали отвечать на запросы сваггер)

* **Eureka UI**:
  [http://localhost:8761](http://localhost:8761)
* **My API Gateway (основной)**:
  [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)


### 4. Остановка и очистка

```bash
docker-compose down
```

---
