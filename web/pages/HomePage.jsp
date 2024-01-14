<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html data-theme="dark">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FileDrive - Home</title>
		<link rel="stylesheet" href="styles/pico.css">
		<style>
			html {
				background-color: #c8f7fd;
			}

			body {
				background-image: url(images/login_illustration.jpg);
				background-position: center;
				background-size: contain;
			}

			main {
				animation: blur 1s forwards;
				height: 100vh;
				display: grid;
				place-items: center;
			}

			.fade {
				animation: fade 1s forwards;
			}

			@keyframes blur {
				to {
					background: rgba(0, 0, 0, 0.8);
					backdrop-filter: blur(100px);
				}
			}

			@keyframes fade {
				from {
					opacity: 0;
				}
			}
		</style>
    </head>
    <body>
        <main>
			<div class="fade" style="text-align: center;">
				<h3 style="margin-bottom: 0;">Welcome</h3>
				<p>Drag your files here to upload</p>
			</div>
		</main>
    </body>
</html>
