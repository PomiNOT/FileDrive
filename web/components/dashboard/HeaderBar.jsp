<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserRole" %>

<div class="header">
    <div class="header__logo">
        <ion-icon name="file-tray-stacked" size="large"></ion-icon>
        Files
    </div>
    <form class="header__search" hx-get="search" hx-trigger="input" hx-target="#search-results">
        <input class="header__search-input" type="text" placeholder="🔍 Search" name="search" oninput="toggleSearchBar(event)">
        <div class="header__search-results">
            <div id="search-results">
                <%@include file="SearchFileList.jsp" %>
            </div>
            <div>
                <div class="header__search-filter">
                    <small>Type</small>
                    <div>
                        <label>
                            File
                            <input type="radio" name="isFolder" value="false" checked="true">
                        </label>
                        <label>
                            Folder
                            <input type="radio" name="isFolder" value="true">
                        </label>
                    </div>
                </div>
                <div class="header__search-filter">
                    <small>Time frame</small>
                    <div>
                        <label>
                            All time
                            <input checked="true" type="radio" name="time" value="-1">
                        </label>
                        <label>
                            Past day
                            <input type="radio" name="time" value="86400">
                        </label>
                        <label>
                            Past week
                            <input type="radio" name="time" value="604800">
                        </label>
                        <label>
                            Past month
                            <input type="radio" name="time" value="2419200">
                        </label>
                        <label>
                            Past year
                            <input type="radio" name="time" value="29030400">
                        </label>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <details style="margin-bottom: 0;" role="list">
        <summary aria-haspopup="listbox">
            <ion-icon name="person-circle-outline"></ion-icon>
            Account
        </summary>
        <ul role="listbox">
            <li><a href="profile">View Profile</a></li>
            <c:if test="${currentUser.getRole() == UserRole.ADMIN}">
            <li><a href="admin">Control Panel</a></li>
            </c:if>
            <li><a style="color: red;" href="login?action=logout">Logout</a></li>
        </ul>
    </details>
</div>

<script>
    const input = document.querySelector('.header__search-input');
    const searchResults = document.querySelector('.header__search-results');

    function toggleSearchBar(e) {
        if (e.target.value) {
            searchResults.style.display = 'block';
        } else {
            searchResults.style.display = 'none';
        }
    }

    function dismissSearch() {
        input.value = '';
        searchResults.style.display = 'none';
    }
</script>