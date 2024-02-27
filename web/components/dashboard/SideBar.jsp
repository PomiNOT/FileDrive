<%@page contentType="text/html" pageEncoding="UTF-8"%>

<aside class="sidebar">
    <button onclick="chooseAndUploadFile(event)" class="sidebar__upload" type="button">
        <ion-icon name="add-circle-outline"></ion-icon>
        Upload
    </button>
    <div>
        <button type="button" onclick="goHome()" class="outline sidebar__nav-buttons">
            <ion-icon name="home-outline"></ion-icon>
            My Files
        </button>
        <button type="button" class="outline sidebar__nav-buttons">
            <ion-icon name="share-social-outline"></ion-icon>
            Shared
        </button>
        <button type="button" class="outline sidebar__nav-buttons">
            <ion-icon name="trash-outline"></ion-icon>
            Trash
        </button>
    </div>

    <input id="fileUploadInput" type="file" style="display: none;"/>
</aside>