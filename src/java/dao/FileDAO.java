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
    }

    private FileItem makeItemFromResultSet(ResultSet rs) throws SQLException {
            FileItem item = new FileItem();

            item.setFileId(rs.getInt("fileId"));
            item.setFileName(rs.getString("fileName"));
            item.setIsFolder(rs.getBoolean("isFolder"));
            item.setOwner(rs.getString("owner"));
            item.setLocation(rs.getString("location"));
            item.setPath(rs.getString("path"));
            item.setTrashed(rs.getBoolean("trashed"));
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
        stmt.setString(2, path == null ? "root" : path);
        rs = stmt.executeQuery();

        return makeArrayFromResultSet(rs);
    }

    public void moveFiles(Integer[] fileIds, int target) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        FileItem targetInfo = getFileById(target);

        if (targetInfo == null) {
            throw new Exception("Target cannot be found");
        }

        for (int file : fileIds) {
            if (file == target) {
                throw new Exception("Cannot move a file onto itself");
            }
        }

        String newParentPath = targetInfo.getDescendantPath();

        for (int file : fileIds) {
            FileItem treeItem = getFileById(file);

            if (treeItem.isFolder()) {
                String currentPath = treeItem.getDescendantPath();
                String newPath = newParentPath + "." + treeItem.getFileId();
                String updateDescendants = "UPDATE Files SET path = REPLACE(path, ?, ?) WHERE path like ?";

                stmt = connection.prepareStatement(updateDescendants);
                stmt.setString(1, currentPath);
                stmt.setString(2, newPath);
                stmt.setString(3, currentPath + "%");
                stmt.executeUpdate();
            }
        }

        for (int file : fileIds) {
            String updateChildren = "UPDATE Files SET path = ? WHERE fileId = ?";
            stmt = connection.prepareStatement(updateChildren);
            stmt.setString(1, newParentPath);
            stmt.setInt(2, file);
            stmt.executeUpdate();
        }
    }

    public void updateFile(FileItem file) throws SQLException {
        PreparedStatement stmt = null;

        String sql = "UPDATE Files SET owner = ?, path = ?, fileName = ? WHERE fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, file.getOwner());
        stmt.setString(2, file.getPath());
        stmt.setString(3, file.getFileName());
        stmt.executeUpdate();
    }

    public FileItem getFileById(int fileId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Files WHERE fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, fileId);
        rs = stmt.executeQuery();

        if (rs.next()) {
            return makeItemFromResultSet(rs);
        }

        return null;
    }

    public void deleteFile(int id) throws SQLException {
        PreparedStatement stmt = null;

        String sql = "DELETE FROM Files WHERE fileId = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}

