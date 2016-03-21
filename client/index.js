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


var server_socket = null;
var client_socket = null;

io.on('connection', function(socket) {
	console.log('server recieved connection');
	console.log(socket.handshake.headers['x-forwarded-for']);
	console.log(socket.handshake.headers['user-agent']);
	socket.onclose = function(event) {
		console.log('closed socket');
		var socket_ip = socket.handshake.headers['x-forwarded-for'];
		console.log(socket_ip);
		console.log(socket.handshake.headers['user-agent']);
		};

	socket.on('message', function(data) {
		var socket_ip = socket.handshake.headers['x-forwarded-for'];
		console.log(socket_ip);
		console.log(data);
		if (data == "bluetooth_server" && server_socket == null) {
			server_socket = socket;
			server_socket.onclose = function(event) {
				console.log('closed server');
				var socket_ip = socket.handshake.headers['x-forwarded-for'];
				console.log(socket_ip);
				server_socket = null;
				};
			server_socket.emit('assignment', 'server');
			}
		else if (	data == "client" &&
					client_socket == null &&
					server_socket != null) {
			client_socket = socket;
			client_socket.on('command', function(data) {
				console.log('client command');
				console.log(data);
				server_socket.emit('command', data);
				});
			client_socket.onclose = function(event) {
				console.log('closed client');
				var socket_ip = socket.handshake.headers['x-forwarded-for'];
				console.log(socket_ip);
				client_socket = null;
				};
			client_socket.emit('assignment', 'client');
			}
		else {
			socket.emit('assignment', 'none');
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




