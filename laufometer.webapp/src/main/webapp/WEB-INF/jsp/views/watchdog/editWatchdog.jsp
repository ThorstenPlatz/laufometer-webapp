<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="prefix" value="${pageContext.request.contextPath}"/>

<jsp:useBean id="watchdog" scope="request" type="de.tp82.laufometer.model.watchdog.Watchdog"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${prefix}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <title>Edit Watchdog</title>
    </head>
    <body>
    <div class="container">
        <h1>Edit Watchdog</h1>

        <form class="form-horizontal"  method="post" action="">
            <div class="control-group">
                <label class="control-label" for="clientId">ClientId</label>
                <div class="controls">
                    <input type="text" readonly="readonly" id="clientId" name="clientId"
                           class="uneditable-input" value="<c:out value="${watchdog.clientId}"/>">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="recipient">Notification Recipient</label>
                <div class="controls">
                    <input type="text" id="recipient" name="recipient"
                           placeholder="multiple e-mail addresses must be separated by semicolons"
                           value="<c:out value="${watchdog.notificationRecepient}"/>">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <label class="checkbox" for="notificationEnabled">
                        <input type="checkbox" id="notificationEnabled" name="notificationEnabled" value="true"
                               <c:if test="${watchdog.notificationEnabled}">
                                 checked="checked"
                               </c:if>
                                />
                        Notifications Enabled
                    </label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <a href="javascript:history.back();" class="btn btn-inverse">back</a>
                    <input type="reset" class="btn btn-danger"/>
                    <input type="submit" class="btn btn-success"/>
                </div>
            </div>
        </form>

    </div>
    </body>
</html>
