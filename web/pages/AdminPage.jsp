<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html data-theme="dark">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Admin</title>
        <link rel="stylesheet" href="styles/pico.css">
        <style>
            body {
                background-image: url(images/login_illustration.jpg);
                background-position: center;
                background-size: contain;
            }

            main {
                height: 100vh;
                min-height: 100vh;
                backdrop-filter: blur(50px);
                background: rgba(0, 0, 0, 0.8);
            }

            main > .container > * {
                margin-bottom: 20px;
            }

            .stats-box, table {
                background: rgba(1, 1, 1, 0.6);
                border-radius: 5px;
                padding: 40px;
                border: 2px solid white;
            }

            .stats-box * {
                margin: 0;
            }

            header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 10px;
            }

            .header__title {
                margin: 0;
            }

            .header__link {
                padding: 10px 10px;
            }
        </style>
    </head>
    <body>
        <jsp:useBean id="sizeUtil" class="utils.SizeFormatUtil" />
        
        <main>
            <div class="container">
                <header>
                    <h2 class="header__title">Control Panel</h2>
                    <a role="button" class="header__link outline" href="dashboard">Go back</a>
                </header>

                <section>
                    <h4>Statistics</h4>
                    <div class="grid">
                        <div class="stats-box">
                            <p>Total users</p>
                            <h2>${users.size()}</h2>
                        </div>
                        <div class="stats-box">
                            <p>Total bytes stored</p>
                            <h2>${sizeUtil.formatBytes(total)}</h2>
                        </div>
                    </div>
                </section>

                <section>
                    <h4>Users Management</h4>
                    <table border="1">
                        <thead>
                            <tr>
                                <th scope="col">Username</th>
                                <th scope="col">Password</th>
                                <th scope="col">Role</th>
                                <th scope="col">Amount stored</th>
                                <th scope="col">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${users}" var="u">
                            <tr>
                                <td scope="row">${u.info().getUsername()}</td>
                                <td>******</td>
                                <td>${u.info().getRole()}</td>
                                <td>${sizeUtil.formatBytes(u.size())}</td>
                                <td class="grid">
                                    <a role="button" href="?action=edit&id=${u.info().getUsername()}">
                                        <ion-icon name="create-outline"></ion-icon>
                                    </a>
                                    <a role="button" href="?action=delete&id=${u.info().getUsername()}">
                                        <ion-icon name="trash-outline"></ion-icon>
                                    </a>
                                </td>
                            </tr>           
                            </c:forEach>

                        </tbody>
                    </table>
                </section>
            </div>
        </main>
                        
        <c:if test="${not empty param.action and param.action eq 'delete'}">
            <dialog open>
                <article>
                    <h3>Confirm your action!</h3>
                    <p>You will destroy all of this user's data</p>
                    <footer>
                        <form method="POST">
                            <a href="admin" style="width: 100%; margin-bottom: 15px;" role="button">Cancel</a>
                            <button type="submit" class="secondary">Confirm</button>
                        </form>
                    </footer>
                </article>
            </dialog>
        </c:if>
                        
                                                
        <c:if test="${not empty param.action and param.action eq 'edit'}">
            <dialog open>
                <article>
                    <h3>Edit ${param.id}'s password</h3>
                    <form method="POST">
                        <input type="password" name="password" placeholder="Enter new password...">
                        <a href="admin" style="width: 100%; margin-bottom: 15px;" role="button">Cancel</a>
                        <button type="submit" class="secondary">Confirm</button>
                    </form>
                </article>
            </dialog>
        </c:if>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </body>
</html>

