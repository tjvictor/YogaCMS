package yoga.dao;

import yoga.model.Video;

import java.sql.SQLException;
import java.util.List;

public interface VideoDao {

    List<Video> getAllVideos() throws SQLException;
}
