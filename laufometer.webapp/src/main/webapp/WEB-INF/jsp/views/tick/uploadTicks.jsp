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

            <form class="form-horizontal"  method="post" action="">
                <div class="control-group">
                    <label class="control-label" for="ticksText">paste ticks:</label>
                    <div class="controls">
                        <textarea id="ticksText" name="ticksText" rows="15" cols="26"></textarea>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="ticksFile" class="disabled">from tick log file:</label>
                    <div class="controls">
                        <input id="ticksFile" type="file" name="ticksFile" disabled="disabled"/>
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

        </div> <!-- container -->
    </body>
</html>
