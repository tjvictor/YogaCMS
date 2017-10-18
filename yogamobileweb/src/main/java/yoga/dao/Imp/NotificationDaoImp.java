package yoga.dao.Imp;


import yoga.dao.NotificationDao;
import yoga.model.Notification;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class NotificationDaoImp extends BaseDao implements NotificationDao{
    @Override
    public List<Notification> getNotifications() throws SQLException {
        List<Notification> items = new ArrayList<Notification>();
        String selectSql = "select * from Notification;";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Notification item = new Notification();
                        item.setId(rs.getString(1));
                        item.setTitle(rs.getString(2));
                        item.setContent(rs.getString(3));
                        item.setDate(rs.getString(4));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public Notification getNotificationById(String id) throws SQLException {
        Notification item = new Notification();
        String selectSql = String.format("select * from Notification where id = '%s';", id);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        item.setId(rs.getString(1));
                        item.setTitle(rs.getString(2));
                        item.setContent(rs.getString(3));
                        item.setDate(rs.getString(4));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public List<Notification> getTopNotificationBriefs(String topCount) throws SQLException {
        List<Notification> items = new ArrayList<Notification>();
        String selectSql = String.format("select * from Notification order by Date desc limit %s;", topCount);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Notification item = new Notification();
                        item.setId(rs.getString(1));
                        item.setTitle(rs.getString(2));
                        item.setContent("");
                        item.setDate(rs.getString(4));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void insertNotification(Notification item) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Notification values(?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {

                ps.setString(1, item.getId());
                ps.setString(2, item.getTitle());
                ps.setString(3, item.getContent());
                ps.setString(4, df.format(new Date()));
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updateNotification(Notification item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Notification set Title=?, Content=? where id = ?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {

                ps.setString(1, item.getTitle());
                ps.setString(2, item.getContent());
                ps.setString(3, item.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deleteNotification(String id) throws SQLException {
        String deleteSql = String.format("delete from Notification where id = '%s';", id);
        update(deleteSql);
    }
}
