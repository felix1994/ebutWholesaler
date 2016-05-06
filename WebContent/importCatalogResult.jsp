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

<c:set var="parser" value="${sessionScope.store_catalog}"/>						
<jsp:useBean id="parser" type="de.htwg_konstanz.ebus.wholesaler.main.MyBMEcatParser" />

<c:choose>
  <c:when test="${parser.valid == true}">
    <table class="detail-view">
		<tr>
			<td class="label">Number of articles found in your catalog: </td>
			<td>${parser.articlesFound}</td>
		</tr>
		<tr>
			<td class="label">Number of articles added to our database: </td>
			<td>${parser.articlesAddedToDatabase}</td>
		</tr>
	</table>
  </c:when>
  <c:otherwise>
  	<p>Could not import your product catalog.</p>
    <p>Your xml file must be wellformed and valid.</p>
  </c:otherwise>
</c:choose>
</body>
</html>