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
        <title>Watchdogs</title>
    </head>
    <body>
    <div class="container">
        <h1>Watchdog Status</h1>

        <table class="table">
            <thead>
                <tr>
                    <th>ClientId</th>
                    <th>Notification Recipient</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="watchdog" items="${watchdogs}">
                <c:choose>
                    <c:when test="${watchdog.lastCheckResult.present}">
                        <c:choose>
                            <c:when test="${watchdog.alive}">
                                <c:set var="rowCss" value="success"/>
                                <c:set var="statusMsg" value="ok"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowCss" value="error"/>
                                <c:set var="statusMsg" value="keepalive overdue"/>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <c:set var="rowCss" value="warning"/>
                        <c:set var="statusMsg" value="keepalive not checked yet"/>
                    </c:otherwise>
                </c:choose>

                <tr class="<c:out value="${rowCss}"/>">
                    <td>
                        <c:out value="${watchdog.clientId}"/>
                    </td>
                    <td>
                        <c:out value="${watchdog.notificationRecepient}"/>
                    </td>
                    <td title="<c:out value="${watchdog}"/>">
                        <c:out value="${statusMsg}"/>
                    </td>
                    <td>
                        <a href="edit/<c:out value="${watchdog.clientId}"/>" class="btn">edit</a>
                        <form action="delete/<c:out value="${watchdog.clientId}"/>" method="post">
                            <input type="submit" value="delete" class="btn btn-danger"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <hr/>

        <div class="row">
            <div class="span8">
            </div>
            <div class="span4">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Legend</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="success">
                            <td>keepalive ok</td>
                        </tr>
                        <tr class="warning">
                            <td>keepalive never checked/received</td>
                        </tr>
                        <tr class="error">
                            <td>keepalive overdue</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <a href="javascript:history.back();" class="btn btn-inverse">back</a>
    </div>
    </body>
</html>
