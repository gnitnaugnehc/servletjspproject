<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="head.jsp"%>
<meta charset="UTF-8">
<title>GraphQL Test</title>
</head>
<body>
	<h1>GraphQL Test</h1>
	<form action="${pageContext.request.contextPath}/graphql" method="POST">
		<label for="query">Query:</label> <input type="text" id="query"
			name="query" size="50"> <br>
		<button type="submit">Execute</button>
	</form>
	<c:if test="${not empty data}">
		<h2>Result:</h2>
		<pre>
			<c:out value="${data}" />
		</pre>
	</c:if>
</body>
</html>
