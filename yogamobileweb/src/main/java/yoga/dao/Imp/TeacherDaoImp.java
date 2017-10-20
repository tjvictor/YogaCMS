package yoga.dao.Imp;

import yoga.dao.TeacherDao;
import yoga.model.Teacher;

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
public class TeacherDaoImp extends BaseDao implements TeacherDao {

    @Override
    public List<Teacher> getTeachers() throws SQLException {
        List<Teacher> items = new ArrayList<Teacher>();
        String selectSql = String.format("select * from Teacher  where IsDel = 0");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Teacher item = new Teacher();
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setAvatar(rs.getString(3));
                        item.setIntroduction(rs.getString(4));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void insertTeacher(Teacher teacher) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into Teacher values(?,?,?,?,?)";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, teacher.getId());
                ps.setString(2, teacher.getName());
                ps.setString(3, teacher.getAvatar());
                ps.setString(4, teacher.getIntroduction());
                ps.setInt(5,teacher.getIsDel());
                ps.executeUpdate();
            }
        }
        teacher.setIntroduction(teacher.getIntroduction());
    }

    @Override
    public void updateTeacher(Teacher teacher) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "update Teacher set Name=?, Avatar=?, Introduction=? where id = ?";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, teacher.getName());
                ps.setString(2, teacher.getAvatar());
                ps.setString(3, teacher.getIntroduction());
                ps.setString(4, teacher.getId());
                ps.executeUpdate();
            }
        }
        teacher.setIntroduction(teacher.getIntroduction());
    }

    @Override
    public void deleteTeacher(String id) throws SQLException {
        String deleteSql = String.format("update Teacher set IsDel=1 where id = '%s'", id);
        delete(deleteSql);
    }
}
