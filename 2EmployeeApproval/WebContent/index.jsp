<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="//jqueryvalidation.org/files/demo/css/cmxform.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- https://jqueryvalidation.org/documentation/ -->
<script src="https://jqueryvalidation.org/files/dist/jquery.validate.js"></script>
<script src="doedje-jquery.soap-1.4.2/jquery.soap.js"></script>
<script src="doedje-jquery.soap-1.4.2/xml2json.js"></script>

<script src="jquery.mobile-1.4.5/jquery.mobile-1.4.5.js"></script>
<link rel="stylesheet" href="jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee Approval</title>

</head>
<body>
<script>

	String.prototype.format = function() {
	  var str = this;
	  for (var i = 0; i < arguments.length; i++) {       
	    var reg = new RegExp("\\{" + i + "\\}", "gm");             
	    str = str.replace(reg, arguments[i]);
	  }
	  return str;
	}
	
	var wsUri = "ws://"+location.host+"${pageContext.request.contextPath}/notifyRegistration";
	function connect(){
		websocket = new WebSocket(wsUri);
		websocket.onopen = function(event) { console.log("open") };
		websocket.onmessage = function(event) {
			event.data
			   
			console.log("register: "+event.data);
	    	 
			$.soap({
			    url: 'http://rafael-pc:8080/WebServices/UsersServicesService',
			    envAttributes: { 'xmlns:de': 'http://de/'},
			    appendMethodToURL : false,
			    method: 'de:get',
			    data: { id_register: event.data }
			    ,
			    beforeSend: function (SOAPEnvelope)  {
			    	console.log(SOAPEnvelope);
			    }, 
			    success: function (SOAPResponse) {
			        // do stuff with soapResponse
			        // if you want to have the response as JSON use soapResponse.toJSON();
			        // or soapResponse.toString() to get XML string
			        // or soapResponse.toXML() to get XML DOM
			        debugger;
			        var r = SOAPResponse.toJSON()["#document"]["S:Envelope"]["S:Body"]["ns2:getResponse"]["return"];
			        var images = ["album-af.jpg", "album-ag.jpg", "album-bb.jpg", "album-bk.jpg", "album-hc.jpg", "album-k.jpg", "album-mg.jpg", "album-ok.jpg"];
			        
			        var image = images[Math.floor(Math.random()*images.length)];
			        
			        var vals = "{"; 
			        Object.getOwnPropertyNames(r).splice(1).forEach(function(e,i,a){ 
			        	vals+=e+": "+r[e]; 
			        	if(i!=a.length-1){ vals+=", "} 
			        });
			        vals+="}";
			        
			        var id = $("#popup_list").children().size();
			        
			        debugger;
			        $("#popup_list").append(
			        	'<div data-role="popup" id="purchase'+id+'" data-theme="a" data-overlay-theme="b" class="ui-content" style="max-width:340px; padding-bottom:2em;">'+
			        	'<h3>Decision</h3>'+
			        	'<p>Action regarding the new user named "'+r.name+'"</p>'+
			        	'<p>'+vals+'</p>'+
			        	'<a href="#" id="approve'+id+'" class="ui-shadow ui-btn ui-corner-all ui-btn-b ui-icon-check ui-btn-icon-left ui-btn-inline ui-mini">Approve</a>'+
			        	'<a href="#" id="discard'+id+'" class="ui-shadow ui-btn ui-corner-all ui-btn-inline ui-mini">Discard</a>'+
			        	'</div>'
			        );
			        
			        function discard(){
			        	$("#purchase"+id).remove();
			        	$("#pending"+id).remove();
			        	$("#pending").listview('refresh');
			        }
			        
			        $( "#approve"+id ).click(function() {
		        	  $.soap({
		  			    url: 'http://rafael-pc:8080/WebServices/UsersServicesService',
		  			    envAttributes: { 'xmlns:de': 'http://de/'},
		  			    appendMethodToURL : false,
		  			    method: 'de:approve',
		  			    data: { 
		  			    	id_employee: $("#id_employee").val(),
		  			    	id_register: r.id_register
		  			    }
		        	  });
		        	  discard();
		        	});
			        $( "#discard"+id ).click(function() {
		        	 $.soap({
		  			    url: 'http://rafael-pc:8080/WebServices/UsersServicesService',
		  			    envAttributes: { 'xmlns:de': 'http://de/'},
		  			    appendMethodToURL : false,
		  			    method: 'de:discard',
		  			    data: { 
		  			    	id_register: r.id_register
		  			    }
		        	  });
		        	 discard()
		        	});
			        $('#purchase'+id+'').popup();
			        $("#pending").append('<li id="pending'+id+'"><a href="#"><img src="jquery.mobile-1.4.5/demos/_assets/img/'+image+'">'+
			    		    '<h2>'+r.name+'</h2>'+
			    		    '<p>'+vals+'</p></a>'+
			    		    '<a href="#purchase'+id+'" data-rel="popup" data-position-to="window" data-transition="pop">Purchase album</a>'+
			    		    '</li>');
			        $("#pending").listview('refresh');
			        console.log(r);
			    },
			    error: function (SOAPResponse) {
			        // show error
			        debugger;
			        console.log(SOAPResponse)
			    }
			});
			/*$.ajax({
				url: "http://rafael-pc:8080/WebServices/UsersServicesService", 
				type: "POST",
				dataType: "xml", 
				data: soapMessage, 
				processData: false,
				contentType: "text/xml; charset=\"utf-8\"",
				success: function(){ console.log("Ok");}, 
				error: function(){ console.log("Fails");}
			});*/   
			
			
			console.log("get") 
		};
		//websocket.onclose
		//websocket.onerror
	}
	connect();
</script>

<label for="basic">Employee Id</label>
<input type="text" id="id_employee" value=""  />

<!-- http://demos.jquerymobile.com/1.4.0/listview/ -->
<ul id="pending" data-role="listview" data-split-icon="gear" data-split-theme="a" data-inset="true">
    <!--
    <li><a href="#"><img src="jquery.mobile-1.4.5/demos/_assets/img/album-af.jpg">
    <h2>Broken Bells</h2>
    <p>Broken Bells</p></a>
    <a href="#purchase" data-rel="popup" data-position-to="window" data-transition="pop">Purchase album</a>
    </li>
    -->
</ul>

<div id="popup_list">
</div>

<!-- https://demos.jquerymobile.com/1.2.0/docs/pages/popup/ -->
<!--
<div data-role="popup" id="purchase" data-theme="a" data-overlay-theme="b" class="ui-content" style="max-width:340px; padding-bottom:2em;">
    <h3>Purchase Album?</h3>
<p>Your download will begin immediately on your mobile device when you purchase.</p>
    <a href="index.html" data-rel="back" class="ui-shadow ui-btn ui-corner-all ui-btn-b ui-icon-check ui-btn-icon-left ui-btn-inline ui-mini">Buy: $10.99</a>
    <a href="index.html" data-rel="back" class="ui-shadow ui-btn ui-corner-all ui-btn-inline ui-mini">Cancel</a>
</div>
-->

<!--
<script>
	$("#pending").append("<li><a href='#'><img src='jquery.mobile-1.4.5/demos/_assets/img/album-af.jpg'>"+
		    '<h2>Broken Bells</h2>'+
		    '<p>Broken Bells</p></a>'+
		    '<a href="#purchase" data-rel="popup" data-position-to="window" data-transition="pop">Purchase album</a>'+
		    '</li>');
</script>
-->

</body>
</html>