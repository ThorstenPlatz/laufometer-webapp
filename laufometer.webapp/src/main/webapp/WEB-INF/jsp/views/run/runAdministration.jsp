<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="prefix" value="${pageContext.request.contextPath}"/>

<c:set var="datePattern" value="yyyy-MM-dd"/>
<c:set var="timePattern" value="HH:mm:ss"/>
<c:set var="dateTimePattern" value="yyyy-MM-dd HH:mm:ss"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${prefix}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen" type="text/css">


        <script type="text/javascript" src="${prefix}/lib/jquery/jquery.js"></script>
        <script type="text/javascript" src="${prefix}/lib/bootstrap.datepicker/js/bootstrap-datepicker.js"></script>

        <script type="text/javascript">
            $(function() {
                $('#from').datepicker();
                $('#to').datepicker();
            });
        </script>

        <title>Run Administration</title>
    </head>
    <body>
    <div class="container">
        <h1>Run Administration</h1>

        <h2>Delete Runs</h2>

        <c:choose>
            <c:when test="${empty oldestRun and empty newestRun}">
                <c:set var="availablity" value="No runs available."/>
            </c:when>
            <c:when test="${not empty oldestRun and not empty newestRun}">
                <jsp:useBean id="oldestRun" scope="request" type="de.tp82.laufometer.model.run.Run"/>
                <jsp:useBean id="newestRun" scope="request" type="de.tp82.laufometer.model.run.Run"/>

                <c:set var="availablity" value="Runs are available from ${oldestRun.begin} to ${newestRun.end}."/>
            </c:when>
        </c:choose>

        <p><c:out value="${availablity}"/></p>

        <form class="form-inline" action="delete" method="post">
            <label for="from">From </label>
            <input type="text" class="input-small" id="from" name="from" placeholder="from" data-date-format="dd.mm.yyyy">
            <label for="to"> to </label>
            <input type="text" class="input-small" id="to" name="to" placeholder="to" data-date-format="dd.mm.yyyy">

            <br/><br/>

            <a href="javascript:history.back();" class="btn btn-inverse">back</a>
            <input type="reset" class="btn btn-danger" value="reset"/>
            <input type="submit" class="btn btn-success" value="delete runs"/>
        </form>

    </div>
    </body>
</html>
