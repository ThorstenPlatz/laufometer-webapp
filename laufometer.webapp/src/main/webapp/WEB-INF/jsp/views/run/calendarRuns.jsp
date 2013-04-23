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
        <link href="${prefix}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">

        <link href='${prefix}/fullcalendar/fullcalendar.css' rel='stylesheet' type='text/css'/>
        <script type='text/javascript' src='${prefix}/jquery/jquery.js'></script>
        <script type='text/javascript' src='${prefix}/fullcalendar/fullcalendar.js'></script>

        <title>Run Calendar</title>

        <script type="text/javascript">
            var apiBasePath = "${prefix}/api/public/run/calendar/group";

            var eventSources = {
                monthSource: {
                    url: apiBasePath+'?groupInterval=86400',
                    type: 'GET',
                    cache: true,
                    error: function() { alert('something broke with monthSource...'); },
                    color: 'purple',
                    textColor: 'white',
                    className: 'monthSource'
                },
                weekSource: {
                    url: apiBasePath+'?groupInterval=7200',
                    type: 'GET',
                    cache: true,
                    error: function() { alert('something broke with weekSource...'); },
                    color: 'black',
                    textColor: 'white',
                    className: 'weekSource'
                },
                daySource:  {
                    url: apiBasePath+'?groupInterval=1800',
                    type: 'GET',
                    cache: true,
                    error: function() { alert('something broke with daySource...'); },
                    color: 'blue',
                    textColor: 'white',
                    className: 'daySource'
                }
            };

            var lastView = "";
            var usedEventSource = eventSources.monthSource;

            $(document).ready(function() {
                // page is now ready, initialize the calendar...

                $('#calendar').fullCalendar({
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'month,agendaWeek,agendaDay'
                    },
                    aspectRatio: 1.75,
                    // put your options and callbacks here
                    dayClick: function(date, allDay, jsEvent, view) {
                        if(view.name != 'month')
                            return;

                        $('#calendar').fullCalendar('changeView', 'agendaDay')
                                .fullCalendar('gotoDate', date);
                    },

                    firstDay: 1,
                    timeFormat: 'HH:mm:ss', // uppercase H for 24-hour clock
                    columnFormat: {
                        month: 'ddd',     // Mon
                        week: 'ddd d.M.', // Mon 31.12.
                        day: 'dddd d.M.'  // Monday 31.12.
                    },
                    titleFormat: {
                        month: 'MMMM yyyy',                             // September 2009
                        week: "MMM d[ yyyy]{ '&#8212;'[ MMM] d yyyy}", // Sep 7 - 13 2009
                        day: 'dddd, d. MMM, yyyy'                  // Tuesday, 8. Sep, 2009
                    },
                    axisFormat: "H(:mm)",

                    // event source
                    //events: '${prefix}/api/public/run/calendar'

                    eventSources: [ usedEventSource ],
                    lazyFetching: true,

                    viewDisplay: function(view) {
                        if(view.name != lastView) {
                            if (view.name == 'month'){
                                usedEventSource = eventSources.monthSource;
                            }
                            if (view.name == 'agendaWeek' || view.name == 'basicWeek'){
                                usedEventSource = eventSources.weekSource;
                            }
                            if (view.name == 'agendaDay' || view.name == 'basicDay'){
                                usedEventSource = eventSources.daySource;
                            }

                            $('#calendar')
                                .fullCalendar('removeEventSource', eventSources.monthSource)
                                .fullCalendar('removeEventSource', eventSources.weekSource)
                                .fullCalendar('removeEventSource', eventSources.daySource)
                                .fullCalendar('removeEvents')
                                .fullCalendar('addEventSource', usedEventSource)
                                .fullCalendar('refetchEvents');

                            lastView = view.name;
                        }
                    },
                    loading: function(bool) {
                        if (bool)
                            $('#loading').show();
                        else
                            $('#loading').hide();
                    }
                })
            });
        </script>
    </head>
    <body>
    <div class="container">
        <h1>Run Calendar <img src="${prefix}/img/loader.gif" id="loading" alt="loading"/></h1>

        <div id="calendar"></div>

    </div>
    </body>
</html>
