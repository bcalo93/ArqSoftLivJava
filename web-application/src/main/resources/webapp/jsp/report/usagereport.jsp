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
        <c:if test="${not empty usageReport.serviceSummary}">
        <div>
            <h2>Average Usage and Call Amount</h2>
            <table border="1">
                <tr>
                    <th>Service Name</th>
                    <th>Average Usage</th>
                    <th>Call Amount</th>
                </tr>
                <c:forEach var="summary" items="${usageReport.serviceSummary}">
                    <tr>
                        <td>${summary.serviceName}</td>
                        <td>${summary.average}</td>
                        <td>${summary.callCount}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div>
            <h2>Slowest and Fastest Service</h2>
            <table border=1>
                <tr>
                    <th></th>
                    <th>Service Name</th>
                    <th>Execution Time</th>
                </tr>
                <tr>
                    <th>Fastest Service</th>
                    <td>${usageReport.fastestService.serviceName}</td>
                    <td>${usageReport.fastestService.timeValue} ms</td>
                </tr>
                <tr>
                    <td>Slowest Service</td>
                    <td>${usageReport.slowestService.serviceName}</td>
                    <td>${usageReport.slowestService.timeValue} ms</td>
                </tr>
            </table>
        </div>
        <div>
            <h2>Most and Less used Service</h2>
            <table border=1>
                <tr>
                    <th></th>
                    <th>Service Name</th>
                    <th>Usage Amount</th>
                </tr>
                <tr>
                   <th>Most used Service</th>
                   <td>${usageReport.mostUsedService.serviceName}</td>
                   <td>${usageReport.mostUsedService.usageCount}</td>
                </tr>
                <tr>
                    <th>Less used Service</th>
                    <td>${usageReport.lessUsedService.serviceName}</td>
                    <td>${usageReport.lessUsedService.usageCount}</td>
                </tr>
            </table>
        </div>
    </c:if>
    <c:if test="${empty usageReport.serviceSummary}">
        <div>
            <h2>No execution logs found.</h2>
        </div>
    </c:if>
    <div>
        <a href="/app/page/main">Back to main</a>
    </div>
</html>
