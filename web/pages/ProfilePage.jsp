<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html data-theme="dark">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Account</title>
		<link rel="stylesheet" href="styles/pico.css">
		<link rel="stylesheet" href="styles/profile.css">
        <style>
            body {
                background-image: url(images/login_illustration.jpg);
                background-position: center;
                background-size: contain;
            }
        </style>
    </head>
    <body>
        <main>
            <section class="container">
                <header>
                    <h1 class="header__title">
                        <ion-icon style="position: relative; top: 5px;" name="person-circle-outline"></ion-icon>
                        Account
                    </h1>
                    <a role="button" class="header__link outline" href="dashboard">Go back</a>
                </header>
                <p>View your user account and update your information here</p>
                <c:set var="user" value="${sessionScope.currentUser}"></c:set>
                <form>
                    <div class="grid">
                        <input type="text" name="firstName" placeholder="First Name" value="${user.getFirstName()}" required>
                        <input type="text" name="lastName" placeholder="Last Name" value="${user.getLastName()}" required>
                    </div>
                    <input type="text" name="username" placeholder="Username" value="${user.getUsername()}" disabled>
                    <c:if test="${error}">
                        <small style="color: red;">${error}</small>
                    </c:if>
                    <input type="password" name="password" placeholder="Password" value="random crap" required>
                    <button type="submit">Edit</button>
                </form>

                <h2>Danger zone</h2>
                <a role="button" class="danger-button" href="?action=delete">Delete account</a>
            </section>
		</main>

        <c:if test="${not empty param.action and param.action eq 'delete'}">
            <dialog open>
                <article>
                    <h3>Confirm your action!</h3>
                    <p>You will not be able to access your content again</p>
                    <footer>
                        <form method="POST">
                            <a href="profile" style="width: 100%; margin-bottom: 15px;" role="button">Cancel</a>
                            <button type="submit" class="secondary">Confirm</button>
                        </form>
                    </footer>
                </article>
            </dialog>
        </c:if>

        <c:if test="${not empty error}">
            <dialog open>
                <article>
                    <h3>Error!</h3>
                    <p>${error}</p>
                    <footer>
                        <a href="profile" role="button">OK</a>
                    </footer>
                </article>
            </dialog>
        </c:if>

        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </body>
</html>

