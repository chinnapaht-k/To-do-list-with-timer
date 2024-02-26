const inputBox = document.getElementById("input-box");
const listContainer = document.getElementById("list-container");
let taskNumber = 0;



function addTask(){
    if(inputBox.value === ''){
        alert("You must write something!");
    }
    else if (taskNumber > 50) {
      alert("You should not add more than 50 tasks! Please delete some before add another.");
    }
    else if (inputBox.value.length > 500){
      alert("You should enter less than 500 characters in one task. Please try again.");
    }
    else {
        let li = document.createElement("li");
        li.innerHTML = inputBox.value;
        listContainer.appendChild(li);
        let span = document.createElement("span");
        span.innerHTML = "\u00d7";
        li.appendChild(span);
        taskNumber++;
    }
    inputBox.value = '';
    saveData();
}

listContainer.addEventListener("click",function(e){
    if(e.target.tagName == "LI"){
        e.target.classList.toggle("checked");
        saveData();
    }
    else if(e.target.tagName === "SPAN"){
        e.target.parentElement.remove();
        saveData();
    }
}, false)


function saveData(){
    localStorage.setItem("data", listContainer.innerHTML);
}

function showTask() {
    listContainer.innerHTML = localStorage.getItem("data");
}
showTask()

//Pomodoro Timer 
const timer = { 
    pomodoro:25,
    shortBreak: 5,
    longBreak:15,
    longBreakInterval:4,
    sessions: 0
};

let interval;

const buttonSound = new Audio("button-sound.mp3");
const mainButton = document.getElementById('js-btn'); //listen to the start button and switch between function
mainButton.addEventListener('click', () => {
  buttonSound.play();
  const { action } = mainButton.dataset;
  if (action === 'start') {
    startTimer();
  }
  else {
    stopTimer();
  }
});

const modeButtons = document.querySelector('#js-mode-buttons');
modeButtons.addEventListener('click', handleMode);

function handleMode(event) { //listen to the mode button if click change current mode
  const { mode } = event.target.dataset;

  if (!mode) return;

  switchMode(mode);
  stopTimer();
}


function switchMode(mode){ //change the mode timer by change the timer and add class active to mode that have been clicked and set new background
    timer.mode = mode;
    timer.remainingTime = {
        total:timer[mode] * 60,
        minutes: timer[mode],
        seconds:0,
    };

    document
    .querySelectorAll('button[data-mode]')
    .forEach(e => e.classList.remove('active'));
  document.querySelector(`[data-mode="${mode}"]`).classList.add('active');
  document.body.style.backgroundColor = `var(--${mode})`;

  updateClock();
}

function getRemainingTime(endTime) { //function to be used inside setinterval that execute everry 1000 milisecond --> to get current total, minutes and seconds
    const currentTime = Date.parse(new Date());
    const difference = endTime - currentTime;
  
    const total = Number.parseInt(difference / 1000, 10);
    const minutes = Number.parseInt((total / 60) % 60, 10);
    const seconds = Number.parseInt(total % 60, 10);
  
    return {
      total,
      minutes,
      seconds,
    };
  }


function startTimer() { 
    let { total } = timer.remainingTime; //declare total variable to equal remainingTime that have been updated after switch mode 
    const endTime = Date.parse(new Date()) + total * 1000; //get the current time and add the total time in timer in them (milli seconds)

    if (timer.mode === "pomodoro") timer.sessions++;

    mainButton.dataset.action = 'stop';
    mainButton.textContent = 'stop';
    mainButton.classList.add('active');

  
    interval = setInterval(function() { //excute callback funtion every 1000 mili seconds
      timer.remainingTime = getRemainingTime(endTime);
      updateClock();
  
      total = timer.remainingTime.total;
      if (total <= 0) { //cause setInterval() to be cancelled and countdown end
        clearInterval(interval);
    

      switch (timer.mode){ //if the timer is at 0 the app will automatically switch the mode to the current mode and current session
        case 'pomodoro':
            if (timer.sessions % timer.longBreakInterval ===0) {
            switchMode("longBreak");
      }     else {
            switchMode('shortBreak');
      }
      break;
      default:
        switchMode("pomodoro");
    }
    document.querySelector(`[data-sound="${timer.mode}"]`).play();
    if (Notification.permission === "granted") {
      const text = 
      timer.mode == 'pomodoro' ? "Get back to work!" : "Take a break!";
      new Notification(text);
    }
    startTimer()
  }
    }, 1000);
    
  }


function updateClock(){ //extract the value of remaining time object and pad it to be 2 number
    const { remainingTime } = timer;
    const minutes = `${remainingTime.minutes}`.padStart(2,'0')
    const seconds = `${remainingTime.seconds}`.padStart(2,'0')

    const min = document.getElementById('js-minutes');
    const sec = document.getElementById('js-seconds');

    min.textContent = minutes;
    sec.textContent = seconds;

    const text = timer.mode === 'pomodoro' ? "Time to work!": "Time to take a break!";
    document.title = `${minutes}:${seconds} - ${text}`



}

function stopTimer() { //If the stop button have been clicked.
    clearInterval(interval);
  
    mainButton.dataset.action = 'start';
    mainButton.textContent = 'start';
    mainButton.classList.remove('active');
  }

  document.addEventListener('DOMContentLoaded', () => {
    // Let's check if the browser supports notifications
    if ('Notification' in window) {
      // If notification permissions have neither been granted or denied
      if (Notification.permission !== 'granted' && Notification.permission !== 'denied') {
        // ask the user for permission
        Notification.requestPermission().then(function(permission) {
          // If permission is granted
          if (permission === 'granted') {
            // Create a new notification
            new Notification(
              'Awesome! You will be notified at the start of each session'
            );
          }
        });
      }
    }
  
    switchMode('pomodoro');
  });

