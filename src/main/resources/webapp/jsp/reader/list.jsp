<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <h1>Compucar</h1>
    <div>
        <h2>Readers</h2>
        <table border="1">
            <tr>
                <th>Id</th>
                <th>Code</th>
                <th>Brand</th>
                <th>Actual time use</th>
                <th>Battery life</th>
                <th>Workshop</th>
            </tr>
            <c:forEach var="reader" items="${readers}">
            <tr>
                <td>${reader.id}</td>
                <td>${reader.code}</td>
                <td>${reader.brand}</td>
                <td>${reader.actualTimeUse}</td>
                <td>${reader.batteryLife}</td>
                <td>${reader.workshop.code}</td>
            </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>
