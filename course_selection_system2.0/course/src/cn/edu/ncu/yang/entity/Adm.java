package cn.edu.ncu.yang.entity;

import cn.edu.ncu.yang.db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Adm管理员，
 * 功能：查询课程，添加课程，删除课程
 */
public class Adm {
    //连接数据库
    private static Connection con= DBUtil.getConnection();
    //管理员id
    private String id;
    //管理员密码
    private String password;
    //创建一个课程类，方便后面操作
    private List<Curriculum> courses=new ArrayList<>();

    public Adm(String id, String password) {
        this.id = id;
        this.password = password;
    }


    /**
     * 注册
     * @param id   账号
     * @param password 密码
     * @return
     */
    public static Adm Register(String id,String password){

        try {
            String sql2="insert into adm(id,password) values('"+id+"','"+password+"')";
            PreparedStatement psmt=DBUtil.getConnection().prepareCall(sql2);
            psmt.executeUpdate();
         } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Adm(id,password);
    }


    /**
     * 登录
     * @param id 账号
     * @param password 密码
     * @return
     */
    public static Adm Login(String id,String password){
        String sql="select * from student where student_id ='"+id+"'"+"and student_password='"+password+"'";

        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.next()) {
                return Register(id,password);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Adm(id,password);
    }



    /**
     * 得到所有课程信息
     * @return
     */
    public  List<Curriculum> getCourses(){
        try{
            Statement statement = con.createStatement(); //2.创建statement类对象，用来执行SQL语句！！
            String sql = "SELECT * FROM curriculum ";//要执行的SQL语句
            ResultSet rs = statement.executeQuery(String.format(sql));
            while (rs.next()) {
                courses.add(Curriculum.getACurriculum(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courses;

    }

    /**
     * 添加课程
     * @param course
     */
    public void addCourse(Curriculum course){
        try {
            String sql2="insert into curriculum(course_id,teacher_name,course_name,course_hours,course_nature,opening_semester,student_number,course_credit) values('"
                    +course.getId()+
                    "','"+course.getTeacherName()+
                    "','"+course.getName()+
                    "','"+course.getClassHours()+
                    "','"+course.getNature()+
                    "','"+course.getOpeningSemester()+
                    "','"+course.getStudentNumber()+
                    "','"+course.getCredit()+"')";
            PreparedStatement psmt=DBUtil.getConnection().prepareCall(sql2);
            psmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * 删除课程
     * @param courseId
     */
    public boolean delCourse(String courseId){

        try{
            var sql = "delete from curriculum where course_id='"+courseId+"'";
            PreparedStatement psmt = con.prepareStatement(String.format(sql));
            //执行SQL语句
            psmt.executeUpdate(String.format(sql));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }



    public void setCourses(java.util.List<Curriculum> courses) {
        this.courses = courses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
