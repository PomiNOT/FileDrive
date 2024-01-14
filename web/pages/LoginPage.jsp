<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html data-theme="light">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Login</title>
		<link rel="stylesheet" href="styles/pico.css">
		<style>
			main {
				padding: 0 20px !important;
				display: grid;
				place-items: center;
				height: 100vh;
				transition: background 200ms, backdrop-filter 200ms;
			}

			html {
				background-color: #c8f7fd;
			}

			body {
				background-image: url(images/login_illustration.jpg);
				background-position: center;
				background-size: contain;
			}

			.form-container {
				padding: 20px 40px;
				border-radius: 10px;
				width: 100%;
				max-width: 600px;
				border: 2px gray solid;
				background: white;
				box-shadow: 10px 10px 71px 0px rgba(0,0,0,0.31);
				-webkit-box-shadow: 10px 10px 71px 0px rgba(0,0,0,0.31);
				-moz-box-shadow: 10px 10px 71px 0px rgba(0,0,0,0.31);
				transition: opacity 300ms;
			}

			.form-container.invisible {
				opacity: 0;
				pointer-events: none;
			}

			.form-container form {
				margin: 0;
				transition: opacity 300ms;
			}

			main:has(input:focus) {
				background: rgba(0, 0, 0, 0.5);
				backdrop-filter: blur(3px);
			}

			.hidden {
				display: none;
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
