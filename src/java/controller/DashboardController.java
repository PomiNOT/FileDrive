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
        String part = request.getParameter("part");

        if (part != null) {
            switch (part) {
                case "myFiles" -> {
                    request.getRequestDispatcher("/components/dashboard/MyFilesScreen.jsp").forward(request, response);
                }
                case "trashed" -> {
                    request.getRequestDispatcher("/components/dashboard/TrashedScreen.jsp").forward(request, response);
                }
                case "shared" -> {
                    request.getRequestDispatcher("/components/dashboard/SharedFiles.jsp").forward(request, response);
                }
            }
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
