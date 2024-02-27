<%@page contentType="text/html" pageEncoding="UTF-8"%>
<form id="signUpForm" class="${visible ? '' : 'hidden'}" hx-ext="morph" hx-post="?action=signup" hx-swap="morph:outerHTML">
	<h2 style="margin: 0;">Sign Up</h2>
	<p>Enter your personal information</p>
	<div class="grid">
		<input type="text" name="firstName" placeholder="First Name" value="${firstName}" required>
		<input type="text" name="lastName" placeholder="Last Name" value="${lastName}" required>
	</div>
	<input type="text" name="username" placeholder="Username" value="${username}" required>
    <c:if test="${error}">
        <small style="color: red;">${error}</small>
    </c:if>
	<input type="password" name="password" placeholder="Password" value="${password}" required>

	<div class="grid">
        <c:choose>
            <c:when test="${loggedIn}">
                <button type="submit" disabled>Succeeded</button>
            </c:when>
            <c:otherwise>
                <button type="button" class="outline" onclick="transitionWrap(login)">Back</button>
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
