package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.FileItem;

public class FileDAO extends DBContext {
    public void createFile(FileItem file) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO Files (owner, location, isFolder, path, fileName) VALUES (?, ?, ?, ?, ?)";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, file.getOwner());
        stmt.setString(2, file.getLocation());
        stmt.setBoolean(3, file.isFolder());
        stmt.setString(4, file.getPath());
        stmt.setString(5, file.getFileName());
        stmt.executeUpdate();
        connection.commit();
    }

    private FileItem makeItemFromResultSet(ResultSet rs) throws SQLException {
            FileItem item = new FileItem();

            item.setFileId(rs.getInt("fileId"));
            item.setFileName(rs.getString("fileName"));
            item.setIsFolder(rs.getBoolean("isFolder"));
            item.setOwner(rs.getString("owner"));
            item.setLocation(rs.getString("location"));
            item.setPath(rs.getString("path"));
            item.setOldParent(rs.getInt("oldParent"));
            item.setUpdated(rs.getTimestamp("updated"));

            return item;
    }

    private List<FileItem> makeArrayFromResultSet(ResultSet rs) throws SQLException {
        List<FileItem> list = new ArrayList<>();

        while (rs.next()) {
            list.add(makeItemFromResultSet(rs));
        }

        return list;
    }

    public List<FileItem> getAllFilesFromOwner(String owner, String path) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Files WHERE owner = ? and path = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, owner);
        stmt.setString(2, path == null ? "-1" : path);
        rs = stmt.executeQuery();
        connection.commit();

        return makeArrayFromResultSet(rs);
    }

    public void markTrash(Integer[] fileIds, boolean trashed, String owner) throws Exception {
        if (trashed) {
            for (int file : fileIds) {
                FileItem item = getFileById(file);

                String[] idChain = item.getPath().split("\\.");
                Integer oldParent = Integer.parseInt(idChain[idChain.length - 1]);

                updateFileByAttribute(file, "oldParent", oldParent);
            }

            moveFiles(fileIds, -2, owner, false);
        } else {
            for (int file : fileIds) {
                FileItem item = getFileById(file);

                int oldParent = item.getOldParent();
                if (oldParent != -1) {
                    FileItem oldParentInfo = getFileById(oldParent);

                    // if parent is trashed, move to root
                    if (oldParentInfo.getPath().startsWith("-2")) {
                        oldParent = -1;
                    }
                }

                moveFiles(fileIds, oldParent, owner, true);
            }
        }
    }

    public void moveFiles(Integer[] fileIds, int target, String owner, boolean moveToRootOnInvalid) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String newParentPath = "-1";
        if (target == -2) { //trash folder
            newParentPath = "-2";
        } else if (target != -1 && target != -2) { //any other folder that is not root
            FileItem targetInfo = getFileById(target);

            if (targetInfo != null) {
                newParentPath = targetInfo.getDescendantPath();
            } else if (targetInfo == null && !moveToRootOnInvalid) {
                throw new Exception("Target cannot be found");
            }
        }  // else root
        
        for (int file : fileIds) {
            if (file == target) {
                throw new Exception("Cannot move a file onto itself");
            }
        }

        for (int file : fileIds) {
            FileItem treeItem = getFileById(file);

            if (treeItem.isFolder()) {
                String currentPath = treeItem.getDescendantPath();
                String newPath = newParentPath + "." + treeItem.getFileId();
                String updateDescendants = "UPDATE Files SET path = REPLACE(path, ?, ?) WHERE path like ? and owner = ?";

                stmt = connection.prepareStatement(updateDescendants);
                stmt.setString(1, currentPath);
                stmt.setString(2, newPath);
                stmt.setString(3, currentPath + "%");
                stmt.setString(4, owner);
                stmt.executeUpdate();
            }
        }

        connection.commit();

        for (int file : fileIds) {
            String updateChildren = "UPDATE Files SET path = ? WHERE fileId = ? and owner = ?";
            stmt = connection.prepareStatement(updateChildren);
            stmt.setString(1, newParentPath);
            stmt.setInt(2, file);
            stmt.setString(3, owner);
            stmt.executeUpdate();
        }

        connection.commit();
    }

    public FileItem getFileById(int fileId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Files WHERE fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, fileId);
        rs = stmt.executeQuery();

        connection.commit();

        if (rs.next()) {
            return makeItemFromResultSet(rs);
        }

        return null;
    }

    public void updateFileByAttribute(int id, String attr, Object value) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "UPDATE Files SET " + attr + " = ? where fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setObject(1, value);
        stmt.setInt(2, id);
        stmt.executeUpdate();

        connection.commit();
    }

    public void deleteFile(int id) throws SQLException {
        PreparedStatement stmt = null;

        String sql = "DELETE FROM Files WHERE fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();

        connection.commit();
    }
}

