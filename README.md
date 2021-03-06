# Rusquant to Quik API
#### *Trade from R now is real!*

If this project will help you, you can give me a cup of coffee :) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=5XQLMWGEXG9CC)

## About

Данный проект начинался и является частью работ по расширению функционала пакета rusquant для платформы R.
Хотя, строго говоря, следует говорить о том, что данный репозиторий содержит реализацию API к торговому терминалу Quik на языках java и R.
Смена названия репозитория в процессе реализации большого смысла не имела, поэтому названия основных исходных файлов были изменены на названия, начинающиеся с префикса R2Quik... :)

Кроме того, потенциальная реализация API к терминалу Мetatrader (MQL5) будет проходить в рамках этого же проекта.


## Motivation

#### *Мы напишем своего робота с блекжеком и ...*

Возможность построения личной системы алгоритмической торговли представляет собой довольно заманчивую перспективу.
Естественно, никто не говорит о масштабах индустрии: data-центры, мощные сервера, выделенные каналы передачи данных и тд. и тп. Это удел очень больших денег.

Что видит "рядовой" пользователь, желающий автоматизировать торговые операции:
1. Языки R \ Python дают коллосальные возможности для прикладного анализа данных и построения эффективных систем принятия решений (стратегий)
2. Сетевые языки, вроде java (и прочие языки для jvm) и c#, на фоне приемлемой производительности и кросплатформенности, дают коллостальную возможность консолидации данных из совершенно различных источников, а также возможность создания совершенно различных представлений данных (от десктопа, до мобильных приложений). Также они (java) являются хорошим интеграционным слоем для объединения разнородного стэка технологий.
3. Торговые терминалы (Quik) не имеют, по понятным причинам, внятного API для автоматической торговли

Проект rusquant нацелен на создание инструмента для "рядового" пользователя, позволяющего разрабатывать, тестировать и исполнять стратегии в рамках одной среды.

## Architecture

**В основе архитектуры проекта лежат следующие умозаключения:**
1. Отлаживать lua-код под квиком крайне сложное дело. Следовательно, пытаться нагрузить lua-часть сложной бизнесс-логикой - очень плохая идея!
   Поэтому, в основе API максимально простая ping-pong схема с многопоточным клиентом и однопоточным сервером. Клиент и сервер представляют собой подобие конечных автоматов, изменяющих свои сотояния согласованно. Связь клиента и сервера жесткая - на каждый запрос должен быть возвращен ответ, иначе сбой процесса обмена данными.

2. Протокол общения клиента и сервера. Никакого парсинга строк по символам и прочего ада. В качестве протокола общения выбран стандарт JSON. как наиболее "легкий" и распространненый.
   Маппинг на стороне java обеспечивается библиотекой Jackson, а на стороне lua при помощи отличной либы JSON.lua. Весь прикладной код оперирует только встроенными и пользовательскими типами данных (классы в java и таблицы в lua), которые серелиализуются и десериализуются. Никагого "ручного" формирования строковых сообщений

3. Во избежание образования километровых lua-файлов, была применена библиотека, дающая возможность применения ООП в lua. Благодаря ей весь сервеный код было разделен на функциональные модули.

4. С точки зрения конечного пользователя API должно быть максимально простым:
   * Создали коннектор (реализация API) и подключились
   * Пользуемся
   * Отключились и удалили коннектор

   Для достижения этой цели, все функции API имеют одну и туже сигнатуру QuikDataObject doSomething(args), где
   QuikDataObject - обощенный результат. Любая прикладная функция API ВСЕГДА возвращает результат:
   * Валидный объект (подтип QuikDataObject) с данными, в случае успешности обращения
   * Либо объект-ошибку (подтип QuikDataObject)
   Таким образом, для клиента API все выглядит как линейный вызов обычной функции, а вся клиент-серверная кухня оказывается скрыта.
   Названия функций и их аргументов максимально прилижены к спецификации QLua, но некоторые были заменены более осмысленными :)

