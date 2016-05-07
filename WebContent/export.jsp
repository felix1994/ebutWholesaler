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

<p>Export entire product catalog or just some products</p>
	
<form action=<%= response.encodeURL("controllerservlet?action="+Constants.ACTION_CREATE_XML)%> method="post" enctype="multipart/form-data">
	<!-- <input type="hidden" name="action" value="store_catalog"> -->
	   <input type="submit" value="create_XML" />
</form>	

</body>
</html>