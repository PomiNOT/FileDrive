<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form
    hx-target="this"
    hx-get="files?shared"
    hx-trigger="load, refresh"
    id="mainFileList"
    data-list
>
    <div class="content__loading">
        <div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
    </div>
</form>

<div id="toolbar" class="toolbar" hx-swap="outerHTML" hx-swap-oob="true">
    <div class="toolbar__buttons">
        <button id="undoShareButton" class="outline" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Unshare
        </button>
    </div>
</div>

<button hx-swap-oob="true" id="uploadButton" class="sidebar__upload" type="button" disabled>
    <ion-icon name="add-circle-outline"></ion-icon>
    Upload
</button>

<script src="scripts/dashboard/sharingScreen.js"></script>