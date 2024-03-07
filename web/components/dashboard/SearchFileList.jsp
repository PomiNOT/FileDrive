<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.FileItem" %>

<ul class="search-list">
    <c:forEach items="${items}" var="f">
        <li>
            <ion-icon name="folder-outline"></ion-icon>
            <p>${f.getFileName()}</p>
        </li>
    </c:forEach>
</ul>