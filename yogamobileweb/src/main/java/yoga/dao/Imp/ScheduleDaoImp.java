package yoga.dao.Imp;

import yoga.dao.ScheduleDao;
import yoga.model.Schedule;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleDaoImp extends BaseDao implements ScheduleDao {

    @Override
    public List<Schedule> getSchedules() throws SQLException {
        List<Schedule> items = new ArrayList<Schedule>();
        String selectSql = String.format("select s.Id, s.TeacherId,s.CourseId,s.StartTime,s.EndTime,s.Capacity,s.Status,s.Location,t.Name,c.Name from Schedule s \n" +
                "left join Teacher t on s.TeacherId=t.id \n" +
                "left join Course c on s.CourseId=c.id \n" +
                "where s.IsDel = 0 order by s.StartTime desc");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Schedule item = new Schedule();
                        item.setId(rs.getString(1));
                        item.setTeacherId(rs.getString(2));
                        item.setCourseId(rs.getString(3));
                        item.setStartDateTime(rs.getString(4));
                        item.setEndDateTime(rs.getString(5));
                        item.setCapacity(rs.getInt(6));
                        item.setStatus(rs.getString(7));
                        item.setLocation(rs.getString(8));
                        item.setTeacherName(rs.getString(9));
                        item.setCourseName(rs.getString(10));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<Schedule> getSchedulesByFilter(String courseId, String teacherId, String startDate, String endDate) throws SQLException, ParseException {
        List<Schedule> items = new ArrayList<Schedule>();
        String selectSql = String.format("select s.Id, s.TeacherId,s.CourseId,s.StartTime,s.EndTime,s.Capacity,s.Status,s.Location,t.Name,c.Name,c.Rating,c.Avatar from Schedule s \n" +
                "left join Teacher t on s.TeacherId=t.id \n" +
                "left join Course c on s.CourseId=c.id \n" +
                "where s.IsDel = 0");

        if(StringUtils.isNotEmpty(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(endDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            endDate = sdf.format(cal.getTime());
        }


        if (StringUtils.isNotEmpty(courseId))
            selectSql += String.format(" and s.CourseId = '%s'", courseId);
        if (StringUtils.isNotEmpty(teacherId))
            selectSql += String.format(" and s.TeacherId = '%s'", teacherId);
        if (StringUtils.isNotEmpty(startDate))
            selectSql += String.format(" and s.StartTime >= '%s'", startDate);
        if (StringUtils.isNotEmpty(endDate))
            selectSql += String.format(" and s.EndTime <= '%s'", endDate);

        selectSql += " order by s.StartTime desc";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Schedule item = new Schedule();
                        item.setId(rs.getString(1));
                        item.setTeacherId(rs.getString(2));
                        item.setCourseId(rs.getString(3));
                        item.setStartDateTime(rs.getString(4));
                        item.setEndDateTime(rs.getString(5));
                        item.setCapacity(rs.getInt(6));
                        item.setStatus(rs.getString(7));
                        item.setLocation(rs.getString(8));
                        item.setTeacherName(rs.getString(9));
                        item.setCourseName(rs.getString(10));
                        item.setCourseRating(rs.getInt(11));
                        item.setCourseAvatar(rs.getString(12));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void insertSchedule(Schedule schedule) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Schedule values(?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, schedule.getId());
                ps.setString(2, schedule.getTeacherId());
                ps.setString(3, schedule.getCourseId());
                ps.setString(4, schedule.getStartDateTime());
                ps.setString(5, schedule.getEndDateTime());
                ps.setInt(6, schedule.getCapacity());
                ps.setString(7, schedule.getStatus());
                ps.setString(8, schedule.getLocation());
                ps.setInt(9, schedule.getIsDel());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updateSchedule(Schedule schedule) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Schedule set TeacherId=?, CourseId=?, StartTime=?, EndTime=?, Capacity=?, Location=? where id=?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {

                ps.setString(1, schedule.getTeacherId());
                ps.setString(2, schedule.getCourseId());
                ps.setString(3, schedule.getStartDateTime());
                ps.setString(4, schedule.getEndDateTime());
                ps.setInt(5, schedule.getCapacity());
                ps.setString(6, schedule.getStatus());
                ps.setString(7, schedule.getLocation());
                ps.setString(8, schedule.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deleteSchedule(String id) throws SQLException {
        String deleteSql = String.format("update Schedule set IsDel=1 where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public void physicalDeleteSchedule(String id) throws SQLException {
        String deleteSql = String.format("delete from Schedule where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public Schedule getScheduleById(String scheduleId) throws SQLException {
        String selectSql = String.format("SELECT Id, TeacherId, CourseId, StartTime, EndTime, Capacity, Status, Location, IsDel FROM Schedule where Id = '%s';", scheduleId);
        Schedule item = new Schedule();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        item.setId(rs.getString(1));
                        item.setTeacherId(rs.getString(2));
                        item.setCourseId(rs.getString(3));
                        item.setStartDateTime(rs.getString(4));
                        item.setEndDateTime(rs.getString(5));
                        item.setCapacity(rs.getInt(6));
                        item.setStatus(rs.getString(7));
                        item.setLocation(rs.getString(8));
                        item.setIsDel(rs.getInt(9));
                    }
                }
            }
        }

        return item;
    }
}