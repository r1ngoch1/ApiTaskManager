
# ApiTaskManager



## Инструкция по запуску

1. **Склонируйте репозиторий** на компьютер с помощью команды:
    ```bash
    git clone https://github.com/r1ngoch1/ApiTaskManager.git
    ```

2. **Создайте в проекте файл `.env`**.

3. **Откройте файл `.env-example`**, скопируйте все в `.env`, и измените значения от пользователя БД на ваши данные.

4. Перейдите в корневую папку проекта и  пропишите следующую команду для сборки:
    ```bash
    mvn clean package
    ```

5. Дальше запустите **docker compose**. Для этого пропишите следующую команду:
    ```bash
    docker compose up --build
    ```

6. Если хотите завершить работу, используйте команду:
    ```bash
    docker compose down
    ```

7. Перейдите по ссылке http://localhost:8080/swagger-ui/index.html, чтобы увидеть функционал сервиса.
