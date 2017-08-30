//this part is used to create the transition between start and game screen
function switchView(){
	gameend = false;
	document.getElementById('startPage').style.visibility = "hidden";
	document.getElementById('startPage').style.display = "none";
	document.getElementById('canvas').style.visibility = "visible";
	document.getElementById('canvas').style.display = "inline";
	document.getElementById('gamemenu').style.visibility = "visible";
	document.getElementById('gamemenu').style.display = "block";
	document.getElementById("score").style.visibility = "visible";
	document.getElementById("score").innerHTML = currenthighest;
	document.getElementById('end').style.visibility = "hidden";
	document.getElementById('end').style.display = "none";
	drawgame();
	timer();
	init();
}
//this section deals with the pause button
// flag and freeze deal with the pausing functions
var flag = false;
var freeze = false;
//function pause(){
	//freeze time countdown
	//freeze = true;	
	//disable mouse click
	//disable food eating
//}

// play function is used to pause the game
function play(){
	freeze = true;
	flag = true;
	document.getElementById('pause').src = "play.jpg";
	document.getElementById('play').onclick = playbutton;
	timer();
}
// playbutton function is use to resume the game
function playbutton(){
	flag = false;
	freeze = false;
	document.getElementById('pause').src = "pause.jpg";
	document.getElementById('play').onclick = play;
	timer();
	bugspawn();
}
//end of play/pause button section

//this section deals with the countdown timer
var count = 60;
function timer(){
	if (gameend == false) { // make sure a game is playing
	  document.getElementById('timer').innerHTML = count;
	  if (freeze == true){
		  timer();
	  }
	  count--;
	  if (count<0){
		  document.getElementById('timer').innerHTML = 'GAME OVER';
		  gameover(); //count reaches 0
	  } else {
		  if(flag === false){
			  setTimeout(timer,1000);
		  }
		  if(gameend === true){
			  count = 60;
			  document.getElementById('timer').innerHTML = 'GAME OVER';
		  }
	  }
	} else {
		count = 60;
		return;
	}
}
//end of countdown timer section

//this section deals with level select
var level = 1;
function setlevel1(){
	level = 1;
}
function setlevel2(){
	level = 2;
}
//end of level select section

//this section deals with drawing the game
function drawgame(){
	var food = new Image();
	food.onload = function(){
		var ctx = document.getElementById("canvas").getContext("2d");
		for (i=0; i<5; i++){
			//create random x and y coordinates
			var ranx = Math.floor((Math.random() *(350-50+1))+50);
			var rany = Math.floor((Math.random() *(590-300+1)) +300);
			//add new food position to list
			addfood(ranx,rany);
			//draw food on canvas
			ctx.drawImage(food,ranx,rany,20,20);
		}
	};
	food.src = "pixelmeat.png";
}
//end of drawing game section

//DRAW BUG SECTION
// different bug images
var black = new Image();
var red = new Image();
var orange = new Image();
// bug death images
var reddeath = new Image();
var red75 = new Image();
var red50 = new Image();
var red20 = new Image();
var blackdeath = new Image();
var b75 = new Image();
var b50 = new Image();
var b20 = new Image();
var orangedeath = new Image();
var o75 = new Image();
var o50 = new Image();
var o20 = new Image();

