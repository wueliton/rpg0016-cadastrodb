package cadastro.model.util;

import java.sql.*;

public class ConectorBD {
    private Connection connection = null;

    public Connection getConnection() {
        if(connection != null) return connection;

        try {
            String url = "jdbc:sqlserver://localhost;databaseName=loja;encrypt=true;trustServerCertificate=true";
            String usuario = "loja";
            String senha = "loja";

            connection = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public PreparedStatement getPrepared(String sql) throws SQLException {
        Connection conn = getConnection();
        return conn.prepareStatement(sql);
    }

    public ResultSet getSelect(String sql) throws SQLException {
        PreparedStatement statement = getPrepared(sql);
        return statement.executeQuery();
    }

    public void close(Statement statement) {
        try {
            statement.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet resultSet) {
        try {
            resultSet.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
