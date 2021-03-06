<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Customer Registration</title>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="//jqueryvalidation.org/files/demo/css/cmxform.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<!-- https://jqueryvalidation.org/documentation/ -->
	<script src="https://jqueryvalidation.org/files/dist/jquery.validate.js"></script>
	
	
	<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	
	<script>
		$(function() {
		    $( "#birthday" ).datepicker();
			$( "#birthday" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
		});
		String.prototype.format = function() {
		  var str = this;
		  for (var i = 0; i < arguments.length; i++) {       
		    var reg = new RegExp("\\{" + i + "\\}", "gm");             
		    str = str.replace(reg, arguments[i]);
		  }
		  return str;
		}
		
	</script>
</head>
<body>
	<form class="cmxform" id="commentForm">
	<p>
		<strong>Name</strong><br />
		<input id="name" name="name" type="text" required />
	</p>
	<p>
		<strong>Birthday</strong><br />
		<input id="birthday" name="birthday" type="text" id="datepicker" />
	</p>
	<p>
		<strong>Email</strong><br />
		<input id="email" name="email" type="email" required/>
	</p>
	<p>
		<strong>Address</strong><br />
		<input id="name" name="addr" type="text" required/>
	</p>
	<p>
		<strong>Tax Number</strong><br />
		<input id="tax_num" name="tax_num"/>
	</p>
	<p>
      <input class="submit" type="submit" value="Submit">
    </p>
	</form>
	<script>
	$( "#commentForm" ).validate({
		submitHandler: function(form) {  
	       if ($(form).valid()){
	    	   
	    	   var soapMessage = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:de="http://de/"><soapenv:Header/><soapenv:Body>';
	    	   soapMessage += '<de:register>'; 
	    	   soapMessage += '<customer>';
	    	   soapMessage += '<name>{0}</name>';
	    	   soapMessage += '<email>{1}</email>';
	    	   soapMessage += '<birthday>{2}</birthday>'; //1991-06-26T00:00+0100
	    	   soapMessage += '<address>{3}</address>';
	    	   soapMessage += '<taxNum>{4}</taxNum>';
	    	   soapMessage += '</customer>';         
	    	   soapMessage += '</de:register>';      
	    	   soapMessage += '</soapenv:Body>';   
	    	   soapMessage += '</soapenv:Envelope>';
	    	   debugger;
	    	   var birthdayVal = $("#birthday").val();
	    	   if(birthdayVal!=""){
	    		   birthdayVal += "T00:00+0000"
	    	   }
	    	   soapMessage = soapMessage.format(
	    			   $("#name").val(),
	    			   $("#email").val(),
	    			   birthdayVal,
	    			   $("#addr").val(),
	    			   $("#tax_num").val());
	    	   //console.log(soapMessage);
	    	   $.ajax({
	    		    url: "http://rafael-pc:8080/WebServices/UsersServicesService", 
	    		    type: "POST",
	    		    dataType: "xml", 
	    		    data: soapMessage, 
	    		    processData: false,
	    		    contentType: "text/xml; charset=\"utf-8\"",
	    		    success: function(reply){
	    		    	debugger;
	    		    	console.log("Ok");
	    		    }, 
	    		    error: function(reply){
	    		    	debugger;
	    		    	console.log("Fails"); 
	    		    }
	    		});   
	       } 
	       return false; // prevent normal form posting
	     },
		rules: {
		tax_num: {
	      required: true,
	      number: true
	    }
	  }
	});
	</script>
</body>
</html>