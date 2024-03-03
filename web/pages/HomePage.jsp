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
            <div id="toolbar" class="toolbar"></div>
            <div id="content" class="content"></div>
            <%@include file="../components/dashboard/SideBar.jsp" %>
            <%@include file="../components/dashboard/HeaderBar.jsp" %>
		</main>

        <upload-tracker></upload-tracker>

        <dialog id="createFolderDialog">
        </dialog>

        <dialog id="moveFilesDialog">
        </dialog>

        <dialog id="shareFileDialog">
        </dialog>

        <script src="scripts/dashboard/uploadProgressTracker.js"></script>
		<script src="scripts/htmx.min.js"></script>
		<script src="scripts/idiomorph-ext.min.js"></script>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </body>
</html>
