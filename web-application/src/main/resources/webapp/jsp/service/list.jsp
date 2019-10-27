<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <h1>Compucar</h1>
    <div>
        <h2>Services</h2>
        <table border="1">
            <tr>
                <th>Id</th>
                <th>Code</th>
                <th>Date</th>
                <th>Service time</th>
                <th>Cost</th>
                <th>Client</th>
                <th>Mechanic</th>
                <th>Reader</th>
                <th>Workshop</th>
            </tr>
            <c:forEach var="service" items="${services}">
            <tr>
                <td>${service.id}</td>
                <td>${service.code}</td>
                <td>${service.date}</td>
                <td>${service.serviceTime}</td>
                <td>${service.cost}</td>
                <td>${service.client.number}</td>
                <td>${service.mechanic.number}</td>
                <td>${service.reader.code}</td>
                <td>${service.workshop.code}</td>
            </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>