<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Scheduler</title>
</head>

<body>
	<table>
		<thead>
			<tr>
				<th> scheduler_report_id </th>
				<th> day </th>
			</tr>
		  <c:forEach var="item" items="${list }">
		  	<tr>
		  		<td>${item.scheduler_report_id }</td>
		  		<td>${item.day }</td>
		  	</tr>
		 </c:forEach>
	</table>
</body>
</html>
