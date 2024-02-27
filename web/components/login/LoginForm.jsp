<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<form id="signInForm" hx-post="?action=login" hx-ext="morph" hx-swap="morph:outerHTML">
	<h2 style="margin: 0;">Login</h2>
	<p>To continue to your Drive</p>
	<label for="username">Username</label>
	<input type="text" name="username" value="${username}" required>
	<label for="password">Password</label>
	<input id="password" type="password" name="password" required>
    <c:if test="${not empty error}">
        <small style="color: red;">${error}</small>
    </c:if>
	<div class="grid">
    <c:choose>
        <c:when test="${loggedIn}">
            <button type="submit" disabled>Succeeded</button>
        </c:when>
        <c:otherwise>
            <button type="button" class="outline" onclick="transitionWrap(signUp)">Sign up</button>
            <button type="submit">Continue</button>
        </c:otherwise>
    </c:choose>
	</div>
    <c:if test="${loggedIn}">
	<script>
		const formContainer = document.querySelector(".form-container");
		setTimeout(() => formContainer.classList.add("invisible"), 500);
		setTimeout(() => window.location.replace('dashboard'), 1000);
	</script>
    </c:if>
</form>