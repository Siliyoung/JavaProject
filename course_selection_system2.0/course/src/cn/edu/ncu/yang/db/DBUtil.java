package cn.edu.ncu.yang.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    //    private static final String URL="jdbc:mysql://localhost:3306/demo_jdbc";
//    private static final String URL="jdbc:mysql://localhost:3306/mytest?useUnicode=true&characterEncoding=utf8SSL=true&serverTineZone=UTC";
    private static final String URL="jdbc:mysql://localhost:3306/course_selection_system?useUnicode=true&serverTimeZone=UTC";
    private static final String NAME="root";
    private static final String PASSWORD="root123456";


    private static Connection conn=null;
    //静态代码块（将加载驱动、连接数据库放入静态块中）
    static{
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获得数据库的连接
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //对外提供一个方法来获取数据库连接
    public static Connection getConnection(){
        return conn;
    }
}
