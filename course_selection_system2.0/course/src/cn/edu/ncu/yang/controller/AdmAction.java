package cn.edu.ncu.yang.controller;

import cn.edu.ncu.yang.db.DBUtil;
import cn.edu.ncu.yang.entity.Adm;
import cn.edu.ncu.yang.entity.Curriculum;
import cn.edu.ncu.yang.entity.Student;
import cn.edu.ncu.yang.view.LoginAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AdmAction {
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn courseId;
    @FXML
    private TableColumn courseTeacher;
    @FXML
    private TableColumn courseCredit;
    @FXML
    private TableColumn classNumber;
    @FXML
    private TableColumn classHours;
    @FXML
    private TableColumn openingTerm;
    @FXML
    private TableColumn courseName;
    @FXML
    private TableColumn courseNature;

    @FXML
    private TextField idTxt;
    @FXML
    private TextField hoursTxt;
    @FXML
    private TextField teacherTxt;
    @FXML
    private TextField creditTxt;
    @FXML
    private TextField termTxt;
    @FXML
    private TextField natureTxt;
    @FXML
    private  TextField nameTxt;

    @FXML
    private TextField delTxt;

    private Adm adm ;



    public void initialAdm(){
        adm=LoginAction.adm;
    }

    /**
     * 显示所有课程
     */
    @FXML
    public void showAllCourse(){
        ObservableList<Curriculum> list = FXCollections.observableArrayList();
        List<Curriculum> allCourses=Curriculum.getAllCourse();
        showCourses(list, allCourses);
    }

    public void showCourses(ObservableList<Curriculum> list, List<Curriculum> courses){
        //每次显示清空一次列表
        list.clear();
        for (Curriculum course : courses) {

            list.add(course);  //list添加值对象
        }
        courseId.setCellValueFactory(new PropertyValueFactory("id"));
        courseTeacher.setCellValueFactory(new PropertyValueFactory("teacherName"));
        classHours.setCellValueFactory(new PropertyValueFactory("classHours"));
        courseNature.setCellValueFactory(new PropertyValueFactory("nature"));
        courseCredit.setCellValueFactory(new PropertyValueFactory("credit"));
        openingTerm.setCellValueFactory(new PropertyValueFactory("openingSemester"));
        classNumber.setCellValueFactory(new PropertyValueFactory("studentNumber"));
        courseName.setCellValueFactory(new PropertyValueFactory("name"));

        tableView.setItems(list);
    }


    /**
     * 添加课程
     */
    @FXML
    public void addCourse(){
        initialAdm();
        String id=idTxt.getText();
        String hours=hoursTxt.getText();
        String teacher = teacherTxt.getText();
        String credit = creditTxt.getText();
        String term = termTxt.getText();
        String nature = natureTxt.getText();
        String name=nameTxt.getText();
        System.out.println(Integer.parseInt(hours));
        adm.addCourse(new Curriculum(id,name,nature,Integer.parseInt(hours),Double.parseDouble(credit),term,teacher));

        showMessage("添加课程成功");
    }

    /**
     * 删除课程
     */
    @FXML
    public void delCourse(){
        initialAdm();
        String id=delTxt.getText();
//        Curriculum course=Curriculum.findCourse(String.format(id));
//        System.out.println(course==null);
        System.out.println(id);
        if(!adm.delCourse(id)){
            showMessage("删除失败");
        }else{
            showMessage("删除成功");
        }


    }

    public void showMessage(String info){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
