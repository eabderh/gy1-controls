

window.onload = function() {

	var connectbtn = document.getElementById("connectbtn");
	var onbtn = document.getElementById("onbtn");
	var offbtn = document.getElementById("offbtn");
	var swingbtn = document.getElementById("swingbtn");

	var element = document.getElementById("elias-text-test");


	alert('page load');
	var socket = io.connect('http://www.gyrobot.tech:80');
	socket.on('message', function (data) {
//		alert(data);
		if (data) {
			element.innerHTML = data;
			}
		});
	socket.on('timer', function (data) {
		if (data) {
			element.innerHTML = data.countdown;
			}
		});
	socket.emit('message', 'client: first');

	connectbtn.addEventListener("click", function(e) {
		socket.emit('timer', 'client: reset');
		}, false);
//	connectbtn.addEventListener("click", function(e) {
//		socket.emit('message', 'client: connectbtn');
//		}, false);

	onbtn.addEventListener("click", function(e) {
		socket.emit('message', 'client: onbtn');
		}, false);

	offbtn.addEventListener("click", function(e) {
		socket.emit('message', 'client: offbtn');
		}, false);

	swingbtn.addEventListener("click", function(e) {
		socket.emit('message', 'client: swingbtn');
		}, false);
	}




