<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>appstore</title>
</head>

<body>
    <%@include file="header.jsp" %>
    <h2>${app.name}</h2>
    <tr>
        <td>
            <c:choose>
                <c:when test="${not empty app.bigIcon.icon}">
                    <img alt="img" src="data:image/jpeg;base64,${app.bigIcon.icon}"/>
                </c:when>
                <c:otherwise>
                    <img alt="img" src="data:image/jpeg;base64,${defaultIcons.bigIcon}"/>
                </c:otherwise>
            </c:choose>
        </td>
        <td>${app.description}</td>
    </tr>
    <p><a href="download/${app.packageName}?id=${app.id}">Download</a></p>
</body>