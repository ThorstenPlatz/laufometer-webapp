<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
        <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    </head>
    <body>
        <div class="container">
            <h1>Error</h1>

            <h2>Error Details</h2>

            <ul>
                <li>message: <c:out value="${it.result.message}"/></li>
                <li>errors:
                    <c:choose>
                        <c:when test="${empty it.result.info}">
                        none
                        </c:when>
                        <c:otherwise>
                            <ul>
                            <c:forEach var="error" items="${it.result.info}">
                                <li><c:out value="${error}"/></li>
                            </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div> <!-- container -->
    </body>
</html>
