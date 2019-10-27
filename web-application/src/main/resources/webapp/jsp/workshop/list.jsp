<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <h1>Compucar</h1>
    <div>
        <h2>Workshops</h2>
        <table border="1">
            <tr>
                <th>Id</th>
                <th>Code</th>
                <th>Name</th>
                <th>Address</th>
                <th>City</th>
            </tr>
                <c:forEach var="workshop" items="${workshops}">
                <tr>
                    <td>${workshop.id}</td>
                    <td>${workshop.code}</td>
                    <td>${workshop.name}</td>
                    <td>${workshop.address}</td>
                    <td>${workshop.city}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>
