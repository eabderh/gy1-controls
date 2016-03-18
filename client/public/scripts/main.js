$(document).ready(function() {
  $('#connectbtn').click(
	  	function () {
	      makeRequest();
	    }            
    );
});

function makeRequest(){
	var xhr = new XMLHttpRequest();
	var url ='http://requestb.in/10i5w2v1'
	//maybe http://172.31.57.1:8080/
	xhr.open("GET", url);
	xhr.send();
}