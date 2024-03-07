<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html data-theme="dark">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shared File</title>
		<link rel="stylesheet" href="styles/pico.css">
        <style>
            html {
                background-color: #c8f7fd;
            }

            body {
                display: grid;
                place-items: center;
                height: 100vh;
                background-image: url(images/login_illustration.jpg);
                background-position: center;
                background-size: contain;
            }
            
            .container {
                width: 500px;
                text-align: center;
                backdrop-filter: blur(20px);
                background: rgba(0, 0, 0, 0.5);
                padding: 30px;
                border-radius: 5px;
            }

            .container ion-icon {
                font-size: 128px;
            }
        </style>
    </head>
    <body>
        <main class="container">
            <c:if test="${not empty item}">
                <h2>Shared file</h2>
                <ion-icon name="document-outline"></ion-icon>
                <p>${item.item().getFileName()}</p>
                <button>Download</button>
            </c:if>
            <c:if test="${empty item}">
                <h2>This item is not shared with you or is non-existent</h2>
                <a role="button" href="dashboard">Back to dashboard</a>
            </c:if>
        </main>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </body>
</html>
