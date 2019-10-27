<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <h1>Compucar</h1>
    <div>
        <h2>Operation Logs</h2>
        <table border="1">
            <tr>
                <th>Id</th>
                <th>Username</th>
                <th>Service Name</th>
                <th>Register Date</th>
            </tr>
                <c:forEach var="operationLog" items="${operationLogs}">
                <tr>
                    <td>${operationLog.id}</td>
                    <td>${operationLog.username}</td>
                    <td>${operationLog.serviceName}</td>
                    <td><fmt:formatDate value="${operationLog.registerDate}" pattern="dd/MM/yyyy hh:mm:ss" /></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>
