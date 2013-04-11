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
        <title>Action Result</title>
    </head>
    <body>
    <div class="container">
        <h1>Action Result</h1>

        <c:choose>
            <c:when test="${result.success}">
                <c:set var="resultClass" value="text-success"/>
            </c:when>
            <c:otherwise>
                <c:set var="resultClass" value="text-error"/>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty result.message}">
            <p class="<c:out value="${resultClass}"/>">
                <c:out value="${result.message}" />
            </p>
        </c:if>

        <c:if test="${not empty result.previous}">
            <a href="<c:out value="${result.previous}"/>" class="btn btn-inverse">back</a>
        </c:if>

        <c:if test="${not empty result.next}">
            <a href="<c:out value="${result.next}"/>" class="btn btn-primary">next</a>
        </c:if>
    </div>
    </body>
</html>
