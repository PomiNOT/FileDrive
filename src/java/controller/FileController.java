package controller;

import dao.FileDAO;
import dao.SharingDAO;
import model.FileShareItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.File;
import java.util.UUID;
import model.FileItem;
import model.UserAccount;
import utils.SessionUtils;
import java.sql.SQLException;
import java.util.List;
import model.ShareItem;

public class FileController extends HttpServlet {
    FileDAO dao;
    SharingDAO sharingDAO;

    @Override
    public void init() {
        dao = new FileDAO();
        sharingDAO = new SharingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = (UserAccount)request.getAttribute("currentUser");

        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String filter = request.getParameter("filter");
        String path = request.getParameter("path");
        String shared = request.getParameter("shared");

        try {
            if (shared != null) {
                List<ShareItem> items = sharingDAO.getAllSharedFromUser(
                        account.getUsername()
                );

                request.setAttribute("items", items);
                request.getRequestDispatcher("/components/dashboard/SharedFileList.jsp").forward(request, response);
                return;
            }

            List<FileItem> items = dao.getAllFilesFromOwner(
                    account.getUsername(),
                    path
            );

            if (filter != null) {
                switch (filter) {
                    case "file" -> {
                        items.removeIf((FileItem item) -> item.isFolder());
                    }
                    case "folder" -> {
                        items.removeIf((FileItem item) -> !item.isFolder());
                    }
                }
            }

            if (sortBy != null && order != null) {
                int factor = order.equals("desc") ? -1 : 1;

                items.sort((FileItem item1, FileItem item2) -> {
                    switch (sortBy) {
                        case "name" -> {
                            int score = item1.getFileName().compareTo(item2.getFileName());
                            return factor * score;
                        }
                        case "date" -> {
                            int score = item1.getUpdated().compareTo(item2.getUpdated());
                            return factor * score;
                        }
                    }

                    return 0;
                });
            }

            items.sort((FileItem item1, FileItem item2) -> {
                return item1.isFolder() ? -1 : 1;
            });

            request.setAttribute("items", items);
            request.getRequestDispatcher("/components/dashboard/FileList.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(500, e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserAccount account = SessionUtils.getSessionUser(request);
        if (account == null) {
            response.sendError(403, "Forbidden");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "createFolder" -> {
                    String folderName = request.getParameter("folderName");
                    String parent = request.getParameter("parent");

                    FileItem item = new FileItem();
                    item.setFileName(folderName);
                    item.setIsFolder(true);
                    item.setOwner(account.getUsername());
                    item.setPath(parent == null ? "-1" : parent);

                    try {
                        dao.createFile(item);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    }
                }
                case "moveFiles" -> {
                    String target = request.getParameter("target");
                    String[] files = request.getParameterValues("files");

                    try {
                        Integer[] ids = new Integer[files.length];

                        for (int i = 0; i < files.length; i++) {
                            ids[i] = Integer.parseInt(files[i]);
                        }

                        dao.moveFiles(ids, Integer.parseInt(target), account.getUsername(), false);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        response.sendError(400, e.getMessage());
                        return;
                    }
                }
                case "markTrash" -> {
                    String[] files = request.getParameterValues("files");
                    String trashed = request.getParameter("trashed");
                    boolean isTrashed = false;

                    if (trashed != null && trashed.equals("true")) {
                        isTrashed = true;
                    }

                    try {
                        Integer[] ids = new Integer[files.length];

                        for (int i = 0; i < files.length; i++) {
                            ids[i] = Integer.parseInt(files[i]);
                        }

                        dao.markTrash(ids, isTrashed, account.getUsername());
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        response.sendError(400, e.getMessage());
                        return;
                    }
                }
                case "shareFile" -> {
                    int id = Integer.parseInt(request.getParameter("fileId"));
                    String sharedTo = request.getParameter("sharedTo");
                    String sharedFrom = account.getUsername();
                    boolean isPublic = Boolean.parseBoolean(request.getParameter("public"));

                    FileShareItem item = new FileShareItem();
                    item.setFileId(id);
                    item.setSharedTo(sharedTo);
                    item.setSharedBy(sharedFrom);
                    item.setIsPublic(isPublic);

                    try {
                        sharingDAO.createShare(item);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    }
                }
                case "rename" -> {
                    String newName = request.getParameter("newName");
                    int id = Integer.parseInt(request.getParameter("file"));

                    try {
                        dao.updateFileByAttribute(id, "fileName", newName);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    }
                }
                case "undoShare" -> {
                    String[] files = request.getParameterValues("files");

                    try {
                        for (int i = 0; i < files.length; i++) {
                            int id = Integer.parseInt(files[i]);
                            sharingDAO.deleteShare(id, account.getUsername());
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        response.sendError(500, "Internal Server Error");
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        response.sendError(400, e.getMessage());
                        return;
                    }
                }
            }

            return;
        }

        String defaultPath = getServletContext().getInitParameter("storagePath");
        String uuid = UUID.randomUUID().toString();
        String fileLocation = defaultPath + File.separator + uuid;
        String parent = request.getParameter("parent");

        for (Part part : request.getParts()) {
            part.write(fileLocation);


            FileItem item = new FileItem();
            item.setFileName(part.getSubmittedFileName());
            item.setIsFolder(false);
            item.setLocation(uuid);
            item.setOwner(account.getUsername());
            item.setPath(parent == null ? "-1" : parent);

            try {
                dao.createFile(item);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                response.sendError(500, "Internal Server Error");
                return;
            }
        }
    }
}
