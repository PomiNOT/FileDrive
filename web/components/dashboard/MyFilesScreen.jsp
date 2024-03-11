<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<form
    data-list
    hx-target="this"
<c:choose>
    <c:when test="${not empty param.path}">
    hx-get="files?path=${param.path}"
    </c:when>
    <c:otherwise>
    hx-get="files"
    </c:otherwise>
</c:choose>
    hx-trigger="load, refresh"
    id="mainFileList"
>
    <div class="content__loading">
        <div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
    </div>
</form>

<div id="toolbar" class="toolbar" hx-swap="outerHTML" hx-swap-oob="true">
    <div class="toolbar__buttons">
        <button class="outline" onclick="myFiles.createFolder()" type="button">
            <ion-icon name="add-circle-outline"></ion-icon>
            New Folder
        </button>
        <button id="moveButton" class="outline" onclick="myFiles.moveFiles()" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Move Files
        </button>
        <button id="markTrashButton" class="outline" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Mark Trash
        </button>
    </div>

    <form method="GET" hx-get="files" hx-target="#mainFileList" hx-swap="innerHTML" hx-trigger="change" class="toolbar__reload">
        <input id="toolbarPath" name="path" type="hidden" value="-1">

        <div>
            <span>
                Sort by
            </span>

            <select name="sortBy">
                <option value="name">Name</option>
                <option value="date">Date</option>
            </select>
        </div>

        <div>
            <span>
                Order
            </span>

            <select name="order">
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>
        </div>
    </form>
</div>

<dialog id="createFolderDialog" hx-swap-oob="true">
    <form method="dialog" style="margin: 0;">
        <label>
            Folder name
            <input type="text" name="folderName" required />
        </label>
        <div class="grid">
            <button type="submit">Create</button>
        </div>
    </form>
</dialog>

<dialog id="shareFileDialog" hx-swap-oob="true">
    <form hx-post="files" hx-swap="none" style="margin: 0;">
        <h3 style="text-align: center; border-bottom: gray 1px;">Share this file</h3>
        <input type="hidden" name="action" value="shareFile" />
        <input type="hidden" name="fileId" value="" />
        <label>
            Share mode
            <select name="public">
                <option value="true">Public</option>
                <option value="false">Specific user</option>
            </select>
        </label>
        <label>
            Share with username
            <input type="text" name="sharedTo">
        </label>
        <div class="grid">
            <button type="submit">Share</button>
        </div>
    </form>
</dialog>

<dialog id="moveFilesDialog" hx-swap-oob="true">
    <form data-list id="moveFilesForm" hx-get="files" hx-target="#folderView" hx-trigger="load, refresh" method="dialog" style="margin: 0;">
        <input type="hidden" name="filter" value="folder">
        <input type="hidden" name="selectOne" value="true">
        <input type="hidden" name="doNotPreservePath" value="true">
        <h3 style="text-align: center; border-bottom: gray 1px;">Select folder to move to</h3>
        <div style="background: rgba(50, 50, 50, 0.7); border-radius: 10px; padding: 10px; margin-bottom: 10px;" id="folderView">

        </div>
        <button type="submit">Move</button>
    </form>
</dialog>

<button hx-swap-oob="true" id="uploadButton" onclick="myFiles.chooseAndUploadFile(event)" class="sidebar__upload" type="button">
    <ion-icon name="add-circle-outline"></ion-icon>
    Upload
</button>

<input id="fileUploadInput" type="file" style="display: none;"/>

<script src="scripts/dashboard/myFilesScreen.js"></script>