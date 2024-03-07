<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.ShareItem" %>
<jsp:useBean id="dateUtil" class="utils.DateUtil" />

<div class="content__table">
    <div class="content__table-header">
        <p></p>
        <p>Name</p>
        <p>Type</p>
        <p>Shared To</p>
        <p></p>
        <p></p>
    </div>

    <c:if test="${not empty items}">
        <ul class="content__table-rows">
            <c:forEach items="${items}" var="f">
                <li hx-disable class="content__table-item" id="${f.item().getFileId()}">
                    <input type="checkbox" name="selected" value="${f.item().getFileId()}">
                    <p>${f.item().getFileName()}</p>
                    <p class="content__table-item-secondary">File</p>
                    <p class="content__table-item-secondary">
                        ${f.shareInfo().isPublic() ? "Public" : f.shareInfo().getSharedTo()}
                    </p>
                    <p></p>
                    <button type="button" title="Copy share link to clipboard" onclick="sharing.copy('${f.item().getFileId()}')">
                        <ion-icon name="copy-outline"></ion-icon>
                    </button>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${empty items}">
        <div class="content__table-empty">
            <h1>Empty</h1>
        </div>
    </c:if>
    
</div>