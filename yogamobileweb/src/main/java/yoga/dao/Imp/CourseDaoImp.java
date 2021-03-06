package yoga.dao.Imp;

import yoga.dao.CourseDao;
import yoga.model.Course;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseDaoImp extends BaseDao implements CourseDao {

    @Override
    public List<Course> getCourses() throws SQLException {
        List<Course> items = new ArrayList<Course>();
        String selectSql = String.format("select * from Course where IsDel = 0");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Course item = new Course();
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setAvatar(rs.getString(3));
                        item.setIntroduction(rs.getString(4));
                        item.setRating(rs.getInt(5));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public Course getCourseById(String id) throws SQLException {
        List<Course> items = new ArrayList<Course>();
        String selectSql = String.format("select * from Course where id = '%s' and IsDel = 0", id);
        Course item = new Course();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setAvatar(rs.getString(3));
                        item.setIntroduction(rs.getString(4));
                        item.setRating(rs.getInt(5));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public void insertCourse(Course course) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into Course values(?,?,?,?,?,?)";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, course.getId());
                ps.setString(2, course.getName());
                ps.setString(3, course.getAvatar());
                ps.setString(4, course.getIntroduction());
                ps.setInt(5, course.getRating());
                ps.setInt(6, course.getIsDel());
                ps.executeUpdate();
            }
        }
        course.setIntroduction(course.getIntroduction());
    }

    @Override
    public void updateCourse(Course course) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "update Course set Name=?, Avatar=?, Introduction=?, Rating=? where id = ?";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, course.getName());
                ps.setString(2, course.getAvatar());
                ps.setString(3, course.getIntroduction());
                ps.setInt(4, course.getRating());
                ps.setString(5, course.getId());
                ps.executeUpdate();
            }
        }
        course.setIntroduction(course.getIntroduction());
    }

    @Override
    public void deleteCourse(String id) throws SQLException {
        String deleteSql = String.format("update Course set IsDel = 1 where id = '%s'", id);
        delete(deleteSql);
    }
}
