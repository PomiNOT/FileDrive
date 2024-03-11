package dao;

import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import model.StorageHistoryItem;

public class StorageHistoryDAO extends DBContext {
    public List<StorageHistoryItem> getAllForUser(String username) throws SQLException {
        var stmt = connection.prepareStatement("select * from UsageHistory where username = ?");
        stmt.setString(1, username);

        var rs = stmt.executeQuery();
        var list = new ArrayList<StorageHistoryItem>();

        while (rs.next()) {
            list.add(new StorageHistoryItem(
                    rs.getString("username"),
                    rs.getInt("total"),
                    rs.getTimestamp("updated")
            ));
        }

        return list;
    }
}
