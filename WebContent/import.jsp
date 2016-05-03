<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Import product catalogs</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="error.jsp" %>
<%@ include file="authentication.jsp" %>
<%@ include file="navigation.jspfragment" %>

<h1>Import product catalogs</h1>
	<form action="#" method="post" enctype="multipart/form-data">
	   <p>
		Select a file : <input type="file" name="Datei"/>
	   </p>
	   <input type="submit" value="import" />
	</form>	
</form>
</body>
</html>