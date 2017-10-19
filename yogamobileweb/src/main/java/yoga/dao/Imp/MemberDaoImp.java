package yoga.dao.Imp;

import yoga.dao.MemberDao;
import yoga.model.Member;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MemberDaoImp extends BaseDao implements MemberDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void insertMember(Member member) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Member values(?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, member.getId());
                ps.setString(2, member.getName());
                ps.setString(3, member.getSex());
                ps.setString(4, member.getTel());
                ps.setString(5, member.getPassword());
                ps.setString(6, member.getJoinDate());
                ps.setString(7, member.getExpireDate());
                ps.setInt(8, member.getFee());
                ps.setString(9, member.getRemark());
                ps.setInt(10, member.getIsDel());
                ps.executeUpdate();
            }
        }
        member.setRemark(escapeString(member.getRemark()));
    }

    @Override
    public void updateMember(Member member) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Member set Name=?, Sex=?, Tel=?, Password=?, JoinDate=?, ExpireDate=?, Fee=?, Remark=? where id = ?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, member.getName());
                ps.setString(2, member.getSex());
                ps.setString(3, member.getTel());
                ps.setString(4, member.getPassword());
                ps.setString(5, member.getJoinDate());
                ps.setString(6, member.getExpireDate());
                ps.setInt(7, member.getFee());
                ps.setString(8, member.getRemark());
                ps.setString(9, member.getId());
                ps.executeUpdate();
            }
        }
        member.setRemark(escapeString(member.getRemark()));
    }

    @Override
    public boolean isMemberExistedById(String id) throws SQLException {
        String selectSql = String.format("select count(0) from Member where id = '%s'", id);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public boolean isMobileExisted(String tel) throws SQLException {
        String selectSql = String.format("select count(0) from Member where tel = '%s'", tel);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public boolean isMobileDuplicated(String id, String tel) throws SQLException {
        String selectSql = String.format("select count(0) from Member where tel = '%s' and id != '%s'", tel, id);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public List<Member> getMembers(String name, String tel) throws SQLException {
        List<Member> items = new ArrayList<Member>();
        String selectSql = String.format("select * from Member  where IsDel = 0 ");
        if (StringUtils.isNotEmpty(name))
            selectSql += String.format(" and Name = '%s'", name);
        if (StringUtils.isNotEmpty(tel))
            selectSql += String.format(" and Tel = '%s'", tel);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        Member item = new Member();
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setSex(rs.getString(3));
                        item.setTel(rs.getString(4));
                        item.setPassword(rs.getString(5));
                        item.setJoinDate(rs.getString(6));
                        item.setExpireDate(rs.getString(7));
                        item.setFee(rs.getInt(8));
                        item.setRemark(escapeString(rs.getString(9)));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void deleteMember(String id) throws SQLException {
        String deleteSql = String.format("update Member set IsDel=1 where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public Member authenciateUser(String tel, String password) throws SQLException {
        String selectSql = String.format("select Id, ExpireDate from Member where Tel = '%s' and Password = '%s';", tel, password);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()){
                        Member member = new Member();
                        member.setId(rs.getString(1));
                        member.setExpireDate(rs.getString(2));
                        return member;
                    }

                    return null;
                }
            }
        }
    }

    @Override
    public Member getMemberById(String id) throws SQLException {
        String selectSql = String.format("select * from Member where id = '%s';", id);
        Member item = new Member();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        item.setId(rs.getString(1));
                        item.setName(rs.getString(2));
                        item.setSex(rs.getString(3));
                        item.setTel(rs.getString(4));
                        item.setPassword(rs.getString(5));
                        item.setJoinDate(rs.getString(6));
                        item.setExpireDate(rs.getString(7));
                        item.setFee(rs.getInt(8));
                        item.setRemark(escapeString(rs.getString(9)));
                    }
                }
            }
        }
        return item;
    }

    @Override
    public void changeMemberPwd(String id, String pwd) {
        String selectSql = String.format("update Member set Password = '%s' where id = '%s';", pwd, id);
    }


}
