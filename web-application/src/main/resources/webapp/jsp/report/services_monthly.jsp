<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <style>
        h2 {
            margin: 5px 0;
        }
        div {
            margin-bottom: 25px;
        }
    </style>
    <h1>Compucar</h1>
    <h2>Services per month</h2>
    <form action="/app/page/services-monthly" method="GET">
        <select id="month" name="month">
            <option value=1>Enero</option>
            <option value=2>Febrero</option>
            <option value=3>Marzo</option>
            <option value=4>Abril</option>
            <option value=5>Mayo</option>
            <option value=6>Junio</option>
            <option value=7>Julio</option>
            <option value=8>Agosto</option>
            <option value=9>Setiembre</option>
            <option value=10>Octubre</option>
            <option value=11>Noviembre</option>
            <option value=12>Diciembre</option>
        </select>
        <input type="submit" value"Get" />
    </form>
        <c:if test="${not empty services}">
        <div>
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
                    <th>Events</th>
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
                    <td>
                    <c:forEach var="diagnose" items="${service.diagnoses}">
                        <p>
                            Event: ${diagnose.eventName}<br>
                            Diagnose: ${diagnose.result}
                        </p>
                    </c:forEach>
                    </td>
                </tr>
                </c:forEach>
            </table>
            <p>
                <a href="">Download as pdf</a>
            </p>
        </div>
    </c:if>
    <c:if test="${empty services}">
        <div>
            <h2>No services found</h2>
        </div>
    </c:if>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>
