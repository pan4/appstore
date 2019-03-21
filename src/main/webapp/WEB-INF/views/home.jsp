<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>appstore</title>
</head>

<body>
    <a href="category-1">home</a>
    <a href="new">new</a>
    <br/>
    <br/>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <th>Category</th>
        </tr>
        <c:forEach var="category" items="${categories}">
            <tr>
                <td><a href="category-${category.id}">${category.type}</a></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <c:if test="${not empty apps}">
        <table border="1" cellpadding="5" cellspacing="5">
            <tr>
                <td>App name</td>
            </tr>
            <c:forEach var="app" items="${apps}">
                <tr>
                    <td>${app.name}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>