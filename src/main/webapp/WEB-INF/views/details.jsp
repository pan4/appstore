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
<h2>${app.name}</h2>
<tr>
    <td>
        <c:if test="${not empty app.bigIcon}">
            <img alt="img" src="data:image/jpeg;base64,${app.bigIcon.icon}"/>
        </c:if>
    </td>
    <td>${app.description}</td>
</tr>
<p><a href="download/${app.packageName}?id=${app.id}">Download</a></p>
</body>