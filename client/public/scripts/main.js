

window.onload = function() {

	var connectbtn = document.getElementById("connectbtn");
	var onbtn = document.getElementById("onbtn");
	var offbtn = document.getElementById("offbtn");
	var swingbtn = document.getElementById("swingbtn");

	var element = document.getElementById("elias-text-test");


	alert('page load');
	var socket = io.connect('http://www.gyrobot.tech:80');
	socket.on('message', function (data) {
		alert(data);
//		if (data) {
//			element.innerHTML = data;
//			}
		});
	socket.on('timer', function (data) {
		if (data) {
			element.innerHTML = data.countdown;
			}
		});

	connectbtn.addEventListener("click", function(e) {
		socket.send('client');
		}, false);

	onbtn.addEventListener("click", function(e) {
		socket.emit('command', 'client: on');
		}, false);

	offbtn.addEventListener("click", function(e) {
		socket.emit('command', 'client: off');
		}, false);

	swingbtn.addEventListener("click", function(e) {
		socket.send('bluetooth_server');
		}, false);
	}




