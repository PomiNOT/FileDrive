<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html data-theme="light">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Login</title>
		<link rel="stylesheet" href="styles/pico.css">
		<link rel="stylesheet" href="styles/login.css">
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
			<div class="form-container">
				<%@include file="../components/login/LoginForm.jsp" %>
				<%@include file="../components/login/SignUpForm.jsp" %>
			</div>
		</main>

		<script src="scripts/htmx.min.js"></script>
		<script src="scripts/idiomorph-ext.min.js"></script>
		<script>
			const signInForm = document.getElementById("signInForm");
			const signUpForm = document.getElementById("signUpForm");
			const container = document.getElementsByClassName("form-container")[0];

			async function transitionWrap(trigger) {
				const { width: width0, height: height0 } = container.getBoundingClientRect();

				const controller = trigger();

				await controller.next();
				await controller.next();

				const { width: width1, height: height1 } = container.getBoundingClientRect();

				const sx = width0 / width1;
				const sy = height0 / height1;

				container.style.transform = "scale(" + sx + ", " + sy + ")";

				requestAnimationFrame(() => {
					container.style.transition = "transform 300ms";
					container.style.transform = "scale(1)";
					setTimeout(() => {
						container.style.transition = "";
						controller.next();
					}, 300);
				}, 0);
			}

			async function wait(ms) {
				return new Promise((resolve, _) => setTimeout(resolve, ms));
			}

			async function* signUp() {
				signInForm.style.opacity = 0;
				signUpForm.style.opacity = 0;

				yield wait(300);
				signInForm.classList.add("hidden");
				signUpForm.classList.remove("hidden");

				yield;
				setTimeout(() => {
					signUpForm.style.opacity = 1;
				}, 10);
			}

			async function* login() {
				signInForm.style.opacity = 0;
				signUpForm.style.opacity = 0;

				yield wait(300);
				signUpForm.classList.add("hidden");
				signInForm.classList.remove("hidden");

				yield;
				setTimeout(() => {
					signInForm.style.opacity = 1;
				}, 10);
			}
		</script>
    </body>
</html>
