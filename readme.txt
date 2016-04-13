
Setup:
	web server:
		description:
			connects the client and the app server.
			uses websockets for both connections.
			forwards commands from client to app server.
			forwards information from app server to client.
			ginx is used for port forwarding.
		framework:
			nodejs, ginx
		note:
			needs refactoring.
	app server:
		description:
			connects the app to the internet.
			communication between app and app server is via bluetooth.
			communication between app server and web server is via websockets.
		framework:
			java, maven
		note:
			bluetooth not implemented yet.
	client:
		description:
			internet browser with layer 8 wet interface.
			connects to web server via html and websockets.
		framework:
			javascript
	app:
		description:
			gyrobot



