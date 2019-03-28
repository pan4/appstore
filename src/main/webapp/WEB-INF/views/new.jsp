<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>appstore</title>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
    <%@include file="header.jsp" %>
    <h2>New application</h2>
    <form:form method="POST" modelAttribute="app" class="form-horizontal" enctype="multipart/form-data">
        <form:input type="text" path="name" id="name" size="30" placeholder="Application name" />
        <div class="has-error">
            <form:errors path="name" class="help-inline"/>
        </div>
        <p><textarea rows="5" cols="45" name="description"></textarea></p>
        <form:select path="category" items="${categories}" itemValue="id" itemLabel="type" class="form-control input-sm" />
        <p><input type="file" name="file" /></p>
        <div class="has-error">
            <form:errors path="appPackage" class="help-inline"/>
        </div>
        <input type="submit" value="Save" />
        <a href="home"> Cancel </a>
    </form:form>
</body>