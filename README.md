# TimeLogger
A java application which logs time the user spent on various activities during the day.
--

The project consists of four main components, from which three are completed. 

[TimeLoggerServer](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-server), the RESTful web service with which client side components can communicate. The service accepts, validates and stores data accordingly, as well as sends custom error responses for bad requests.

[TimeLoggerDesktop](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-desktop), an application written with JavaFX which can communicate with server and stores data on it's local database. The application validates requests it's about to send, presents custom error messages when the data entered is malformed and synchronizes any changes made on server on startup. 

[TimeLoggerAndroid](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-android), an android app which has similar functionality as the desktop app. It also stores data obtined from the server on the app's database, synchronizes it with the server and is able to present the data in a graphical form.

[TimeLoggerWeb](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-web) an in progess component for a website which will be able to do all the mentioned things but in a web browser.
