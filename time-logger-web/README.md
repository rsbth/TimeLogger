#TimeLoggerWeb

A GWT application serving as a frontend for TimeLoggerServer. The application will present the data requested from the server in a web browser and will allow the user to interact with it. 

The application uses open source library [GWTMaterialDesign](https://github.com/GwtMaterialDesign/gwt-material)

The main page presents a table of tasks:

![tasksTable](https://sc-cdn.scaleengine.net/i/efbf81467528915880097bef20ee3f0a3.png)

The app will the user to define and store task records with hourly accuracy. 

While creating a new task user can define the activity's name, it's optional description and the color from a dropdown list
 
 ![addTaskGif](https://media.giphy.com/media/l1KVba6NxdC8hTKwM/source.gif)
 
 New task will be sent to server to be stored in the backend's database.
 
 
 When creating a new record for selected task, a user will have to pass the information about record's start and end hour as well as the date of the record.
 
 If the record does not meet app's requirements, an error message will be displayed.
  
 ![addRecordGif](https://media.giphy.com/media/xUPGclpBmuUzC98MRq/source.gif)
 