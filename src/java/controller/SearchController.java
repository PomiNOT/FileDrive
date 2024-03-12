package controller;

import dao.FileDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import model.UserAccount;

public class SearchController extends HttpServlet {
    FileDAO dao;

    @Override
    public void init() {
        dao = new FileDAO(getServletContext().getInitParameter("storagePath"));
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = (UserAccount)request.getAttribute("currentUser");

        String startsWith = request.getParameter("search");
        
        boolean isFolder = false;

        try {
            isFolder = Boolean.parseBoolean(request.getParameter("isFolder"));
        } catch (Exception e) {};

        int timeDeltaSecs = -1;

        try {
            timeDeltaSecs = Integer.parseInt(request.getParameter("time"));
        } catch (Exception e) {}

        Date before = null;

        if (timeDeltaSecs > 0) {
            before = new Date();
            before.setTime(before.getTime() - timeDeltaSecs * 1000);
        }

        String path = request.getParameter("path");

        try {
            var list = dao.getAllFilesMatching(
                    account.getUsername(),
                    path,
                    before,
                    isFolder,
                    startsWith
            );

            request.setAttribute("items", list);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        request.getRequestDispatcher("/components/dashboard/SearchFileList.jsp").forward(request, response);
    } 

    @Override
    public String getServletInfo() {
        return "Search function";
    }
}
