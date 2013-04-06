<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Ticks</title>
        <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    </head>
    <body>
        <div class="container">
            <h1>Upload Ticks</h1>

            <form action="/api/rest/import/ticks" method="post">
                <fieldset>
                    <label for="ticks">ticks:</label><br/>
                    <textarea id="ticks" name="ticks" rows="15" cols="26"></textarea>
                    <br/>
                    <input type="submit" class="btn btn-success"> <input type="reset" class="btn btn-danger">
                </fieldset>
            </form>
        </div> <!-- container -->
    </body>
</html>
