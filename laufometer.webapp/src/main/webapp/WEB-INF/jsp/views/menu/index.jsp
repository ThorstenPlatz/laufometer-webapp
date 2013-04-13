<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>laufometer</title>
    <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
<div class="container">
    <h1>Laufometer</h1>
    <h2>Menu</h2>
    <ul>
        <li>
            <h3>ticks</h3>
            <ul>
                <li><a href="/web/tick">list all ticks</a></li>
                <li><a href="/web/tick/upload">upload ticks</a></li>
            </ul>
        </li>
        <li>
            <h3>watchdogs</h3>
            <ul>
                <li><a href="/web/watchdog/">list watchdogs</a></li>
                <li><a href="/web/watchdog/create">create watchdog</a></li>
                <li><a href="/web/watchdog/delete">delete watchdog</a></li>
            </ul>
        </li>
        <li>
            <hr/>
            <ul>
                <li><a href="/about.html">about</a></li>
            </ul>
        </li>
    </ul>
</div> <!-- container -->
</body>
</html>