5. Язык java был выбран в первую очередь потому, что он легко и без боли двусторонне интегрируется с R. Java-код можно вызвать из R, и R можно использовать в java. Возможности, которые дает такая связка, огромны. Правильнее, наверно, было бы написать все на котлине, но его я пока еще не изучил. Использование C++ как среды интеграции накладывает очень много ограничений и боли

6. Проект использует минимально необходимый набор open source библиотек. Библиотеку ffi вы можете скачать и собрать самостоятельно при помощи MS Visual Studio, при этом никаких плясок с бубном не требуется. Либа для работы сокетов в lua находится в открытом доступе. Никаких Trans2Quik.dll :)

7. Реализация поддержки функций обратного вызова, функций QLua, которые требуют создания графиков (обращение через параметр tag), фукнций для программного создания таблиц и их элементов отложено на потом, как не приоритетное в контексте алгоритмической торговли. Хотя, могут быть реализованы все функции из спецификации QLua

## Sources

1. Отправной точкой проекта можно считать статью [On-Line получение данных из Quik в Java и не только*](https://smart-lab.ru/blog/216370.php)
   Ссылка на гитхаб есть в тексте статьи. Спасибо тебе, дружище, твой код очень помог чтобы разобраться с ffi. Только мне не потребовались dll lua от терминала для компиляции ffi.dll
2. Собственно библиотека [ffi](https://github.com/jmckaskill/luaffi), поставляемая с проектом, собрана по сценарию описанному в README
3. Интересно было глянуть реализацию [QUIKSharp](https://github.com/finsight/QUIKSharp)
4. Также очень полезным источником информации является сайт [ИНСТРУМЕНТЫ АЛГОРИТМИЧЕСКОГО ТРЕЙДЕРА](https://quikluacsharp.ru/bez-rubriki/s-chego-nachat/)

## Usage

1. Собрать проект при помощи maven как библитеку или как исполняемый jar для работы с тестовым клиентом
2. Серверная часть (lua) может быть собрана сценрием build.bat, находящимся в папке lua. Для этого на машине должна быть установлена lua (lua for Windows), а путь до бинарников lua должен быть прописан в path. Компиляция lua-кода в байткод не является обязательной.
3. Запустить в терминале Quik скрипт R2QServer.lua. Копирование каких-либо dll не требуется
4. Запустить тестовый java-клиент
6. Для работы с R необходимо установить пакеты rJava и R6. Более подробно смотри файл R2QConnector.R

Весь код разрабатывался и тестировался на:
* Версия ОС: win 7 x64
* Версия Quik: 7.2.0.45.
* Версия java: 8
* Версия lua: 5.1
* Версия R: 3.3.2 x64

Работоспособность на 32-двух разрядной платформе на данный момент не проверялась.


**Для работы сокетов**, возможно, потребуется скопировать lua5.1.dll из директории вашей версии терминала, переименовать ее в lua51.dll и использвать вместо lua51.dll, поставляемой с проектом.
Почему-то, Quik поставляется без lua51.dll, а dll от других версий терминала не всегда совместимы. Библиотека lua51.dll используется внутри core.dll.


**Для работы ffi**, если на компьютере не было установлено никакаих версий Microsoft Visual Studio 2012, то необходимо установить runtime пакеты для этой студии:

visual c++ redistributable for visual studio 2012 update 4 x86
visual c++ redistributable for visual studio 2012 update 4 x64

Эти пакеты можно скачать с официального сайта майкрософт.
Эти пакеты содержат msvcr110.dll, необходимую для работы ffi.dll. Других зависимостей не обнаружено.
Почему лучше поставить оба пакета? Потому что 64-х битная msvcr110.dll ставится с x86 пакетом. А для 64-х битного терминала нужна 64-х битная msvcr110.dll

Для проверки необходимости описанного выше (ffi), достаочно выполнить (не в терминале!!!) lua-код
```
local ffiLib = assert( require "ffi" );
print "Hello World"
```
Если на вашем компьютере нет ``msvcr110.dll``, вы полчите соотвествующее сообщение об ошибке. Для проверки можно поставить lua for windows.