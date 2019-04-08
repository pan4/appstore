<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<a href="<c:url value="/home"/>">home</a>
<sec:authorize access="hasRole('DEVELOPER')">
    <a href="<c:url value="/new"/>">new</a>
</sec:authorize>
<span style="float: right">Logged <strong>${loggedinuser}</strong> <a href="<c:url value="/logout" />"> Logout</a></span>

</br>
</br>
<tr>
    <c:forEach var="app" items="${popular}">
        <td>
            <c:choose>
                <c:when test="${not empty app.smallIcon.icon}">
                    <a href="<c:url value="/app/${app.id}"/>"><img alt="img" src="data:image/jpeg;base64,${app.smallIcon.icon}"/></a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="/app/${app.id}"/>"><img alt="img" src="data:image/jpeg;base64,${defaultIcons.smallIcon}"/></a>
                </c:otherwise>
            </c:choose>
        </td>
    </c:forEach>
</tr>
</br>