function init() {
	// set up all image locations
	black.src = "black.png";
	red.src = "red.png";
	orange.src = "orange.png";
	reddeath.src = "reddeath.png";
	red75.src = "r75.png";
	red50.src = "r50.png";
	red20.src = "r20.png";
	blackdeath.src = "blackdeath.png";
	b75.src = "b75.png";
	b50.src = "b50.png";
	b20.src = "b20.png";
	orangedeath.src = "orangedeath.png";
	o75.src = "o75.png";
	o50.src = "o50.png";
	o20.src = "o20.png";
	
	// initiate some game settings
	bugspawn();
	captureclick();
	document.getElementById("score").innerHTML = x.toString();
}
// Need to set random positions then run draw bug animation
function blackbug(){
	var ctx = document.getElementById("canvas").getContext("2d");
	var xpos = Math.floor(Math.random() *400);
	var x = xpos;
	var y = 0;
	var alive = 1;
	// save bug info into list
	savepos("black",x,y,3,5,x,y,alive);
	var z = poslist.length -1;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var speed = pixspeed/dist;
	//ctx.drawImage(black,xpos,0,10,40);
	var f = 0;
	// draw the bug
	drawbb(z,f,speed);
}
function redbug(){
	var ctx = document.getElementById("canvas").getContext("2d");
	var xpos = Math.floor(Math.random() *400);
	var x = xpos;
	var y = 0;
	var alive = 1;
	savepos("red",x,y,2,4,x,y,alive);
	var z = poslist.length -1;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var speed = pixspeed/dist;
	//ctx.drawImage(black,xpos,0,10,40);
	var f = 0;
	drawrb(z,f,speed);
}
function orangebug(){
	var ctx = document.getElementById("canvas").getContext("2d");
	var xpos = Math.floor(Math.random() *400);
	var x = xpos;
	var y = 0;
	var alive = 1;
	savepos("orange",x,y,1,3,x,y,alive);
	var z = poslist.length -1;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var speed = pixspeed/dist;
	//ctx.drawImage(black,xpos,0,10,40);
	var f = 0;
	drawob(z,f,speed);
}
// spawn some bugs
function bugspawn(){
	// make sure there is a game before spawning the bugs
	if(gameend == false){
	  if (flag) { //if paused
		  bugspawn();
	  }
	  if(count>1) {
		  //random number from 1-3
		  var randtime = Math.floor((Math.random() *3) +1);
		  // random number from 0-100 to pick which bug colour to spawn
		  var ranbug = Math.floor(Math.random()*100);
		  if (ranbug <30){
			  setTimeout(blackbug,randtime*1000);
		  } else if (ranbug >= 30 && ranbug <60) {
			  setTimeout(redbug,randtime*1000);
		  } else {
			  setTimeout(orangebug,randtime*1000);
		  }
		  setTimeout(bugspawn,randtime*1000);
	  }
	}
}
// draw the black bug
function drawbb(z,f,s){
	var ctx = document.getElementById("canvas").getContext("2d");
	var x = poslist[z].posx;
	var y = poslist[z].posy;
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var x2 = foodlist[i].fx;
	var y2 = foodlist[i].fy;
	// if f = 0, not at destination
	f=0;
	poslist[z].initx = x;
	poslist[z].inity = y;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	s = pixspeed/dist;
	
	var x1 = poslist[z].initx;
	var y1 = poslist[z].inity;
	//ctx.drawImage(black,x,y,10,40);
	ctx.clearRect(x,y,10,40);
	f += s;
	x = x1 + (x2-x1)*f;
	y = y1 + (y2-y1)*f;
	
	updatepos(z,x,y);
	// make sure bug is alive
	if (poslist[z].alive == 1) {
		// if not at destination, draw bug
		if (f<1){		
			ctx.drawImage(black,x,y,10,40);
			//ctx.fillRect(x, y, 10, 40);
			requestAnimationFrame(function(){drawbb(z,f,s)});
		} else {
			// if bug at desination
			// game is paused, bugs move but cannot eat and disappear
			if (freeze){
				ctx.clearRect(x,y,10,40);
				poslist[z].alive = 0;
				var food = new Image();
				//food.onload = function(){
				//var ctx = document.getElementById("canvas").getContext("2d");
					food.src = "pixelmeat.png";
					ctx.drawImage(food,x2,y2,20,20);
				//};
				return;
			} else {
				// bug eats food and finds next food	
				ctx.drawImage(black,x,y,10,40);
				//ctx.fillRect(x, y, 10, 40);
				f = 0;
				checkeaten(i);
				list = findfood(x,y);
				i = list[0];
				dist = list[1];
				x2 = foodlist[i].fx;
				y2 = foodlist[i].fy;
				poslist[z].initx = x;
				poslist[z].inity = y;
				if (level == 1) {
					var pixspeed = poslist[z].lv1;
				} else {
					var pixspeed = poslist[z].lv2;
				}
				s = pixspeed/dist;
				drawbb(z,f,s);
			}
		}
	} else {
		// if bug dies draw death animation
		ctx.drawImage(blackdeath,x,y,30,40);
		setTimeout(function(){ctx.drawImage(b75,x,y,30,40)}, 500);
		setTimeout(function(){ctx.drawImage(b50,x,y,30,40)}, 1000);
		setTimeout(function(){ctx.drawImage(b20,x,y,30,40)}, 1500);
		setTimeout(function(){ctx.clearRect(x,y,30,40)}, 2000);
	}
}
function drawrb(z,f,s){
	var ctx = document.getElementById("canvas").getContext("2d");
	var x = poslist[z].posx;
	var y = poslist[z].posy;
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var x2 = foodlist[i].fx;
	var y2 = foodlist[i].fy;
	
	f=0;
	poslist[z].initx = x;
	poslist[z].inity = y;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	s = pixspeed/dist;
	
	var x1 = poslist[z].initx;
	var y1 = poslist[z].inity;
	//ctx.drawImage(black,x,y,10,40);
	ctx.clearRect(x,y,10,40);
	f += s;
	x = x1 + (x2-x1)*f;
	y = y1 + (y2-y1)*f;
	updatepos(z,x,y);
	if (poslist[z].alive == 1) {
		if (f<1){
			ctx.drawImage(red,x,y,10,40);
			//ctx.fillRect(x, y, 10, 40);
			requestAnimationFrame(function(){drawrb(z,f,s)});
		} else {
			if (freeze){
				ctx.clearRect(x,y,10,40);
				poslist[z].alive = 0;
				var food = new Image();
				//food.onload = function(){
				//var ctx = document.getElementById("canvas").getContext("2d");
					food.src = "pixelmeat.png";
					ctx.drawImage(food,x2,y2,20,20);
				//};
				return;
			} else {
				ctx.drawImage(red,x,y,10,40);
				//ctx.fillRect(x, y, 10, 40);
				f = 0;
				checkeaten(i);
				list = findfood(x,y);
				i = list[0];
				dist = list[1];
				x2 = foodlist[i].fx;
				y2 = foodlist[i].fy;
				poslist[z].initx = x;
				poslist[z].inity = y;
				if (level == 1) {
					var pixspeed = poslist[z].lv1;
				} else {
					var pixspeed = poslist[z].lv2;
				}
				s = pixspeed/dist;
				drawrb(z,f,s);
			}
		}
	} else {
		ctx.drawImage(reddeath,x,y,30,40);
		setTimeout(function(){ctx.drawImage(red75,x,y,30,40)}, 500);
		setTimeout(function(){ctx.drawImage(red50,x,y,30,40)}, 1000);
		setTimeout(function(){ctx.drawImage(red20,x,y,30,40)}, 1500);
		setTimeout(function(){ctx.clearRect(x,y,30,40)}, 2000);
	}
	//requestAnimationFrame(redbug);
}
function drawob(z,f,s){
	var ctx = document.getElementById("canvas").getContext("2d");
	var x = poslist[z].posx;
	var y = poslist[z].posy;
	var list = findfood(x,y);
	var i = list[0];
	var dist = list[1];
	var x2 = foodlist[i].fx;
	var y2 = foodlist[i].fy;
	
	f=0;
	poslist[z].initx = x;
	poslist[z].inity = y;
	if (level == 1) {
		var pixspeed = poslist[z].lv1;
	} else {
		var pixspeed = poslist[z].lv2;
	}
	s = pixspeed/dist;
	
	var x1 = poslist[z].initx;
	var y1 = poslist[z].inity;
	//ctx.drawImage(black,x,y,10,40);
	ctx.clearRect(x,y,10,40);
	f += s;
	x = x1 + (x2-x1)*f;
	y = y1 + (y2-y1)*f;
	updatepos(z,x,y);
	if (poslist[z].alive == 1) {
		if (f<1){
			ctx.drawImage(orange,x,y,10,40);
			//ctx.fillRect(x, y, 10, 40);
			requestAnimationFrame(function(){drawob(z,f,s)});
		} else {
			if (freeze){
				ctx.clearRect(x,y,10,40);
				poslist[z].alive = 0;
				var food = new Image();
				//food.onload = function(){
				//var ctx = document.getElementById("canvas").getContext("2d");
					food.src = "pixelmeat.png";
					ctx.drawImage(food,x2,y2,20,20);
				//};
				return;
			} else {
				ctx.drawImage(orange,x,y,10,40);
				//ctx.fillRect(x, y, 10, 40);
				f = 0;
				checkeaten(i);
				list = findfood(x,y);
				i = list[0];
				dist = list[1];
				x2 = foodlist[i].fx;
				y2 = foodlist[i].fy;
				poslist[z].initx = x;
				poslist[z].inity = y;
				if (level == 1) {
					var pixspeed = poslist[z].lv1;
				} else {
					var pixspeed = poslist[z].lv2;
				}
				s = pixspeed/dist;
				drawob(z,f,s);
			}
			
		}
	} else {
		ctx.drawImage(orangedeath,x,y,30,40);
		setTimeout(function(){ctx.drawImage(o75,x,y,30,40)}, 500);
		setTimeout(function(){ctx.drawImage(o50,x,y,30,40)}, 1000);
		setTimeout(function(){ctx.drawImage(o20,x,y,30,40)}, 1500);
		setTimeout(function(){ctx.clearRect(x,y,30,40)}, 2000);
	}
	//requestAnimationFrame(orangebug);
}
// list of positions of each bug (should update every time the position changes)
var poslist = [];
function savepos(c,x,y,a,b,q,w,l){
	poslist.push({colour: c, posx: x, posy:y, lv1:a, lv2:b, initx:q, inity:w, alive:l});
}
// update the positions
function updatepos(i,x,y){
	poslist[i].posx = x;
	poslist[i].posy = y;
}
//list of positions of each food (should update when food is eaten)
var foodlist = [];
function addfood(x,y){
	foodlist.push({fx: x, fy: y});
}
// this function finds the nearest food
function findfood(x,y){
	var x2 = 0;
	var y2 = 0;
	var dx = 0;
	var dy = 0;
	var food = 0;
	var dist = 9999999;
	var mindist = 0;
	// loop through food list to find closest food based on euclidean distance
	for (i = 0; i<foodlist.length; i++){
		x2 = foodlist[i].fx;
		y2 = foodlist[i].fy;
		dx = x2-x;
		dy = y2-y;
		mindist = Math.abs(Math.sqrt(dx*dx + dy*dy));
		if (mindist < dist) {
			dist = mindist;
			food = i;
		}
	}
	// return index of the food and distance in a list so we do not need to recalculate
	return [food, dist];
}
// this function is run when in drawbb/rb/ob, bug reaches it's destination to a food.
function checkeaten(i){
	var ctx = document.getElementById("canvas").getContext("2d");
	// erase the food the bug has eaten
	ctx.clearRect(foodlist[i].fx,foodlist[i].fy,40,40);
	// remove the food entry from the list so that bugs don't look for nonexistant food
	foodlist.splice(i,1);
	if (foodlist.length == 0){
		//all food is eaten
		gameover();
	}
}
// gameend is true if game has ended, false otherwise.  This is used to keep track of the game in certain functions.
var gameend = false;
function gameover(){
	//This function runs when all bugs eat everything or time is up.
	//Should display replay or back to main menu.
	gameend = true;
	count = 60;
	setbestscore();
	document.getElementById("end").style.visibility = "visible";
	document.getElementById("end").style.display = "block";
	document.getElementById("finalscore").style.visibility = "absolute";
	document.getElementById("finalscore").innerHTML = currenthighest;
	
}
// this function returns to the main menu
function mainmenu(){
	gameend = false;
	document.getElementById("canvas").style.visibility = "hidden";
	document.getElementById("canvas").style.display = "none";
	document.getElementById("gamemenu").style.visibility = "hidden";
	document.getElementById("gamemenu").style.display = "none";
	document.getElementById("startPage").style.visibility = "visible";
	document.getElementById("startPage").style.display = "block";
	document.getElementById("end").style.visibility = "hidden";
	document.getElementById("end").style.display = "none";
	currenthighest = 0;
	var ctx = document.getElementById("canvas").getContext("2d");
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	//drawgame();
	throw new Error(''); // there is no error but used to kill all current running functions
}
// this function replays the game on the same level.
function replay(){
	document.getElementById("end").style.visibility = "hidden";
	document.getElementById("end").style.display = "none";
	gameend = false;
	currenthighest = 0;
	var ctx = document.getElementById("canvas").getContext("2d");
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	drawgame();
	timer();
	init();
	throw new Error(''); // there is no error but used to kill all current running functions
}
// TODO LIST:
// need function to calc distance to closest food (done)
// need function to check if bug position = food position (pop out eaten food in list)
// after game over, radio button re-initiates new game abruptly

