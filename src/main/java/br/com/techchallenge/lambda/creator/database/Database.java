package br.com.techchallenge.lambda.creator.database;

import br.com.techchallenge.lambda.Parameters;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Database {

    public static Connection CONNECTION;

    static {

        try {
            System.err.println("Loading Driver class");
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.err.println("getting connection");
            CONNECTION = DriverManager.getConnection(
                    Parameters.getDbUrl(),
                    Parameters.getDbAdminUser(),
                    Parameters.getDbAdminPwd()
            );
            System.err.println("connected");
        } catch (Exception e) {
            System.err.println("connection FAILED");
            System.err.println(e.getMessage());
        }
    }

    public static String getTime() {
        Statement stmt = null;
        try {
            stmt = CONNECTION.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT NOW()");

            String currentTime = null;
            if (resultSet.next()) {
                currentTime = resultSet.getObject(1).toString();
                System.err.println("Successfully executed query.  Result: " + currentTime);
                return currentTime;
            } else {
                System.err.println("Error executed query.  Result: null");
                return currentTime;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> query(final String sql, Consumer<PreparedStatement> parameters) throws SQLException {
        PreparedStatement statement = Database.CONNECTION.prepareStatement(sql);
        if (parameters != null)
            parameters.accept(statement);
        ResultSet rs = statement.executeQuery();
        return resultSetToList(rs);
    }

    private static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()){
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for(int i = 1; i <= columns; ++i){
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

}
