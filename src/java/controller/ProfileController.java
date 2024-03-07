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
import utils.SessionUtils;

public class ProfileController extends HttpServlet {
    public LoginDAO dao;

    @Override
    public void init() {
        dao = new LoginDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        if (SessionUtils.getSessionUser(request) == null) {
            response.sendRedirect("login");
            return;
        }

		request.getRequestDispatcher("/pages/ProfilePage.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = (UserAccount)request.getAttribute("currentUser");

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "delete" -> {
                    dao.deleteUser(account.getUsername());
                    response.sendRedirect("login");
                }
                case "updateUser" -> {
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String password = request.getParameter("password");
                    String oldPassword = request.getParameter("oldPassword");

                    if (password == null) {
                        password = account.getPassword();
                    } else {
                        if (oldPassword == null || !oldPassword.equals(account.getPassword())) {
                            request.setAttribute("message", "Old password does not match");
                            break;
                        }
                    }

                    UserAccount newAccount = new UserAccount(
                        firstName,
                        lastName,
                        account.getUsername(),
                        password
                    );

                    dao.updateUser(newAccount);

                    HttpSession session = request.getSession(true);
                    session.setAttribute("currentUser", newAccount);

                    request.setAttribute("message", "Updated user successfully");
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }
        
		request.getRequestDispatcher("/pages/ProfilePage.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home page";
    }
}
