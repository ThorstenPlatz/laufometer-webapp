<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <title>All Ticks</title>
    </head>
    <body>
    <div class="container">
        <h1>List of all Ticks</h1>

        <ul>
            <c:forEach var="tick" items="${ticks}">
                <li><c:out value="${tick}" /></li>
            </c:forEach>
        </ul>
    </div>
    </body>
</html>
