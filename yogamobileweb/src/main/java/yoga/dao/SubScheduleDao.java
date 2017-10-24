package yoga.dao;

import yoga.model.SubSchedule;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface SubScheduleDao {
    List<SubSchedule> getSubSchedulesByFilter(String startDate, String endDate) throws SQLException, ParseException;

    List<SubSchedule> getSubScheduledMembers(String subScheduleId) throws SQLException;

    List<SubSchedule> getMobileSubSchedulesByFilter(String date, String userId) throws SQLException, ParseException;

    void insertSubSchedule(SubSchedule subSchedule) throws SQLException;

    void updateSubSchedule(SubSchedule subSchedule) throws SQLException;

    void deleteSubSchedule(SubSchedule subSchedule) throws SQLException;

    void deleteSubSchedule(String id) throws SQLException;

    int getSubScheduleMemberCountByScheduleId(String scheduleId) throws SQLException;
}
