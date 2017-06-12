<%@page import="main.MainClass"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/xml; charset=ISO-8859-1"/>
	<title>Insert title here</title>
	</head>
	<body>
		<h1>Computer Cooking Contest - Mixology Challenge</h1>
	
		<%
			if(request.getParameter("d") == null) {
			out.println("Please retry with desired ingredients list.");	
			} else {
		%>
			<h3><u>Desired Ingredients:</u></h3>
			<b><%= request.getParameter("d") %></b>
			<br>
			<br>
			<h3><u>Undesired Ingredients:</u></h3>
			<b><%= request.getParameter("u") %></b>
			<h3> Recipe:: </h3>		
			<%= MainClass.getCocktailRecipe(request.getParameter("d"), request.getParameter("u")) %>	
		<%}	%>
	 
	</body>
</html>