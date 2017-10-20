package yoga.dao;

import yoga.model.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {
    List<Course> getCourses() throws SQLException;

    Course getCourseById(String id) throws SQLException;

    void insertCourse(Course course) throws SQLException;

    void updateCourse(Course course) throws SQLException;

    void deleteCourse(String id) throws SQLException;
}
