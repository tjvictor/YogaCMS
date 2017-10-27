package yoga.dao.Imp;

import org.springframework.stereotype.Component;
import yoga.dao.VideoDao;
import yoga.model.Video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoDaoImp extends BaseDao implements VideoDao {
    @Override
    public List<Video> getAllVideos() throws SQLException {
        List<Video> items = new ArrayList<Video>();

        String selectSql = String.format("select * from Video");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Video item = new Video();
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setType(rs.getString(3));
                        item.setImgPath(rs.getString(4));
                        item.setVideoPath(rs.getString(5));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }
}
