# TimeLogger
A java application which logs time the user spent on various activities during the day.
--

The project consists of four main components, from which three are completed. 

[TimeLoggerServer](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-server), the RESTful web service with which client side components can communicate. The service accepts, validates and stores data accordingly, as well as sends custom error responses for bad requests.

[TimeLoggerDesktop](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-desktop), an application written with JavaFX which can communicate with server and stores data on it's local database. The application validates requests it's about to send, presents custom error messages when the data entered is malformed and synchronizes any changes made on server on startup. 


Desktop app UI: 

![desktop-ui](https://sc-cdn.scaleengine.net/i/7cbabb5f3570b138657879ef3da4a0cc.png)

[TimeLoggerAndroid](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-android), an android app which has similar functionality as the desktop app. It also stores data obtined from the server on the app's database, synchronizes it with the server and is able to present the data in a graphical form.

![android-ui](https://sc-cdn.scaleengine.net/i/1b659b90f974807ecc253f82270b7d38.png)


[TimeLoggerWeb](https://github.com/mprtcz/TimeLogger/tree/master/time-logger-web) an in progess component for a website which will be able to do all the mentioned things but in a web browser.


![web-ui](https://sc-cdn.scaleengine.net/i/87d5eee02422a54ff0fc9e5f57d9e9bb.png)
