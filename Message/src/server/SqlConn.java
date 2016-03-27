package server;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlConn {

    // Connection configurations
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
    private static final String username="root";
    private static final String password="576677";
    private static Connection conn=null;

    static {
        try {
            Class.forName(driver);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        if(conn==null) {
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        }

        return conn;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new SqlConn().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
