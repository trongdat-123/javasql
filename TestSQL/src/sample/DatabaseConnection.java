package sample;
        import java.sql.*;

public class DatabaseConnection {
    public Connection databaselink;
    public Connection getConnection(){
        String databaseUser = "sa";
        String databasePassword = "1234";
        String url = "jdbc:sqlserver://localhost:1433;database=TRONGDAT";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            databaselink = DriverManager.getConnection(url, databaseUser, databasePassword);

        }catch (Exception e){
            e.printStackTrace();
        }
        return databaselink;
    }
    public void excute (String sql){
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
