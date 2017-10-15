package yoga.dao;

import yoga.model.Schedule;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ScheduleDao {
    List<Schedule> getSchedules() throws SQLException;

    List<Schedule> getSchedulesByFilter(String courseId, String teacherId, String startDate, String endDate) throws SQLException, ParseException;

    void insertSchedule(Schedule schedule) throws SQLException;

    void updateSchedule(Schedule schedule) throws SQLException;

    void deleteSchedule(String id) throws SQLException;

    void physicalDeleteSchedule(String id) throws SQLException;
}
