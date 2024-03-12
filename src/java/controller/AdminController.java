package controller;

import dao.FileDAO;
import dao.LoginDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.UserAccount;

public class AdminController extends HttpServlet {

    private LoginDAO ldao;
    private FileDAO fdao;

    public static record UserInfoAndSize(UserAccount info, int size) {};
    
    @Override
    public void init() {
        ldao = new LoginDAO();
        fdao = new FileDAO(getServletContext().getInitParameter("storagePath"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            var users = ldao.getAll();

            List<UserInfoAndSize> usersWithSize = users.stream().map((acc) -> {
                try {
                    int size = fdao.getTotalBytesFor(acc.getUsername());
                    return new UserInfoAndSize(acc, size);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }).filter((item) -> item != null).toList();
            
            int total = 0;
            for (var item : usersWithSize) {
                total += item.size();
            }

            request.setAttribute("users", usersWithSize);
            request.setAttribute("total", total);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        request.getRequestDispatcher("/pages/AdminPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String userId = request.getParameter("id");

        try {
            switch (action) {
                case "delete" -> {
                    fdao.removeUserFiles(userId);
                    ldao.deleteUser(userId);
                }
                case "edit" -> {
                    var user = ldao.getUserByUsername(userId);
                    user.setPassword(request.getParameter("password"));

                    ldao.updateUser(user);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response.sendRedirect("admin");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
