<%@ page session="true" import="de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatParser"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Result of importing product catalog</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="error.jsp" %>
<%@ include file="authentication.jsp" %>
<%@ include file="navigation.jspfragment" %>

<c:set var="importInfo" value="${sessionScope.store_catalog}"/>						
<jsp:useBean id="importInfo" type="de.htwg_konstanz.ebus.wholesaler.main.ImportInformation" />

<c:choose>
  <c:when test="${importInfo.wellformed == false}">
	<p>Could not import your product catalog.</p>
    <p>Your xml file must be wellformed.</p>
  </c:when>
  <c:otherwise>
 	<c:choose>
 		<c:when test="${importInfo.valid == false}">
 			<p>Could not import your product catalog.</p>
    		<p>Your xml file is wellformed but NOT valid.</p>
 		</c:when>
 		<c:otherwise>
 			<c:choose>
		 		<c:when test="${importInfo.supplierFound == false}">
		 			<p>Could not import your product catalog.</p>
    				<p>Database does not contain the SUPPLIER_NAME of your catalog</p>
		 		</c:when>
		 		<c:otherwise>
		 			<p>Number of articles found in your catalog:</p>
				    <p>${importInfo.articlesFound}</p>
				    <p>Number of articles added to our database: </p>
				    <p>${importInfo.articlesAddedToDatabase}</p>
				    <p>${importInfo.productsUpdated} products has been updated</p>
		 		</c:otherwise>
		 	</c:choose>
 		</c:otherwise>
 	</c:choose>
  </c:otherwise>
</c:choose>
</body>	
</html>