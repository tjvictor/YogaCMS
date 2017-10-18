package yoga.dao;


import yoga.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationDao {

    List<Notification> getNotifications() throws SQLException;

    Notification getNotificationById(String id) throws SQLException;

    List<Notification> getTopNotificationBriefs(String topCount) throws SQLException;

    void insertNotification(Notification item) throws SQLException;

    void updateNotification(Notification item) throws SQLException;

    void deleteNotification(String id) throws SQLException;
}
