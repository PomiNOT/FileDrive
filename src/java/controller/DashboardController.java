package controller;



import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

public class DashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        if (SessionUtils.getSessionUser(request) == null) {
            response.sendRedirect("login");
            return;
        }

		request.getRequestDispatcher("/pages/HomePage.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		request.getRequestDispatcher("/pages/HomePage.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home page";
    }
}
