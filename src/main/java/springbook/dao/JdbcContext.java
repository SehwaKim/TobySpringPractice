package springbook.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public JdbcContext(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = strategy.makePreparedStatement(c)){
            ps.executeUpdate();

        }catch (SQLException e){
            throw e;
        }
    }

    public void executeSql(String query) throws SQLException {
        workWithStatementStrategy((c) -> c.prepareStatement(query));
    }
}
