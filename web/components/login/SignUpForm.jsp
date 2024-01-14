<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
	Object usernameError = request.getAttribute("usernameError"); 
	boolean visible = request.getAttribute("visible") != null ? true : false;
	boolean goToHomeSU = request.getAttribute("goToHome") != null ? true : false;
	Object firstName = request.getParameter("firstName");
	Object lastName = request.getParameter("lastName");
	Object usernameSU = request.getParameter("username");
	Object password = request.getParameter("password");
%>
<form id="signUpForm" class="<%= visible ? "" : "hidden" %>" hx-ext="morph" hx-post="login/signup" hx-swap="morph:outerHTML">
	<h2 style="margin: 0;">Sign Up</h2>
	<p>Enter your personal information</p>
	<div class="grid">
		<input type="text" name="firstName" placeholder="First Name" value="<%= firstName != null ? firstName : "" %>" required>
		<input type="text" name="lastName" placeholder="Last Name" value="<%= lastName != null ? lastName : "" %>" required>
	</div>
	<input type="text" name="username" placeholder="Username" value="<%= usernameSU != null ? usernameSU : "" %>" required>
	<% if (usernameError != null) { %>
	<small style="color: red;"><%= usernameError %></small>
	<% } %>
	<input type="password" name="password" placeholder="Password" value="<%= password != null ? password : "" %>" required>
	<div class="grid">
		<% if (!goToHomeSU) { %>
			<button type="button" class="outline" onclick="transitionWrap(login)">Back</button>
			<button type="submit">Continue</button>
		<% } else { %>
			<button type="submit" disabled>Succeeded</button>
		<% } %>
	</div>

	<% if (goToHomeSU) { %>
	<script>
		const formContainer = document.querySelector(".form-container");
		setTimeout(() => formContainer.classList.add("invisible"), 500);
		setTimeout(() => window.location.replace('home'), 1000);
	</script>
	<% } %>
</form>
