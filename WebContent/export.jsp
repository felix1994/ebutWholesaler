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
<!--  
<h3>Options, export:</h3>
<div>
	<form action=<%= response.encodeURL("controllerservlet?action="+Constants.ACTION_CREATE_XML)%> method="post">
	   Export the whole catalog <input type="submit" value="Export" />
	   <input type="hidden" name="searchstring" value="fullcatalog"/>
	</form>	
</div>
<br>
<div>
	<form action=<%= response.encodeURL("controllerservlet?action="+Constants.ACTION_CREATE_XML)%> method="post">
	   Export some produts by entering your searchstring: <input type="text" name="searchstring"/>
	   <input type="submit" value="Export some productproducts" />
	</form>	
</div>
<br>
-->
<fieldset>
	<legend><h1>Export whole product catalog</h1></legend>
	<p>Choose output:</p>
	<form action="rest/hello/createoutput" method="get">
	<input type="hidden" name="searchstring" value="fullcatalog"/>
		<table>
			<tr><input type="radio" name="outputtype" value="xhtml"/>XHTML</tr>
			<tr><input type="radio" name="outputtype" value="xml"/>XML file for download</tr>
			<tr><input type="submit" value="Complete export" /></tr>
		</table>		
	</form>
</fieldset>

<fieldset>
	<legend><h1>Selective export of products</h1></legend>
	<p>Choose output:</p>
	<form action="rest/hello/createoutput" method="post">
		<table>
			<tr><input type="radio" name="outputtype" value="xhtml"/>XHTML</tr>
			<tr><input type="radio" name="outputtype" value="xml"/>XML file for download</tr>
		</table>
		<br>
		 Export some produts by entering your searchstring: <input type="text" name="searchstring"/>
		
		<input type="submit" value="Selective export" />
	</form>
</fieldset>



</body>
</html>