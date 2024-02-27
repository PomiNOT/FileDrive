<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html data-theme="dark">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Home</title>
		<link rel="stylesheet" href="styles/pico.css">
		<link rel="stylesheet" href="styles/dashboard.css">
		<link rel="stylesheet" href="styles/spinner.css">
        <style>
            body {
                background-image: url(images/login_illustration.jpg);
                background-position: center;
                background-size: contain;
            }
        </style>
    </head>
    <body>
        <main class="main-container">
            <%@include file="../components/dashboard/SideBar.jsp" %>
            <%@include file="../components/dashboard/HeaderBar.jsp" %>
            <%@include file="../components/dashboard/Toolbar.jsp" %>
            <%@include file="../components/dashboard/MainContent.jsp" %>
		</main>

        <upload-tracker></upload-tracker>

        <dialog id="createFolderDialog">
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

        <dialog id="moveFilesDialog">
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

        <script src="scripts/dashboard/main.js"></script>
        <script src="scripts/dashboard/uploadProgressTracker.js"></script>
		<script src="scripts/htmx.min.js"></script>
		<script src="scripts/idiomorph-ext.min.js"></script>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </body>
</html>
