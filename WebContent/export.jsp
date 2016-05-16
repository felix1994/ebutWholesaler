<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Export product catalogs</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="error.jsp" %>
<%@ include file="authentication.jsp" %>
<%@ include file="navigation.jspfragment" %>
<fieldset class="scheduler-border">
	<legend class="scheduler-border">1. Export our whole product catalog</legend>
	<p>Choose output format:</p>
	<form action="rest/hello/createoutput" method="get">
		<input type="hidden" name="searchstring" value="fullcatalog"/>
		<div class="radio" style="text-indent:20px;">
			<input type="radio" name="outputtype" value="xhtml" checked="checked">XHTML</input>
		</div>
		<div class="radio" style="text-indent:20px;">
			<input type="radio" name="outputtype" value="xml">XML</input>
		</div>
		<button type="submit" class="btn btn-primary" value="export">Complete Export</button>	
	</form>
</fieldset>

<fieldset class="scheduler-border">
	<legend class="scheduler-border">2. Selective export of products</legend>
	<div class="control-group">
	<p>Choose output format:</p>
	<form action="rest/hello/createoutput" method="get">
			<div class="radio" style="text-indent:20px;">	
				<input type="radio" name="outputtype" value="xhtml" checked="checked">XHTML</input>
			</div>
			<div class="radio" style="text-indent:20px;">
				<input type="radio" name="outputtype" value="xml">XML</input>
			</div> 
		 <div>Export some produts by entering your searchstring: (no input means complete export)</div>
		  <input type="text" name="searchstring" class="form-control" placeholder="Enter your searchstring"/>
		<button type="submit" class="btn btn-primary" value="export">Selective export</button>
	</form>
	</div>
</fieldset>
</body>
</html>