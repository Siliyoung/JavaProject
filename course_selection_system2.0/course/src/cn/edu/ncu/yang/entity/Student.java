package cn.edu.ncu.yang.entity;

import cn.edu.ncu.yang.db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生类
 * 学号：id
 * 登录密码：password
 * 姓名：name
 * 班级：grade
 * 课程数：courseNumber
 * 总学分：totalCredit
 *
 *功能：添加课程，查询课程，删除课程，查看（修改个人信息）
 *
 *
 *
 */

public class Student {

    private static Connection con=DBUtil.getConnection();
    private String id;
    private String password;
    private String name;
    private String grade;
    private int courseNumber;
    private double totalCredit;
    private List<Curriculum> curriculumList;


    public Student() {

    }

    public Student(String id,String password){
        this.id=id;
        this.password=password;
    }

    public Student(String id, String password, String name, String grade, int courseNumber, double totalCredit) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.grade = grade;
        this.courseNumber = courseNumber;
        this.totalCredit = totalCredit;
    }

    /**
     * 登录
     * @param id
     * @param password
     * @return
     */
    public static Student Login(String id,String password) {
        String sql="select * from student where student_id ='"+id+"'"+"and student_password='"+password+"'";

        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return getAStudent(rs);
            }

            else{
                return Register(id,password);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    /**
     * 注册
     * @param id
     * @param password
     * @return
     */
    public static Student Register(String id,String password){
        String sql="select * from student where student_id ='"+id+"'"+"and student_password='"+password+"'";

        try {
            String sql2="insert into student(student_id,student_password) values('"+id+"','"+password+"')";
            PreparedStatement psmt=DBUtil.getConnection().prepareCall(sql2);
            psmt.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Login(id,password);

    }


    /**
     * 判断课程是否已选
     * @param course
     * @return
     */
    public Boolean isCourseSelected(Curriculum course){
        String sql="select * from student_course where student_id =%s and course_id=%s";
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(String.format(sql,getId(),course.getId()));
            if (rs.next()) {
                System.out.println(course.getName()+":该课程已经被选");
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    /**
     * 判断课程人数是否已满
     * @param course
     * @return
     */
    public boolean isFullPerson(Curriculum course){
        String sql="select * from curriculum where course_id='"+course.getId()+"'";
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                System.out.println("学生人数："+Curriculum.getACurriculum(rs).getStudentNumber());//debug
                return Curriculum.getACurriculum(rs).getStudentNumber()>=30;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * 判断课程是否存在
     * @param course
     * @return
     */
    public boolean isCourseExistence(Curriculum course){
        String sql="select * from curriculum where course_id=%s";
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(String.format(sql,course.getId()));
            if (rs.next()) {
                System.out.println("course"+course.getName()+"存在");//debug
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    /**
     * 添加课程
     * @param course
     * @return
     */
    public boolean addCourse(Curriculum course){
        if(!isCourseSelected(course)&&!isFullPerson(course)&&isCourseExistence(course)) {
            addTotalCredit(course);
            addCourseNumber(course);
            addStudentToCourseId(getId(), course.getId());
            return true;
        }
        return false;
    }

    /**
     * 删除课程
     * @param course
     * @return
     */
    public boolean delCourse(Curriculum course){
        if(isCourseSelected(course)){
            delCourseNumber(course);
            delTotalCredit(course);
            delStudentToCourseId(getId(),course.getId());
            return true;
        }
        return false;
    }


    /**
     * 学生增加课程时，添加对应的student_course
     * @param studentID
     * @param courseID
     */
    public void addStudentToCourseId(String studentID,String courseID){
       try{
            String sql = "insert into student_course(student_id,course_id) values(%s,%s)";
           System.out.println("插入学生id："+studentID);
           PreparedStatement psmt = con.prepareStatement(String.format(sql, studentID, courseID));
           //执行SQL语句
           psmt.executeUpdate(String.format(sql, studentID, courseID));
        } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }


    /**
     * 学生删除课程时
     * 删掉对应的student_course表中的信息
     * @param studentID
     * @param courseID
     */
    public void delStudentToCourseId(String studentID,String courseID){
//        try{
//            String sql = "insert into student_course(student_id,course_id) values(" + studentID + "," + courseID + ")";
//            PreparedStatement psmt = DBUtil.getConnection().prepareCall(sql);
//            psmt.execute();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        try{
            var sql = "delete from student_course where student_id=%s and course_id=%s";
            PreparedStatement psmt = con.prepareStatement(String.format(sql, studentID, courseID));
            //执行SQL语句
            psmt.executeUpdate(String.format(sql, studentID, courseID));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    /**
     * 添加课程时，对应的课程数加一
     * @param course
     */
    public void addCourseNumber(Curriculum course){

        try{
            Statement statement = con.createStatement(); //2.创建statement类对象，用来执行SQL语句！！
            String sql1 = "UPDATE curriculum SET student_number='" + (course.getStudentNumber()+ 1)+
            "' WHERE course_id='" + course.getId() + "'";//要执行的SQL语句
            statement.executeUpdate(String.format(sql1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 学生删除课程时，课程人数减一
     * @param course
     */
    public void delCourseNumber(Curriculum course){

        try{
            Statement statement = con.createStatement(); //2.创建statement类对象，用来执行SQL语句！！
            String sql1 = "UPDATE curriculum SET student_number='" + (course.getStudentNumber()-1)+
                    "' WHERE course_id='" + course.getId() + "'";//要执行的SQL语句
            statement.executeUpdate(String.format(sql1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 添加课程时增加相应的学分
     * @param course
     */
    public void addTotalCredit(Curriculum course){
        try{
            Statement statement = con.createStatement();
            String sql = "update student set total_credit='" + (getTotalCredit() + course.getCredit()) + "' where student_id='" + getId() + "'";
            statement.executeUpdate(String.format(sql));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * 删除课程时减去相应的学分
     * @param course
     */
    public void delTotalCredit(Curriculum course){
        try{
            Statement statement = con.createStatement();
            String sql = "update student set total_credit='" + (getTotalCredit() - course.getCredit()) + "' where student_id='" + getId() + "'";
            statement.executeUpdate(String.format(sql));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    /**
     * 得到自己的所有的课程信息
     * @return
     */
    public List<Curriculum> getAllMyCourse(){
        List<Curriculum> courses= new ArrayList<>();
//        String sql="select course_id from student_course where student_id='"+getId()+"'";
//        String sql2="SELECT * from curriculum as c " +
//                "RIGHT JOIN student_course as s " +
//                "ON "+"c.student_id="+getId()+ "and c.course_id=s.course_id";
        String sql1="SELECT * from curriculum as c inner JOIN student_course as s ON s.student_id="+getId()+"  and s.course_id=c.course_id";
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(String.format(sql1));
            while (rs.next()) {
                courses.add(Curriculum.getACurriculum(rs));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courses;

    }



    /**
     * 得到一个学生的全部信息
     * @param rs
     * @return
     */
    public static Student getAStudent(ResultSet rs)  {
        Student student=new Student();

        try{
            student.setId(rs.getString("student_id"));
            student.setName(rs.getString("student_name"));
            student.setPassword(rs.getString("student_password"));
            student.setGrade(rs.getString("student_grade"));
            student.setCourseNumber(rs.getInt("course_number"));
            student.setTotalCredit(rs.getDouble("total_credit"));

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return student;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", courseNumber=" + courseNumber +
                ", totalCredit=" + totalCredit +
                ", curriculumList=" + curriculumList +
                '}';
    }
}
