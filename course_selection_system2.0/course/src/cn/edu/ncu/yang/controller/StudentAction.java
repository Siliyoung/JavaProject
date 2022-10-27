package cn.edu.ncu.yang.controller;

import cn.edu.ncu.yang.db.DBUtil;
import cn.edu.ncu.yang.entity.Curriculum;
import cn.edu.ncu.yang.entity.Student;
import cn.edu.ncu.yang.view.LoginAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**\
 * 查看所有课程
 * 查看我的课程
 * 查询课程
 * 添加课程
 * 删除课程
 * 查看我的信息
 */
public class StudentAction {
    @FXML
    private TextField infoTxt;
    private Button button1;
    private Button button2;

    @FXML
    private TableColumn<Curriculum,String> course_id;
    @FXML
    private TableColumn<Curriculum,String> course_teacher;
    @FXML
    private TableColumn<Curriculum,Integer> class_hours;
    @FXML
    private TableColumn<Curriculum,String> course_nature;
    @FXML
    private TableColumn<Curriculum,Double> course_credit;
    @FXML
    private TableColumn<Curriculum,String> opening_term;
    @FXML
    private TableColumn<Curriculum,Integer> student_number;
    @FXML
    private TableColumn<Curriculum,String> course_name;
    @FXML
    private TableColumn<Curriculum,String> add;
    @FXML
    private TableColumn<Curriculum,String> del;

    @FXML
    private TextField id_m;
    @FXML
    private TextField name_m;
    @FXML
    private TextField garde_m;
    @FXML
    private TextField credit_m;
    @FXML
    private TextField number_m;


    @FXML
    private TableView tableView;
    private Student stu;



