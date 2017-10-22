package yoga.dao.Imp;

import yoga.dao.ScheduleDao;
import yoga.dao.SubScheduleDao;
import yoga.model.Schedule;
import yoga.model.SubSchedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SubScheduleDaoImp extends BaseDao implements SubScheduleDao {

    @Autowired
    private ScheduleDao scheduleDaoImp;


    @Override
    public List<SubSchedule> getSubSchedulesByFilter(String startDate, String endDate) throws SQLException, ParseException {

        List<Schedule> schedules = scheduleDaoImp.getSchedulesByFilter("", "", startDate, endDate);
        List<SubSchedule> subSchedules = new ArrayList<SubSchedule>();
        String selectSql = "select count(0) from SubSchedule where scheduleId = '%s'";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                for (Schedule scheduleItem : schedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, scheduleItem.getId()))) {
                        if (rs.next()) {
                            SubSchedule item = new SubSchedule();
                            item.setScheduleId(scheduleItem.getId());
                            item.setCourseName(scheduleItem.getCourseName());
                            item.setTeacherName(scheduleItem.getTeacherName());
                            item.setStartDateTime(scheduleItem.getStartDateTime());
                            item.setEndDateTime(scheduleItem.getEndDateTime());
                            item.setLocation(scheduleItem.getLocation());
                            item.setCapacity(scheduleItem.getCapacity());
                            item.setSubCount(rs.getInt(1));

                            subSchedules.add(item);
                        }
                    }
            }
        }

        return subSchedules;
    }

    @Override
    public List<SubSchedule> getSubScheduledMembers(String subScheduleId) throws SQLException {
        List<SubSchedule> subSchedules = new ArrayList<SubSchedule>();

        String selectSql = String.format("select sub.Id, sub.MemberId, m.Name, m.Tel from SubSchedule sub\n" +
                "join Member m on sub.MemberId = m.Id\n" +
                "where sub.ScheduleId = '%s'", subScheduleId);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        SubSchedule item = new SubSchedule();
                        item.setId(rs.getString(1));
                        item.setMemberId(rs.getString(2));
                        item.setMemberName(rs.getString(3));
                        item.setMemberTel(rs.getString(4));

                        subSchedules.add(item);
                    }
                }
            }

            return subSchedules;
        }
    }

    @Override
    public List<SubSchedule> getMobileSubSchedulesByFilter(String date, String userId) throws SQLException, ParseException {
        List<Schedule> schedules = scheduleDaoImp.getSchedulesByFilter("", "", date, date);
        List<SubSchedule> subSchedules = new ArrayList<SubSchedule>();
        String selectSql = "select count(0) from SubSchedule where scheduleId = '%s'";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                for (Schedule scheduleItem : schedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, scheduleItem.getId()))) {
                        if (rs.next()) {
                            SubSchedule item = new SubSchedule();
                            item.setScheduleId(scheduleItem.getId());
                            item.setCourseName(scheduleItem.getCourseName());
                            item.setTeacherName(scheduleItem.getTeacherName());
                            item.setStartDateTime(df.format(df.parse(scheduleItem.getStartDateTime())));
                            item.setEndDateTime(df.format(df.parse(scheduleItem.getEndDateTime())));
                            item.setLocation(scheduleItem.getLocation());
                            item.setCapacity(scheduleItem.getCapacity());
                            item.setCourseRating(scheduleItem.getCourseRating());
                            item.setCourseAvatar(scheduleItem.getCourseAvatar());
                            item.setStatus(scheduleItem.getStatus());
                            item.setSubCount(rs.getInt(1));

                            subSchedules.add(item);
                        }
                    }

                selectSql = "select id from SubSchedule where scheduleId = '%s' and memberId = '%s'";
                for(SubSchedule subScheduleItem : subSchedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, subScheduleItem.getScheduleId(), userId))) {
                        if (rs.next()) {
                            subScheduleItem.setId(rs.getString(1));
                            subScheduleItem.setMemberId(userId);
                        }
                    }
            }
        }

        return subSchedules;
    }

    @Override
    public void insertSubSchedule(SubSchedule subSchedule) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String insertSql = String.format("insert into SubSchedule values('%s','%s','%s', '%s');",
                subSchedule.getId(), subSchedule.getScheduleId(), subSchedule.getMemberId(), df.format(new Date()));

        insert(insertSql);
    }

    @Override
    public void updateSubSchedule(SubSchedule subSchedule) throws SQLException {

    }

    @Override
    public void deleteSubSchedule(SubSchedule subSchedule) throws SQLException {
        String deleteSql = String.format("delete from SubSchedule where ScheduleId = '%s' and MemberId = '%s';",
                subSchedule.getScheduleId(), subSchedule.getMemberId());
        delete(deleteSql);
    }

    @Override
    public void deleteSubSchedule(String id) throws SQLException {
        String deleteSql = String.format("delete from SubSchedule where id = '%s';", id);
        delete(deleteSql);
    }
}
