var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);


app.set('port', (process.env.PORT || 5000));
app.use(express.static(__dirname + '/public'));

app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');



server.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});

app.get('/', function(request, response) {
	var ip = request.headers['x-forwarded-for'] || request.connection.remoteAddress;
	console.log('HTML GET request for index from %s', ip);

	response.render('pages/index');
});

// vid.me links
app.get('/videos', function(request, response) {
	response.render('pages/videos');
});



var server_socket = null;
var client_socket = null;

io.of('/').on('connection', function(socket) {
	console.log('server: received websocket connection');
	//console.log(socket.handshake.headers['x-forwarded-for']);
	//console.log(socket.handshake.headers['user-agent']);

	socket.onclose = function(event) {
		console.log('sockets: closed socket');
		//console.log(socket.handshake.headers['x-forwarded-for']);
		//console.log(socket.handshake.headers['user-agent']);
		};

	socket.on('message', function(data) {
		console.log('sockets: message');
		//console.log(socket.handshake.headers['x-forwarded-for']);
		//console.log(socket.handshake.headers['user-agent']);
		console.log(data);

		if (	data == "appserver" &&
				server_socket == null) {
			server_socket = socket;
			server_socket.onclose = function(event) {
				console.log('sockets: closed appserver');
				server_socket = null;
				};
			server_socket.on('status', function(data) {
				console.log('sockets: appserver status');
				console.log(data);
				if (client_socket != null) {
					if (data = "disconnected") {
						client_socket.emit('assignment', 'none');
						}
					else {
						client_socket.emit('assignment', 'client');
						}
					}
				});
			server_socket.on('angle', function(data) {
				console.log('sockets: appserver angle');
				console.log(data);
				client_socket.emit('angle', data);
				});
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
				console.log('sockets: closed client');
				client_socket = null;
				};
			client_socket.emit('assignment', 'client');
			}
		else {
			socket.emit('assignment', 'none');
			}
		});
	});




