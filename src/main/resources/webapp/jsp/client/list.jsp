<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <div>
        <h2>Clients</h2>
        <table border="1">
            <tr>
            <th>Id</th>
            <th>Number</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Type</th>
            </tr>
                <c:forEach var="client" items="${clients}">
                <tr>
                    <td>${client.id}
                    <td>${client.number}</td>
                    <td>${client.name}</td>
                    <td>${client.phone}</td>
                    <td>${client.email}</td>
                    <td>${client.type}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</html>
