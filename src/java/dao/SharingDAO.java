package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.FileItem;
import model.FileShareItem;
import model.ShareItem;
import utils.FileUtils;

public class SharingDAO extends DBContext {
    private String basePath;
    
    public SharingDAO(String basePath) {
        this.basePath = basePath;
    }
    
    public void createShare(FileShareItem item) throws SQLException {
        String sql = "INSERT INTO Sharing (fileId, sharedTo, sharedBy, isPublic) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, item.getFileId());
        stmt.setString(2, item.getSharedTo());
        stmt.setString(3, item.getSharedBy());
        stmt.setBoolean(4, item.isPublic());
        stmt.executeUpdate();
        connection.commit();
    }

    public void deleteShare(int id, String owner) throws SQLException {
        String sql = "DELETE FROM Sharing WHERE fileId = ? AND sharedBy = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, owner);
        stmt.executeUpdate();
        connection.commit();
    }

    private ShareItem makeItemFromResultSet(ResultSet rs) throws SQLException {
        FileItem item = new FileItem();
        FileShareItem shareItem = new FileShareItem();

        item.setFileId(rs.getInt("file_id"));
        item.setFileName(rs.getString("file_name"));
        item.setIsFolder(rs.getBoolean("is_folder"));
        item.setOwner(rs.getString("owner"));
        item.setLocation(FileUtils.getFilePath(basePath, rs.getString("location")));
        item.setPath(rs.getString("path"));
        item.setOldParent(rs.getInt("old_parent"));
        item.setUpdated(rs.getTimestamp("updated"));

        shareItem.setFileId(rs.getInt("sharing_id"));
        shareItem.setIsPublic(rs.getBoolean("is_public"));
        shareItem.setSharedBy(rs.getString("shared_by"));
        shareItem.setSharedTo(rs.getString("shared_to"));

        return new ShareItem(item, shareItem);
    }

    private List<ShareItem> makeArrayFromResultSet(ResultSet rs) throws SQLException {
        List<ShareItem> list = new ArrayList<>();

        while (rs.next()) {
            list.add(makeItemFromResultSet(rs));
        }

        return list;
    }

    public List<ShareItem> getAllSharedFromUser(String username) throws SQLException {
        String sql = "SELECT "
                + "f.fileId AS file_id, "
                + "f.fileName AS file_name, "
                + "f.isFolder AS is_folder, "
                + "f.location AS location, "
                + "f.oldParent AS old_parent, "
                + "f.owner AS owner, "
                + "f.path AS path, "
                + "f.updated AS updated, "
                + "s.fileId AS sharing_id, "
                + "s.isPublic AS is_public, "
                + "s.sharedBy AS shared_by, "
                + "s.sharedTo AS shared_to "
                + "FROM Sharing s "
                + "INNER JOIN Files f ON s.fileId = f.fileId "
                + "WHERE s.sharedBy = ? "
                + "AND f.path LIKE '-1%';";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        connection.commit();

        return makeArrayFromResultSet(rs);
    }

    public ShareItem getSharedFileById(int id) throws SQLException {
        String sql = "SELECT "
                + "f.fileId AS file_id, "
                + "f.fileName AS file_name, "
                + "f.isFolder AS is_folder, "
                + "f.location AS location, "
                + "f.oldParent AS old_parent, "
                + "f.owner AS owner, "
                + "f.path AS path, "
                + "f.updated AS updated, "
                + "s.fileId AS sharing_id, "
                + "s.isPublic AS is_public, "
                + "s.sharedBy AS shared_by, "
                + "s.sharedTo AS shared_to "
                + "FROM Sharing s "
                + "INNER JOIN Files f ON s.fileId = f.fileId "
                + "WHERE s.fileId = ? "
                + "AND f.path LIKE '-1%';";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        connection.commit();


        if (rs.next()) {
            return makeItemFromResultSet(rs);
        } else {
            return null;
        }
    }
}
