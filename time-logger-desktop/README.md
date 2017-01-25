# TimeLogger
A java application which logs time the user spent on various activities during the day.
---

Desktop Application uses Material Design controls from a JFoenix library: [JFoenix](https://github.com/jfoenixadmin/JFoenix)
, therefore it requires JRE version at least  1.8u60

![mainUI](https://sc-cdn.scaleengine.net/i/f107d5d7d3d9f9f2beb28cb3be0908f22.png)

The application allows a user to define and store activity records with an hourly accuracy. 

While creating a new activity user can define the activity's name, it's optional description and the color which will be displayed on the application's graphic.

![addingactivity](https://media.giphy.com/media/26gssXswfD2Dm4TnO/source.gif)

The application uses SQLite database which can be created upon first application's run along with all it's necessary tables.
 
Every defined activity can have it's name, description and the color changed:
 
 ![editgif](https://media.giphy.com/media/26gsjyxP8D96C24eY/source.gif) 
 
 
 Every time the user leaves time period without any activity entry, the application fills the chart's record void with
  white space.
  Every line represents one day, divided by 24 hour blocks. 
  

  When creating a new record for a given activity, the user must pass the data about the record's starting time 
  and ending time. The default time in appearing popup will be set to the ending of the last record in the database.
  
  ![addingrecord](https://media.giphy.com/media/26gsf5rnnVFbVfa1y/source.gif)
    
  
  The user can change the application's language at a runtime. Available languages are: Polish, English(default)
  
  ![languagegif](https://media.giphy.com/media/26gsrKyZ38lme2rhC/source.gif)
  
  
  The user can set visibility of data displayed on the graph and the headers showing which hour and day is displayed:
  
  ![settings-canvas](https://media.giphy.com/media/l0ExnAdwIKDf6I2pa/source.gif)
  
  If the application runs into any exception or malformed input data, it will display an alert at the bottom of the screen:
  
  ![alertgif](https://media.giphy.com/media/l0Ex2AJGyr3EVHHbO/source.gif)
  
  While hovering the mouse over the data graph, tooltip is displayed which contains info about which date the mouse hovers over and which activity (or if there's none) was performed during that hour.
  
  ![tooltipgif](https://media.giphy.com/media/l0ExjE3o5Vbgk5YnC/source.gif)
  
  The user can export and import data from and to xml format. Importing data will replace existing database with the data from selected file,
  if the data is formed correctly.
  
  ![importgif](https://media.giphy.com/media/26xBD2gI4J176WnC0/source.gif)
  
  ---
  
  To generate and run a jar file using maven:
  
  1. In a folder with scr folder : `mvn package`
  
  2. `cd target`
  
  3. `java -jar time-logger-desktop-1.0-jar-with-dependencies.jar`
