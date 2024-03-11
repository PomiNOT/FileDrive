<%@page contentType="text/html" pageEncoding="UTF-8"%>

<form
    data-list
    hx-target="this"
    hx-get="files"
    hx-trigger="load, refresh"
    id="mainFileList"
>
    <input type="hidden" name="path" value="-2" />
    <div class="content__loading">
        <div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
    </div>
</form>

<div id="toolbar" class="toolbar" hx-swap="outerHTML" hx-swap-oob="true">
    <div class="toolbar__buttons">
        <button id="undoTrashButton" class="outline" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Undo Trash
        </button>
        <button id="deleteItemsButton" class="outline" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Delete Items
        </button>
    </div>

    <form method="GET" hx-get="files" hx-target="#mainFileList" hx-swap="innerHTML" hx-trigger="change" class="toolbar__reload">
        <input id="toolbarPath" name="path" type="hidden" value="-2">

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
</dialog>

<dialog id="moveFilesDialog" hx-swap-oob="true">
</dialog>

<button hx-swap-oob="true" id="uploadButton" class="sidebar__upload" type="button" disabled>
    <ion-icon name="add-circle-outline"></ion-icon>
    Upload
</button>

<script src="scripts/dashboard/trashedScreen.js"></script>