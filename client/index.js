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
  console.log(JSON.stringify(request.headers));
  console.log('Get request from %s', ip);
});


server.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});

io.on('connection', function(client) {
	console.log('server recieved connection');
	client.on('message', function(data) {
		console.log(data);
		});
	client.emit('message', 'server: test');


	setInterval(function() {
		countdown--;
		client.emit('timer', {countdown: countdown});
		}, 1000);

	client.on('timer', function(data) {
		countdown = 1000;
		client.emit('timer', {countdown: countdown});
		});
});




