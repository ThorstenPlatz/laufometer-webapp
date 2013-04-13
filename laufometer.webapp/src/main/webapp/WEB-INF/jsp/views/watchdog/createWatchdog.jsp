<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="prefix" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${prefix}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <title>Create Watchdog</title>
    </head>
    <body>
    <div class="container">
        <h1>Create Watchdog</h1>

        <form class="form-horizontal" method="post" action="">
            <div class="control-group">
                <label class="control-label" for="clientId">ClientId</label>
                <div class="controls">
                    <input type="text" id="clientId" name="clientId"
                           placeholder="should not contain special characters">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="recipient">Notification Recipient</label>
                <div class="controls">
                    <input type="text" id="recipient" name="recipient"
                           placeholder="e-mail address for notifications">
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
