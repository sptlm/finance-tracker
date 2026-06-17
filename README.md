# Finance Tracker

Finance Tracker - веб-приложение для учета личных финансов. Проект позволяет вести счета, фиксировать доходы и расходы, распределять операции по категориям и тегам, а также смотреть сводную статистику на дашборде.

## Возможности

- Регистрация и авторизация пользователей.
- Защита основных страниц от неавторизованного доступа.
- Управление финансовыми счетами.
- Учет доходных и расходных транзакций.
- Создание, редактирование и удаление категорий доходов и расходов.
- Создание, редактирование и удаление тегов.
- Фильтрация транзакций по типу, категории, тегам, дате и сумме.
- Дашборд с общим балансом, статистикой за день, неделю, месяц и год.
- Диаграммы доходов и расходов по категориям.
- Просмотр последних транзакций.
- Автоматическое обновление баланса счета при добавлении или удалении транзакций на уровне базы данных.

## Технический стек

### Backend

- Java 21
- Java Servlet API 4.0
- Maven
- JDBC
- HikariCP
- PostgreSQL
- Jackson
- SLF4J

### Frontend

- FreeMarker
- HTML/CSS
- Bootstrap 5
- Bootstrap Icons
- jQuery
- Chart.js

### Инфраструктура

- Docker
- Docker Compose
- Tomcat 9
- PostgreSQL 16

## Структура проекта

```text
src/main/java/kfu/itis/maslennikov/financetracker
+-- dao/          # Работа с базой данных
+-- dto/          # DTO-объекты
+-- entity/       # Доменные сущности
+-- exception/    # Пользовательские исключения
+-- filter/       # Servlet-фильтры
+-- listener/     # Инициализация сервисов при старте приложения
+-- service/      # Бизнес-логика
+-- servlet/      # HTTP-контроллеры
+-- util/         # Утилиты приложения

src/main/webapp/WEB-INF/views
+-- *.ftl         # FreeMarker-шаблоны страниц

docker/init
+-- schema.sql    # SQL-схема базы данных
```

## Требования

Для запуска через Docker:

- Docker
- Docker Compose

Для локального запуска без Docker:

- JDK 21
- Maven
- PostgreSQL
- Tomcat 9

## Быстрый запуск через Docker

1. Создайте файл конфигурации приложения:

```powershell
Copy-Item src/main/resources/config-example.json src/main/resources/config.json
```

2. Запустите приложение и базу данных:

```powershell
docker compose up -d --build
```

3. Откройте приложение в браузере:

```text
http://localhost:8080
```

4. После первого запуска можно посмотреть логи приложения:

```powershell
docker compose logs app
```

5. Остановить контейнеры можно командой:

```powershell
docker compose down
```

Если нужно удалить также данные PostgreSQL, используйте:

```powershell
docker compose down -v
```

## Локальный запуск без Docker

1. Создайте базу данных PostgreSQL.

2. Выполните SQL-скрипт со схемой:

```text
docker/init/schema.sql
```

3. Создайте файл `src/main/resources/config.json` на основе примера:

```powershell
Copy-Item src/main/resources/config-example.json src/main/resources/config.json
```

4. Укажите переменные окружения для подключения к PostgreSQL:

```powershell
$env:FIN_URL = "jdbc:postgresql://localhost:5432/finance"
$env:FIN_USER = "finance"
$env:FIN_PASSWORD = "finance"
```

5. Соберите WAR-файл:

```powershell
mvn clean package
```

6. Разверните файл из директории `target` в Tomcat 9.

## Конфигурация

Приложение использует переменные окружения для подключения к базе данных:

| Переменная | Описание | Пример |
| --- | --- | --- |
| `FIN_URL` | JDBC URL PostgreSQL | `jdbc:postgresql://localhost:5432/finance` |
| `FIN_USER` | Имя пользователя БД | `finance` |
| `FIN_PASSWORD` | Пароль пользователя БД | `finance` |

Файл `config.json` также загружается приложением при старте. В репозитории есть шаблон:

```text
src/main/resources/config-example.json
```

Сам `config.json` не хранится в Git, потому что может содержать локальные настройки и секреты.

## База данных

Схема базы данных находится в файле:

```text
docker/init/schema.sql
```

В ней создаются таблицы:

- `users`
- `currencies`
- `accounts`
- `categories`
- `transactions`
- `tags`
- `transaction_tags`

Также создается триггер `trg_update_balance`, который обновляет баланс счета после добавления или удаления транзакции.

Важно: таблица `currencies` создается схемой, но начальные записи валют в SQL-скрипте не добавляются. Для создания счетов нужно заранее добавить хотя бы одну валюту, например `RUB`:

```sql
INSERT INTO currencies (code, name, symbol, exchange_rate_to_rub)
VALUES ('RUB', 'Russian Ruble', '₽', 1.0000)
ON CONFLICT (code) DO NOTHING;
```

## Основные маршруты

| Маршрут | Назначение |
| --- | --- |
| `/login` | Вход в систему |
| `/register` | Регистрация |
| `/logout` | Выход |
| `/dashboard` | Главная панель со статистикой |
| `/accounts` | Управление счетами |
| `/transactions` | Управление транзакциями |
| `/categories` | Управление категориями |
| `/tags` | Управление тегами |
| `/profile` | Профиль пользователя |

## Сборка

Проект собирается Maven в WAR-архив:

```powershell
mvn clean package
```

После сборки результат будет находиться в директории:

```text
target/
```

## Примечания

- Приложение разворачивается в Docker как `ROOT.war`, поэтому доступно по корневому адресу `http://localhost:8080`.
- При регистрации пользователя автоматически создаются стандартные категории доходов и расходов.
- Пароли пользователей хранятся в виде хеша.
- Доступ к страницам `/profile`, `/dashboard`, `/accounts`, `/categories`, `/tags` и `/transactions` разрешен только авторизованным пользователям.
