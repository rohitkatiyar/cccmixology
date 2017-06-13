<%@page import="main.MainClass"%>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/xml; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<% response.setContentType("text/xml;charset=UTF-8"); %>
	<%= MainClass.getCocktailRecipe(request.getParameter("d"), request.getParameter("u"))
	%>
</body>
</html>