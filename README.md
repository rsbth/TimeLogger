# TimeLogger
A java application which logs time the user spent on various activities during the day.
---

Desktop Application uses Material Design controls from a JFoenix library: [JFoenix](https://github.com/jfoenixadmin/JFoenix)
, therefore it requires JRE version at least  1.8u60

![mainUI](https://sc-cdn.scaleengine.net/i/3fdd2852f87244f11c967970fcc80de16.png)

The application allows a user to define and store activity records with an hourly accuracy. 

While creating a new activity user can define the activity's name and it's optional description.

![addingactivity](https://media.giphy.com/media/26FLe77hh5Mpvf7JC/source.gif)

The application uses SQLite database which can be created upon first application's run along with all it's necessary tables.
 
Every defined activity gets a color which can be later changed by the user and which will be displayed below the 
 activities list as a summary of all user's entries:
 
 ![colorgif](https://media.giphy.com/media/l4Jz4xdJPubiCz3pu/source.gif) 
 
 
 Every time the user leaves time period without ant activity entry, the application fills the chart's record void with
  white space.
  Every line represents one day, divided by 24 hour blocks. 
  

  When creating a new record for a given activity, tha user must pass the data about the record's starting time 
  and ending time. The default time in appearing popup will be set to the ending of the last record in the database.
  
  ![addingrecord](https://media.giphy.com/media/l4Jzi70Jp2lGOoi5y/source.gif)
    
  
  The user can change the application's language at a runtime. Available languages are: Polish, English(default)
  
  ![gif](https://media.giphy.com/media/l4JyUWLfF9TaY4UPC/source.gif)
  
  
  If the application runs into any exception or malformed input data, it will display an alert at the bottom of the screen:
  
  ![alertgif](https://media.giphy.com/media/26FKVVlX6D8R9I97q/source.gif)
  
  ---
  
  To generate and run a jar file using maven:
  
  1. In a folder with scr folder : `mvn package`
  
  2. `cd target`
  
  3. `java -jar time-logger-desktop-1.0-jar-with-dependencies.jar`