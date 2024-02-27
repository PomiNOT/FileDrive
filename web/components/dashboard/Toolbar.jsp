<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="toolbar">
    <div class="toolbar__buttons">
        <button class="outline" onclick="createFolder()" type="button">
            <ion-icon name="add-circle-outline"></ion-icon>
            New Folder
        </button>
        <button id="moveButton" class="outline" onclick="moveFiles()" type="button" disabled>
            <ion-icon name="add-circle-outline"></ion-icon>
            Move Files
        </button>
    </div>

    <form method="GET" hx-get="files" hx-target=".content" hx-trigger="change" class="toolbar__reload">
        <input id="toolbarPath" name="path" type="hidden" value="root">
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