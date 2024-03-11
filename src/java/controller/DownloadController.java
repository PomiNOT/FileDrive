
package controller;

import dao.FileDAO;
import dao.SharingDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import model.ShareItem;
import model.UserAccount;
import model.FileItem;
import utils.FileUtils;
import utils.SessionUtils;

public class DownloadController extends HttpServlet {
    private SharingDAO sharingDAO;
    private FileDAO fileDAO;

    @Override
    public void init() {
        sharingDAO = new SharingDAO();
        fileDAO = new FileDAO();
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = SessionUtils.getSessionUser(request);

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            FileItem item = returnFilePathIfPermissionValid(id, account);

            if (item != null) {
                File file = new File(FileUtils.getFilePath(getServletContext(), item.getLocation()));

                FileInputStream stream = new FileInputStream(file);
                response.setContentType("application/octet-stream");
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + item.getFileName() + "\"");

                stream.transferTo(response.getOutputStream());
                stream.close();
            } else {
                response.sendError(403, "Access denied");
            }
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }

    private FileItem returnFilePathIfPermissionValid(int id, UserAccount account) throws SQLException {
        ShareItem shareItem = sharingDAO.getSharedFileById(id);
        FileItem fileItem = null;

        if (shareItem == null) {
            fileItem = fileDAO.getFileById(id);
        }

        if (shareItem != null) {
            if (account != null && shareItem.item().getOwner().equals(account.getUsername())) {
                return shareItem.item();
            }

            if (account != null && shareItem.shareInfo().getSharedTo().equals(account.getUsername())) {
                return shareItem.item();
            }

            if (shareItem.shareInfo().isPublic()) {
                return shareItem.item();
            }
        }

        if (fileItem != null) {
            if (account != null && fileItem.getOwner().equals(account.getUsername())) {
                return fileItem;
            }
        }

        return null;
    }

    @Override
    public String getServletInfo() {
        return "Download controller";
    }
}
