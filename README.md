<h1 align="center"><a href=https://t.me/UpdateListenerBot>UpdateListenerBot</a>&#x1F916;</h1>

Телеграм бот, написанный на Java. \
Данный проект полностью написан мной в рамках курса от Тинькофф по бэкенд разработке на Java. В ходе заданий было написано 2
web-сервиса для отслеживания обновления контента по ссылкам: <b>Bot</b> и <b>Scrapper</b>. В сервисе поддерживаются:
<ul>
  <li>Вопросы со StackOverflow</li>
  <li>Репозитории GitHub</li>
</ul>

Управление подписками (ссылками) происходит через чат с ботом в Telegram. При новых изменениях в чат отправляется уведомление. \
\
Сервисы будут общаться между собой как напрямую (по HTTP), так и асинхронно (очередь сообщений). Для хранения данных будет использоваться СУБД PostgreSQL. \
\
Примитивная схема выглядит следующим образом:

<pre class="ql-syntax" spellcheck="false">+-------------+&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
|&nbsp; PostgreSQL |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
+----------|--+&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;+---------|--+&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp; Scrapper&nbsp; --\&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp; ---\&nbsp;&nbsp; +-------------+
&nbsp;+---------|--+&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ---|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp; RabbitMQ&nbsp; |
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |HTTP&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; --|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -/&nbsp; +-------------+
&nbsp;+---------|--+&nbsp;&nbsp; -/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | -/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp;&nbsp;&nbsp; Bot&nbsp;&nbsp;&nbsp;&nbsp; -/&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
&nbsp;+------------+&nbsp;&nbsp;
</pre>

Взаимодействие с базой данных может происходить по одному из следующих способов: JDBC, JPA, jOOQ.

## Взаимодействие с ботом:

Чтобы начать отслеживать ссылку, нужно ввести следующую команду:
```bash
/track <your-tracking-url>
```
![image](https://github.com/vbandurin7/Tinkoff-Homeworks/assets/93590005/e9bf9c09-12b8-4c64-b720-a87fa0115b3f) 

Чтобы прекратить отслеживать ссылку, нужно ввести следующую команду:
```bash
/track <url-to-untrack>
```
![image](https://github.com/vbandurin7/Tinkoff-Homeworks/assets/93590005/7c288e55-935b-4d7c-b5d6-ca28ca314164) 

Чтобы получить список всех отслеживаемых ссылок, нужно ввести следующую команду:
```bash
/list
```
![image](https://github.com/vbandurin7/Tinkoff-Homeworks/assets/93590005/604e878c-9ad6-4870-a1d5-b3291133884a)

# Поддерживаемые на данный момент сообщения об обновлениях:

## GitHub
Оповещение об обновлении репозитория выглядит следующим образом: 
![image](https://github.com/vbandurin7/Tinkoff-Homeworks/assets/93590005/3a6bb0aa-cf88-4884-a217-85cd75a07cad)

## StackOverflow
Оповещение о новых ответах на вопрос выглядит следующим образом:
![image](https://github.com/vbandurin7/Tinkoff-Homeworks/assets/93590005/05013f2c-35e6-4412-882b-f9c5b51cb055)


