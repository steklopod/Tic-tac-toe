#### Задание

Реализовать бекенд для сетевой пошаговой игры в крестики-нолики на Scala.

======

+ В своей реализации для хранения игр, шагов, пользователей и сессий надо использовать `in-memory` реляционную базу данных h2 или другую; 

+ Как Web сервер надо выбрать `Play` или `akka-http`. 

+ Обрабатывать запросы пользователей и в базу можно как синхронно, так и асинхронно, используя, например, `Future`; 

+ Архитектуру приложения нужно разбить на уровни (api/логика игры/хранилище) и написать для каждого уровня `тесты`;

+ В приложении пользователи для авторизации используют заголовок сессии `session`; 

+ Время жизни сессии должно быть 5 минут и при каждом запросе время жизни должно сбрасываться опять на 5 минут, то есть сессия будет считаться истекшей через 5 минут бездействия (отсутствия запросов) от пользователя.

_Подробная инструкция в **pdf**-файле в каталоге `resources`_