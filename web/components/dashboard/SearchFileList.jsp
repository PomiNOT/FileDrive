<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.FileItem" %>

<ul class="search-list">
    <c:forEach items="${items}" var="f">
        <li onclick="dismissSearch()" hx-get="dashboard?part=myFiles&path=${f.getPath()}" hx-target=".content">
            <c:if test="${f.isFolder()}">
                <ion-icon name="folder-outline"></ion-icon>
            </c:if>
            <c:if test="${!f.isFolder()}">
                <ion-icon name="document-outline"></ion-icon>
            </c:if>
            <p>${f.getFileName()}</p>
        </li>
    </c:forEach>
</ul>