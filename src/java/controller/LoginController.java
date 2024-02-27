package controller;

import dao.LoginDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import model.UserAccount;

public class LoginController extends HttpServlet {
    public LoginDAO dao;

    @Override
    public void init() {
        dao = new LoginDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        
		request.getRequestDispatcher("/pages/LoginPage.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {
			case "login" -> loginHandler(request, response);
			case "signup" -> createAccountHandler(request, response);
		}
    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");

        try {
            UserAccount account = dao.getUserByUsername(username);

            if (account == null) {
                request.setAttribute("error", "This user does not exist");
            } else if (!account.getPassword().equals(password)) {
                request.setAttribute("error", "Password does not match!");
                request.setAttribute("username", username);
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", account);

                request.setAttribute("loggedIn", true);
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }

		request.getRequestDispatcher("/components/login/LoginForm.jsp").forward(request, response);
	}

    private void createAccountHandler(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		String firstName = (String)request.getParameter("firstName");
		String lastName = (String)request.getParameter("lastName");

        try {
            UserAccount account = dao.getUserByUsername(username);

            if (account != null) {
                request.setAttribute("error", username + " has already been taken!");
            } else {
                UserAccount newAccount = dao.createUser(new UserAccount(firstName, lastName, username, password));

                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", newAccount);
                
                request.setAttribute("loggedIn", true);
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }

		request.setAttribute("visible", true);
		request.getRequestDispatcher("/components/login/SignUpForm.jsp").forward(request, response);
	}

    @Override
    public String getServletInfo() {
        return "Login page";
    }
}
