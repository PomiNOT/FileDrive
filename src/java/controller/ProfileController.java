package controller;

import dao.LoginDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        UserAccount account = SessionUtils.getSessionUser(request);
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if (action != null && action.equals("delete")) {
            try {
                dao.deleteUser(account.getUsername());
                response.sendRedirect("login");
                return;
            } catch (SQLException e) {
                request.setAttribute("error", e.getMessage());
            }
        }
        
		request.getRequestDispatcher("/pages/ProfilePage.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home page";
    }
}
