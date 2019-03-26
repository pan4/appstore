<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>appstore</title>
</head>

<body>
    <a href="category-1?page=1">home</a>
    <a href="new">new</a>
    <br/>
    <br/>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <th>Category</th>
        </tr>
        <c:forEach var="category" items="${categories}">
            <tr>
                <td><a href="category-${category.id}?page=1">${category.type}</a></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <c:if test="${not empty apps}">
        <table border="1" cellpadding="5" cellspacing="5">
            <tr>
                <th>Icon</th>
                <th>Name</th>
            </tr>
            <c:forEach var="app" items="${apps}">
                <tr>
                    <td>
                        <c:if test="${not empty app.smallIcon}">
                            <img alt="img" src="data:image/jpeg;base64,${app.smallIcon.icon}"/>
                        </c:if>
                    </td>
                    <td>
                        <a href="app-${app.id}">${app.name}</a>
                        <p>Downloads: ${app.downloadsCount}</p>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${noOfPages > 0}">
        <table border="1" cellpadding="5" cellspacing="5">
            <tr>
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <td>${i}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="category-${categoryId}?page=${i}">${i}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>
    </c:if>
</body>