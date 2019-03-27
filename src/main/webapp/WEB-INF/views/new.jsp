<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>appstore</title>
</head>

<body>
    <%@include file="header.jsp" %>
    <h2>New application</h2>
    <form:form method="POST" modelAttribute="app" class="form-horizontal" enctype="multipart/form-data">
        <form:input type="text" path="name" id="name" size="30" placeholder="Application name" />
        <p><textarea rows="5" cols="45" name="description"></textarea></p>
        <form:select path="category" items="${categories}" itemValue="id" itemLabel="type" class="form-control input-sm" />
        <%--<select name="category">--%>
            <%--<option value="${selected}" selected>${selected}</option>--%>
            <%--<c:forEach items="${categories}" var="category">--%>
                <%--<c:if test="${category.type != selected}">--%>
                    <%--<option value="${category.type}">${category.type}</option>--%>
                <%--</c:if>--%>
            <%--</c:forEach>--%>
        <%--</select>--%>
        <p><input type="file" name="file" /></p>
        <input type="submit" value="Save" />
        <a href="category-1?page=1"> Cancel </a>
    </form:form>
</body>