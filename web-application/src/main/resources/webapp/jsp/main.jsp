<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <h1>Compucar</h1>
    <div>
        <h2>Select an element to list</h2>
        <ul>
            <li><a href="/app/page/clients">Clients</a></li>
            <li><a href="/app/page/mechanics">Mechanics</a></li>
            <li><a href="/app/page/workshops">Workshops</a></li>
            <li><a href="/app/page/readers">Readers</a></li>
            <li><a href="/app/page/services">Services</a></li>
            <li><a href="/app/page/logs">Operation logs</a></li>
            <li>
                Reports
                <ul>
                    <li><a href="/app/page/usagereport">Usage reports</a></li>
                    <li><a href="/app/page/services-monthly">Services by month</a></li>
                    <li><a href="/app/page/services-dates-reader">Services between dates with reader</a></li>
                </ul>
            </li>
        </ul>
    </div>
</html>
