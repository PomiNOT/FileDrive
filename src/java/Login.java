import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/login/*"})
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		request.getRequestDispatcher("/pages/LoginPage.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String path = request.getPathInfo();
		if (path == null || path.length() < 1) return;

		String action = path.substring(1);

		switch (action) {
			case "verify":
				verifyHandler(request, response);
				break;
			case "signup":
				createAccountHandler(request, response);
				break;
		}
    }

    private void verifyHandler(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");

		if (username.equals("abcdef") && password.equals("abcdef")) {
			request.setAttribute("goToHome", true);
		} else {
			request.setAttribute("passwordError", "Password is incorrect");
		}

		request.getRequestDispatcher("/components/login/LoginForm.jsp").forward(request, response);
	}

    private void createAccountHandler(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String username = (String)request.getParameter("username");
		if (username.equals("abcdef")) {
			request.setAttribute("usernameError", username + " has already been taken!");
		} else {
			request.setAttribute("goToHome", true);
		}

		request.setAttribute("visible", true);
		request.getRequestDispatcher("/components/login/SignUpForm.jsp").forward(request, response);
	}

    @Override
    public String getServletInfo() {
        return "Login page";
    }
}
