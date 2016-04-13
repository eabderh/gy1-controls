

window.onload = function() {

	var connectbtn = document.getElementById("connectbtn");
	var onbtn = document.getElementById("onbtn");
	var offbtn = document.getElementById("offbtn");
	var swingbtn = document.getElementById("swingbtn");
	var statusbox = document.getElementById("statusbox");

	var socket = io.connect('http://www.gyrobot.tech:80');

	socket.on('message', function (data) {
		//TODO add random messages handler
		});
	socket.on('assignment', function (data) {
		if (data == "client") {
			statusbox.innerHTML = "CONNECTED"
			statusbox.style.color="#337ab7";
			onbtn.disabled = false;
			offbtn.disabled = false;
			swingbtn.disabled = false;
			}
		else if (data == "none") {
			statusbox.innerHTML = "SERVER DOWN"
			//TODO implement 'none' assignment handler
			}
		});

	connectbtn.addEventListener("click", function(e) {
		socket.send('client');
		}, false);

	onbtn.addEventListener("click", function(e) {
		socket.emit('command', 'on');
		statusbox.innerHTML = "RUNNING";
		statusbox.style.color="green";
		}, false);

	offbtn.addEventListener("click", function(e) {
		socket.emit('command', 'off');
		statusbox.innerHTML = "STOPPED";
		statusbox.style.color="red";
		}, false);

	swingbtn.addEventListener("click", function(e) {
		socket.send('bluetooth_server');
		}, false);
	}



