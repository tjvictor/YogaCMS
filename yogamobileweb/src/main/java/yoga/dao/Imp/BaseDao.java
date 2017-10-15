package yoga.dao.Imp;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.*;

@Component
public class BaseDao {

    @Value("${db.connectString}")
    protected String dbConnectString;

    public int insert(String insertSql) throws SQLException {
        return update(insertSql);
    }

    public int update(String updateSql) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            try(Statement stmt = connection.createStatement()) {
                return stmt.executeUpdate(updateSql);
            }
        }
    }

    public int delete(String deleteSql) throws SQLException {
        return update(deleteSql);
    }

    public String escapeString(String item){
        if(StringUtils.isEmpty(item))
            return "";
        return StringEscapeUtils.escapeHtml4(item);
    }
}
