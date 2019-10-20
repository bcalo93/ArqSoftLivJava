<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <div>
        <h2>Mechanics</h2>
        <table border="1">
            <tr>
            <th>Id</th>
            <th>Number</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Start Date</th>
            </tr>
                <c:forEach var="mechanic" items="${mechanics}">
                <tr>
                    <td>${mechanic.id}
                    <td>${mechanic.number}</td>
                    <td>${mechanic.name}</td>
                    <td>${mechanic.phone}</td>
                    <td><fmt:formatDate value="${mechanic.startDate}" pattern="dd/MM/yyyy" /></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</html>