//  Can add in death animation at the current position and have it fade out.

/****
Erase bug on click
****/
//
function captureclick() {
	var canvas = document.getElementById('canvas');
	canvas.addEventListener('mousedown', function(evt) {
		if (freeze == true){
			return;
		} else {
			var rect = canvas.getBoundingClientRect();
			var x = evt.clientX - rect.left;
			var y = evt.clientY - rect.top;

			buginrange(x, y);
		}
	}, false);
}
// on poslist, find bug within 30px range of mouse click
function buginrange(x, y){
	var idx = 0;
	for (i=0; i<poslist.length; i++){
		if (poslist[i].alive == 1){
		// insert function that calculate distance between click and bugs
			if (Math.abs(((poslist[i].posy+20) - y)) < 50){
				if (Math.abs(((poslist[i].posx+5) - x)) < 35){
					poslist[i].alive = 0;
					//i = poslist.length;
					if(poslist[i].colour == "black"){
						setscore(currenthighest+5);
					} else if (poslist[i].colour == "red"){
						setscore(currenthighest+3);
					} else {
						setscore(currenthighest+1);
					}
				}
			}
		}
	}
	// make image and animation for bug disappear
}

// This store current game's highest score so far
var currenthighest = 0;
function setscore(x){
	currenthighest = x;
	document.getElementById("score").innerHTML = x;
}
// This stores global highest score so far
var alltimehighest = 0;
function setbestscore(){
	if (currenthighest > alltimehighest){
		alltimehighest = currenthighest;
	}
	document.getElementById("highscore").innerHTML = alltimehighest;
}
// end of highest score section

