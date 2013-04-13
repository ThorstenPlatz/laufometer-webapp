<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="prefix" value="${pageContext.request.contextPath}"/>

<jsp:useBean id="watchdogs" scope="request" type="java.util.List"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${prefix}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <title>Watchdogs</title>
    </head>
    <body>
    <div class="container">
        <h1>Watchdog Status</h1>

        <p class="text-right"><c:out value="${fn:length(watchdogs)}"/> watchdogs</p>

        <table class="table table-striped">
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
                                <c:set var="statusStyle" value="label-success"/>
                                <c:set var="statusMsg" value="alive"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="statusStyle" value="label-important"/>
                                <c:set var="statusMsg" value="dead"/>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <c:set var="statusStyle" value="label-warning"/>
                        <c:set var="statusMsg" value="never checked"/>
                    </c:otherwise>
                </c:choose>

                <tr>
                    <td>
                        <c:out value="${watchdog.clientId}"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${watchdog.notificationEnabled}">
                                <abbr title="Notifications are enabled">
                                    <c:out value="${watchdog.notificationRecepient}"/>
                                </abbr>
                            </c:when>
                            <c:otherwise>
                                <abbr title="Notifications are disabled" class="muted">
                                    <c:out value="${watchdog.notificationRecepient}"/>
                                </abbr>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <abbr title="<c:out value="${watchdog}"/>">
                            <span class="label ${statusStyle}"><c:out value="${statusMsg}"/></span>
                        </abbr>
                    </td>
                    <td>
                        <div class="btn-group">
                            <form action="edit/<c:out value="${watchdog.clientId}"/>" method="get"
                                  class="form-inline" style="display:inline;">
                                <input type="submit" value="edit" class="btn btn-small btn-primary"/>
                            </form>

                            <form action="delete/<c:out value="${watchdog.clientId}"/>" method="post"
                                  class="form-inline" style="display:inline;">
                                <input type="submit" value="delete" class="btn btn-small btn-danger"/>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <a href="../" class="btn btn-inverse">main menu</a>
    </div>
    </body>
</html>
