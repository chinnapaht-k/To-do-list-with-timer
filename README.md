# To-do list with timer
I created a website where you can add tasks that you want to do and a to-do list. You can add, delete and mark the tasks that have been done. You can also use the Pomodoro timer while doing your task to keep you on track. Also, you can register and log in to track your progress when you click the task to check it and delete it the number of tasks will increment in the progress page and is the same in the timer perspective.

## Features

* To-do list (Main Page): You can add, delete and mark the task or to-do list. Users can add maximum to 50 tasks and maximun of character of the task to 500 character. The task that you have been add will store in the browser history.


* Register: You can register and login to access the progress function that will track all the time and the task that you have been done. The register username should have between 4-10 character. The password should contain at least one character, one number, one special character and should have the length between 6-13. Then you re enter the password to register your username and password to the database. If the user enter the space before or after the username and password the space will be trim. Also the SQL injection will not work.


* Progress: If you successfully register, you can login and then the progres button will appear. When you already login and you mark task and then delete it, the task count will increment. Also if you use the timer, the time that you used will also increment in the progress page.

## Demo
You can access the website at https://chinnapaht.pythonanywhere.com/

## Testing

* You can view my user story,acceptance criteria and test case here:https://docs.google.com/spreadsheets/d/1sKC3WBralXiHd3hYBDcAqLds6UUUwhpR95qJRLZn7Nk/edit?usp=sharing
* You can find automated test scipt in the to do list  folder at this repo.


## Inspiration
To-do list reference by GreatStack : https://www.youtube.com/watch?v=G0jO8kUrg-I&list=PLqKkh2QRDgER_gcDpflt-u6NO0uCIyaSi&index=2

Pomodorotimer reference by Tiff in Tech: https://www.youtube.com/watch?v=8VRNSIc4VeQ&list=PLqKkh2QRDgER_gcDpflt-u6NO0uCIyaSi&index=3

Flask User Authentication by EK-developer : https://www.youtube.com/watch?v=nZRygaTH2MA


