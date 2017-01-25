# Android Time Logger Application

An android application which logs time the user spent on various activities during the day.
---

The app's main activity UI:

![mainUI](https://sc-cdn.scaleengine.net/i/f7980561d982a561b17c77fd01a95cc4.png)

The application allows a user to define and store task records with an hourly accuracy. 

While creating a new task user can define the activity's name, it's optional description and the color which will be displayed on the application's graphic.

![addingactivity](https://media.giphy.com/media/26xBQF2bSOspcLhU4/source.gif)

The application uses SQLite database which can be created upon first application's run along with all it's necessary tables.
 
Every defined activity can have it's name, description and the color changed:
 
 ![editgif](https://media.giphy.com/media/26xBEOpt9ewLvr13a/source.gif) 
 
 
 Every time the user leaves time period without any activity entry, the application fills the chart's record void with
  white space.
  Every line represents one day, divided by 24 hour blocks. 
  

  When creating a new record for a given activity, the user must pass the data about the record's starting time 
  and ending time. The default time in appearing dialog will be set to the ending of the last record in the database.
  
  ![addingrecord](https://media.giphy.com/media/26xBxo8itSYJ6rHXy/source.gif)
    
  
  The user can change the application's language at a runtime. Available languages are: Polish, English(default)
  
  ![languagegif](https://media.giphy.com/media/l3q2rD43ghbLMVE3e/source.gif)
  
  
  The user can set visibility of data displayed on the graph via the slider setting how many days are displayed:
  
  ![settings-canvas](https://media.giphy.com/media/26xBQlbDjuH9bjU9W/source.gif)
  
  If the application runs into any exception or malformed input data, it will display an alert at the bottom of the screen:
  
  ![alertgif](https://media.giphy.com/media/l3q2CEqZIHWxG4Owo/source.gif)
  
  
