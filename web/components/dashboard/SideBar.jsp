<%@page contentType="text/html" pageEncoding="UTF-8"%>

<aside class="sidebar">
    <button id="uploadButton">
        Loading...
    </button>
    <div>
        <button type="button" hx-trigger="load, click" hx-get="dashboard?part=myFiles" hx-target=".content" class="outline sidebar__nav-buttons">
            <ion-icon name="home-outline"></ion-icon>
            My Files
        </button>
        <button type="button" hx-get="dashboard?part=shared" hx-target=".content" class="outline sidebar__nav-buttons">
            <ion-icon name="share-social-outline"></ion-icon>
            Shared
        </button>
        <button type="button" hx-get="dashboard?part=trashed" hx-target=".content" class="outline sidebar__nav-buttons">
            <ion-icon name="trash-outline"></ion-icon>
            Trash
        </button>
    </div>

</aside>