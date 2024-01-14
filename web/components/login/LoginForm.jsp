<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
	Object passwordError = request.getAttribute("passwordError"); 
	Object usernameLO = request.getParameter("username");
	boolean goToHome = request.getAttribute("goToHome") != null ? true : false;
%>
<form id="signInForm" hx-post="login/verify" hx-ext="morph" hx-swap="morph:outerHTML">
	<h2 style="margin: 0;">Login</h2>
	<p>To continue to your Drive</p>
	<label for="username">Username</label>
	<input type="text" name="username" value="<%= usernameLO != null ? usernameLO : "" %>" required>
	<label for="password">Password</label>
	<input id="password" type="password" name="password" required>
	<% if (passwordError != null) { %>
	<small style="color: red;"><%= passwordError %></small>
	<% } %>
	<div class="grid">
	<% if (!goToHome) { %>
		<button type="button" class="outline" onclick="transitionWrap(signUp)">Sign up</button>
		<button type="submit">Continue</button>
	<% } else { %>
		<button type="submit" disabled>Succeeded</button>
	<% } %>
	</div>
	<% if (goToHome) { %>
	<script>
		const formContainer = document.querySelector(".form-container");
		setTimeout(() => formContainer.classList.add("invisible"), 500);
		setTimeout(() => window.location.replace('home'), 1000);
	</script>
	<% } %>
</form>