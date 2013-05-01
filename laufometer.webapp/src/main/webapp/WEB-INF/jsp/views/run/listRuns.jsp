<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="prefix" value="${pageContext.request.contextPath}"/>

<jsp:useBean id="runs" scope="request" type="java.util.List"/>

<c:set var="datePattern" value="yyyy-MM-dd"/>
<c:set var="timePattern" value="HH:mm:ss"/>
<c:set var="dateTimePattern" value="yyyy-MM-dd HH:mm:ss"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${prefix}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <title>Runs</title>
    </head>
    <body>
    <div class="container">
        <h1>Run Intervals</h1>

        <p class="text-right"><c:out value="${fn:length(runs)}"/> runs</p>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Begin</th>
                    <th>End</th>
                    <th>Satistics</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="run" items="${runs}">
                <tr>
                    <td>
                        <fmt:formatDate value="${run.intervalBegin}" pattern="${datePattern}" /><br/>
                        <fmt:formatDate value="${run.intervalBegin}" pattern="${timePattern}" />
                    </td>
                    <td>
                        <fmt:formatDate value="${run.intervalEnd}" pattern="${datePattern}" /><br/>
                        <fmt:formatDate value="${run.intervalEnd}" pattern="${timePattern}" />
                    </td>
                    <td>
                        <ul>
                            <li>Duration: <fmt:formatNumber value="${run.runDuration}" pattern="##.##" /></li>
                            <li>Distance: <fmt:formatNumber value="${run.runDistance}" pattern="##.##" /></li>
                            <li title="<fmt:formatNumber value="${run.averageSpeed}" pattern="##.##" /> m/s">
                                <c:set var="speedInKmPH" value="${run.averageSpeed * 3.6}"/>
                                Velocity: <fmt:formatNumber value="${speedInKmPH}" pattern="##.##" />
                            </li>
                        </ul>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <a href="javascript:history.back();" class="btn btn-inverse">back</a>
    </div>
    </body>
</html>
