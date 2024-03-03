<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.FileItem" %>
<jsp:useBean id="dateUtil" class="utils.DateUtil" />

<div class="content__table">
    <c:if test="${not empty param.sortBy}">
        <input type="hidden" name="sortBy" value="${param.sortBy}">
    </c:if>
    <c:if test="${not empty param.order}">
        <input type="hidden" name="order" value="${param.order}">
    </c:if>
    <c:if test="${not empty param.trashed}">
        <input type="hidden" name="trashed" value="${param.trashed}">
    </c:if>
    <c:if test="${not empty param.path and empty param.doNotPreservePath}">
        <input type="hidden" name="path" value="${param.path}">
        <input id="toolbarPath" hx-swap-oob="true" type="hidden" name="path" value="${param.path}">
    </c:if>

    <div class="content__table-header">
        <p></p>
        <p>Name</p>
        <p>Type</p>
        <p>Updated</p>
        <p></p>
    </div>

    <c:if test="${not empty items}">
        <ul class="content__table-rows">
            <c:forEach items="${items}" var="f">
                <c:choose>
                    <c:when test="${f.isFolder()}">
                        <li
                            class="content__table-item"
                            hx-get="files"
                            hx-trigger="dblclick"
                            hx-vals='{
                            "filter": "${param.filter}",
                            "selectOne": "${param.selectOne}",
                            "path": "${f.getDescendantPath()}",
                            "sortBy": "${param.sortBy}",
                            "order": "${param.order}",
                            "trashed": "${param.trashed}",
                            "doNotPreservePath": "${param.doNotPreservePath}"
                            }'
                        >
                            <c:if test="${not empty param.selectOne}">
                                <input hx-disable type="radio" name="selected" value="${f.getFileId()}">
                            </c:if>
                            <c:if test="${empty param.selectOne}">
                                <input hx-disable type="checkbox" name="selected" value="${f.getFileId()}">
                            </c:if>
                            <p>${f.getFileName()}</p>
                            <p class="content__table-item-secondary">${f.isFolder() ? "Folder" : "File" }</p>
                            <p class="content__table-item-secondary">${dateUtil.getRelativeTime(f.getUpdated())}</p>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li hx-disable class="content__table-item" id="${f.getFileId()}">
                            <c:if test="${not empty param.selectOne}">
                                <input type="radio" name="selected" value="${f.getFileId()}">
                            </c:if>
                            <c:if test="${empty param.selectOne}">
                                <input type="checkbox" name="selected" value="${f.getFileId()}">
                            </c:if>
                            <p>${f.getFileName()}</p>
                            <p class="content__table-item-secondary">${f.isFolder() ? "Folder" : "File" }</p>
                            <p class="content__table-item-secondary">${dateUtil.getRelativeTime(f.getUpdated())}</p>
                            <button type="button" onclick="myFiles.share(${f.getFileId()})">
                                <ion-icon name="share-outline"></ion-icon>
                            </button>
                        </li>
                    </c:otherwise>
                </c:choose>
                
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${empty items}">
        <div class="content__table-empty">
            <h1>Empty</h1>
        </div>
    </c:if>
    
</div>