
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import dao.SharingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ShareItem;
import model.UserAccount;
import utils.SessionUtils;

public class SharedViewController extends HttpServlet {
    private SharingDAO dao;

    @Override
    public void init() {
        dao = new SharingDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = SessionUtils.getSessionUser(request);

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ShareItem item = dao.getSharedFileById(id);

            boolean matchSharedTo = account != null &&
                    item.shareInfo().getSharedTo().equals(account.getUsername());

            boolean fromSelf = account == null ? false : account.getUsername().equals(item.shareInfo().getSharedBy());            

            if (item.shareInfo().isPublic() || matchSharedTo || fromSelf) {
                request.setAttribute("item", item);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        request.getRequestDispatcher("/pages/SharedFilePage.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
