var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
//var suspend = require('suspend');
//var resume = suspend.resume;

//suspend(function*() {
//	var data = yield fs.



var countdown = 1000;

app.set('port', (process.env.PORT || 5000));
app.use(express.static(__dirname + '/public'));

// views is directory for all template files
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');



app.get('/', function(request, response) {
  response.render('pages/index');
  var ip = request.headers['x-forwarded-for'] || request.connection.remoteAddress;
  //console.log(JSON.stringify(request.headers));
  console.log('Get request from %s', ip);
});


server.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});

var server_socket;
var client_socket;

io.on('connection', function(socket) {
	console.log('server recieved connection');
	socket.onclose = function(event) {
		console.log('closed socket');
		var socket_ip = socket.handshake.headers['x-forwarded-for'];
		console.log(socket_ip);
		};

	socket.on('message', function(data) {
		var socket_ip = socket.handshake.headers['x-forwarded-for'];
		console.log(socket_ip);
		console.log(data);
		if (data == "bluetooth_server") {
			server_socket = socket;
			server_socket.send('you are bluetooth_server');
			}
		else if (client_socket == undefined) {
			client_socket = socket;
			client_socket.send('you are client');
			client_socket.on('command', function(data) {
				server_socket.send(data);
				});
			}
		});
//	socket.emit('message', 'server: test');
//	setInterval(function() {
//		countdown--;
//		socket.emit('timer', {countdown: countdown});
//		}, 1000);
//
//	socket.on('timer', function(data) {
//		countdown = 1000;
//		socket.emit('timer', {countdown: countdown});
//		});
});




