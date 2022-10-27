package cn.edu.ncu.yang.entity;

import cn.edu.ncu.yang.db.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 课程类：
 * 课程编号 id
 * 课程名称，name
 * 课程性质，必修/选修  obligatory/optional
 * 学时，授课学时，实验或上机学时， teaching_hours/experimental_hours
 * 学分，credit
 * 开课学期  opening_semester
 *
 */

public class Curriculum {
    //创建数据库连接
    private static Connection con=DBUtil.getConnection();
    //课程id
    private String id;
    //课程名
    private String name;
    //课程性质
    private String nature;
    //学时
    private int classHours;
    //学分
    private double credit;
    //开课学期
    private String openingSemester;
    //学生人数
    private int studentNumber;
    //任课老师
    private String teacherName;


    public Curriculum() {

    }

    public Curriculum(String id) {
        this.id = id;
    }

    public Curriculum(String id, String name, String nature, int classHours, double credit, String openingSemester, String teacherName) {
        this.id = id;
        this.name = name;
        this.nature = nature;
        this.classHours = classHours;
        this.credit = credit;
        this.openingSemester = openingSemester;
        this.studentNumber=0;
        this.teacherName=teacherName;
    }


    /**
     * 查找课程
     * @param info
     * @return
     */
    public static  Curriculum findCourse(String info){
      Curriculum course= new Curriculum();
        course=findCourseByID(info);
        if(course==null){
           course=findCourseByTeacherName(info);
           if(course==null){
               course=findCourseByCourseName(info);
               if(course==null){
                   course=findCourseByCourseCredit(info);
                   if(course==null){
                       course=findCourseByCourseNature(info);
                   }
               }
           }
        }


        return course;
    }

    /**
     * 查找符合信息的所有课程
     * @param info
     * @return
     */
    public static List<Curriculum> findCourses(String info){
        List<Curriculum> courses=new ArrayList<>();
        if((findCourseByID(info))!=null)courses.add(findCourseByID(info));
        if(findCourseByCourseName(info)!=null)courses.add(findCourseByCourseName(info));
        if(findCourseByTeacherName(info)!=null)courses.add(findCourseByTeacherName(info));
        if(findCourseByCourseCredit(info)!=null)courses.add(findCourseByCourseCredit(info));
        if(findCourseByCourseNature(info)!=null)courses.add(findCourseByCourseNature(info));
        return courses;
    }

    /**
     * 查找课程通过课程编号
     * @param id
     * @return
     */
    public static Curriculum findCourseByID(String id){

        String sql="select * from curriculum where course_id='"+id+"'";//课程id
        try {
            Statement stmt= con.createStatement();

            ResultSet rsId=stmt.executeQuery(String.format(sql));
            if(rsId.next()) {
                System.out.println("找到了课程："+id);
                return getACurriculum(rsId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    /**
     * 根据courseName查找课程
     * @param courseName
     * @return
     */
    public static Curriculum findCourseByCourseName(String courseName) {
        String sqlID = "select * from curriculum where course_name='" + courseName + "'";//课程名
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(String.format(sqlID));
            if (rs.next()) {
                System.out.println("找到了课程："+courseName);
                return getACurriculum(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 根据teacherName查找
     * @param teacherName
     * @return
     */
    public static Curriculum findCourseByTeacherName(String teacherName) {
        String sql="select * from curriculum where teacher_name='"+teacherName+"'";//授课老师
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(String.format(sql));
            if (rs.next()) {
                return getACurriculum(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Curriculum findCourseByCourseCredit(String courseCredit) {
        String sql="select * from curriculum where course_credit='"+courseCredit+"'";//课程学分
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(String.format(sql));
            if (rs.next()) {
                return getACurriculum(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }



    public static Curriculum findCourseByCourseNature(String courseNature) {
        String sql="select * from curriculum where course_nature='"+courseNature+"'";//课程性质
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(String.format(sql));
            if (rs.next()) {
                System.out.println("找到了课程："+courseNature);
                return getACurriculum(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    /**
     * 查找所有课程
     * @return
     */
    public  static List<Curriculum> getAllCourse(){
        List<Curriculum> courses = new ArrayList<>();

        String sql="select * from curriculum";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(String.format(sql));
            System.out.println("okgetAllCourse");
            System.out.println(courses==null);
//            System.out.println(rs.next());
            while (rs.next()) {

                //用来测试是否执行到这里了
                System.out.println((courses.add(Curriculum.getACurriculum(rs))));
//                System.out.println("ok2");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(courses.isEmpty());
        return courses;

    }





    //根据找到的ResultSet封装成课程
    public static Curriculum getACurriculum(ResultSet rs)  {
        Curriculum course= new Curriculum();

        try{
            course.setId(rs.getString("course_id"));
            course.setName(rs.getString("course_name"));
            course.setNature(rs.getString("course_nature"));
            course.setClassHours(rs.getInt("course_hours"));
            course.setCredit(rs.getDouble("course_credit"));
            course.setTeacherName(rs.getString("teacher_name"));
            course.setOpeningSemester(rs.getString("opening_semester"));
            course.setStudentNumber(rs.getInt("student_number"));

            System.out.println("ok");

            }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(course.toString());
        return course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getClassHours() {
        return classHours;
    }

    public void setClassHours(int classHours) {
        this.classHours = classHours;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getOpeningSemester() {
        return openingSemester;
    }

    public void setOpeningSemester(String openingSemester) {
        this.openingSemester = openingSemester;
    }



    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNUmber) {
        this.studentNumber = studentNUmber;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nature='" + nature + '\'' +
                ", classHours=" + classHours +
                ", credit=" + credit +
                ", openingSemester='" + openingSemester + '\'' +
                ", studentNumber=" + studentNumber +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curriculum that = (Curriculum) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