    public void  initialStudent(){
        String sql ="select * from student where student_id=%s";
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(String.format(sql,LoginAction.student.getId()));
            if (rs.next()) {
                stu=Student.getAStudent(rs);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * 显示所有课程
     */
    @FXML
    public void showAllCourse(){
        initialStudent();

        List<Curriculum> allCourses=Curriculum.getAllCourse();
        show(allCourses);
//        ObservableList<Curriculum> list = FXCollections.observableArrayList();
//        showCourses(list, allCourses);
    }

    /**
     * 显示我的课程
     */
    @FXML
    public void showMyCourse(){
        initialStudent();
        List<Curriculum> allCourses=stu.getAllMyCourse();
        show(allCourses);
//        ObservableList<Curriculum> list = FXCollections.observableArrayList();
//        showCourses(list, allCourses);
    }

    /**
     * 添加课程
     */
    public void addCourse(Curriculum course){
        initialStudent();
       if(stu.addCourse(course)) {
           showMessage("选课成功");
       }
       else{
           showMessage("选课失败");
       }
       if(!isCreditEnough()){
            showMessage("总学分未修满15分！");
       }
    }


    /**
     * 退课
     */
    public void delCourse(Curriculum course){
        initialStudent();

        if(stu.delCourse(course)) {
            showMessage("退课成功");
        }
        else{
            showMessage("退课失败");
        }
        if(!isCreditEnough()){
            showMessage("目前修读总学分为："+stu.getTotalCredit()+",总学分未修满15分！");
        }
    }

    @FXML
    public void findCourses(){
        String info =infoTxt.getText();
        initialStudent();
        List<Curriculum> allCourses=Curriculum.findCourses(info);
        show(allCourses);
//
//        ObservableList<Curriculum> list = FXCollections.observableArrayList();
//        showCourses(list, allCourses);
    }

    @FXML
    public void editInfo(){

        garde_m.setEditable(true);
        number_m.setEditable(true);
    }

    /**
     * 修改班级，姓名
     */
    @FXML
    public void confirmInfo(){
        initialStudent();
        String garde=garde_m.getText();
        String name=name_m.getText();
        String sql="update student set student_name='%s',student_grade='%s' where student_id='%s'";
        try{
            Statement statement = DBUtil.getConnection().createStatement(); //2.创建statement类对象，用来执行SQL语句！！

            statement.executeUpdate(String.format(sql,name,garde,stu.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showMessage("修改失败");
        }
        showMessage("修改成功");
        garde_m.setEditable(false);
        name_m.setEditable(false);

    }

    @FXML
    public void showMyInfo(){
        initialStudent();
        garde_m.setText(stu.getGrade());
        name_m.setText(stu.getName());
        id_m.setText(stu.getId());
        number_m.setText(Integer.toString(stu.getCourseNumber()));
        credit_m.setText(Double.toString(stu.getTotalCredit()));
    }




    public void showCourses(ObservableList<Curriculum> list,List<Curriculum> courses){
        //每次显示清空一次列表
        list.clear();
        for (Curriculum course : courses) {

            list.add(course);  //list添加值对象
        }
        course_id.setCellValueFactory(new PropertyValueFactory("id"));
        course_teacher.setCellValueFactory(new PropertyValueFactory("teacherName"));
        class_hours.setCellValueFactory(new PropertyValueFactory("classHours"));
        course_nature.setCellValueFactory(new PropertyValueFactory("nature"));
        course_credit.setCellValueFactory(new PropertyValueFactory("credit"));
        opening_term.setCellValueFactory(new PropertyValueFactory("openingSemester"));
        student_number.setCellValueFactory(new PropertyValueFactory("studentNumber"));
        course_name.setCellValueFactory(new PropertyValueFactory("name"));

        tableView.setItems(list);
    }





    public boolean isCreditEnough(){
        return stu.getTotalCredit()>=15;
    }

    public void showMessage(String info){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(info);
        alert.showAndWait();
    }


    public void show( List<Curriculum> courses){
        ObservableList<Curriculum> list = FXCollections.observableArrayList();


        //每次显示清空一次列表
        list.clear();
        for (Curriculum course : courses) {

            list.add(course);  //list添加值对象
        }
        course_id.setCellValueFactory(new PropertyValueFactory("id"));
        course_teacher.setCellValueFactory(new PropertyValueFactory("teacherName"));
        class_hours.setCellValueFactory(new PropertyValueFactory("classHours"));
        course_nature.setCellValueFactory(new PropertyValueFactory("nature"));
        course_credit.setCellValueFactory(new PropertyValueFactory("credit"));
        opening_term.setCellValueFactory(new PropertyValueFactory("openingSemester"));
        student_number.setCellValueFactory(new PropertyValueFactory("studentNumber"));
        course_name.setCellValueFactory(new PropertyValueFactory("name"));

        //添加按钮进列表
        add.setCellFactory((col)->{

                    //UserLoad换成你自己的实体名称
                    TableCell<Curriculum, String> cell = new TableCell<Curriculum, String>(){
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            button1 = new Button("选课");
                            button1.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");

                            button1.setOnMouseClicked((col) -> {

                                //获取list列表中的位置，进而获取列表对应的信息数据
                                Curriculum course1 = list.get(getIndex());
                                //按钮事件自己添加
                                addCourse(course1);

                            });

                            if (empty) {
                                //如果此列为空默认不添加元素
                                setText(null);
                                setGraphic(null);
                            } else {
                                this.setGraphic(button1);
                            }
                        }
                    };
                    return cell;
                }
        );

        del.setCellFactory((col)->{
                    TableCell<Curriculum, String> cell = new TableCell<Curriculum, String>(){

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            //按钮显示文字
                            button2 = new Button("退课");
                            //设置按钮颜色
                            button2.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                            //按钮点击事件
                            button2.setOnMouseClicked((col) -> {
                                //获取list列表中的位置，进而获取列表对应的信息数据
                                Curriculum course2 = list.get(getIndex());
                                //按钮事件自己添加
                                delCourse(course2);
                            });

                            if (empty) {
                                //如果此列为空默认不添加元素
                                setText(null);
                                setGraphic(null);
                            } else {
                                //加载按钮
                                this.setGraphic(button2);
                            }
                        }



                    };
                    return cell;
                }
        );
        tableView.setItems(list);

    }





}
