

window.onload = function() {

	var connectbtn = document.getElementById("connectbtn");
	var onbtn = document.getElementById("onbtn");
	var offbtn = document.getElementById("offbtn");
	var swingbtn = document.getElementById("swingbtn");
	var anglebox = document.getElementById("anglebox");

	var socket = io.connect('http://www.gyrobot.tech:80');

	socket.on('message', function (data) {
		//TODO add random messages handler
		});
	socket.on('assignment', function (data) {
		if (data == "client") {
			onbtn.disabled = false;
			offbtn.disabled = false;
			swingbtn.disabled = false;
			}
		else if (data == "none") {
			//TODO implement 'none' assignment handler
			}
		});
	socket.on('angle', function (data) {
		if (data) {
			anglebox.innerHTML = data;
			}
		});

	connectbtn.addEventListener("click", function(e) {
		socket.send('client');
		}, false);

	onbtn.addEventListener("click", function(e) {
		socket.emit('command', 'on');
		}, false);

	offbtn.addEventListener("click", function(e) {
		socket.emit('command', 'off');
		}, false);

	swingbtn.addEventListener("click", function(e) {
		socket.send('bluetooth_server');
		}, false);
	}



