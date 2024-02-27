<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="header">
    <div class="header__logo">
        <ion-icon name="file-tray-stacked" size="large"></ion-icon>
        Files
    </div>
    <form>
        <input class="header__search" type="text" placeholder="🔍 Search" name="search">
    </form>
    <details style="margin-bottom: 0;" role="list">
        <summary aria-haspopup="listbox">
            <ion-icon name="person-circle-outline"></ion-icon>
            Account
        </summary>
        <ul role="listbox">
            <li><a href="profile">View Profile</a></li>
            <li><a style="color: red;" href="login?action=logout">Logout</a></li>
        </ul>
    </details>
</div>