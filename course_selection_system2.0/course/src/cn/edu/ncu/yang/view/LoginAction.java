package cn.edu.ncu.yang.view;

import cn.edu.ncu.yang.entity.Adm;
import cn.edu.ncu.yang.entity.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginAction {

    public static Adm adm=null;
    public static Student student=null;

    @FXML
    private TextField idTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Button login_registerBt;

    @FXML
    private ToggleGroup selectID;

    @FXML
    private RadioButton stuRB;
    @FXML
    private RadioButton admRB;





    private Student stu;
    private String sql_id="select * from curriculum where course_id='id'";

    @FXML
    public void Login() throws IOException {
        var id=idTxt.getText();
        var password=passwordTxt.getText();
        if(stuRB.isSelected()){
            studentLogin(id,password);
            AnchorPane root = FXMLLoader.load(getClass().getResource("student.fxml"));
            var scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("学生登录");
            stage.show();
        }
        else if(admRB.isSelected()){
            admLogin(id, password);
            AnchorPane root = FXMLLoader.load(getClass().getResource("adm.fxml"));
            var scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("管理员登录");
            stage.show();
        }

    }

    public boolean studentLogin(String id,String password){
       this.student=Student.Login(id,password);
       return this.student!=null;

    }

    public boolean admLogin(String id,String password){
        this.adm=Adm.Login(id,password);
        return this.adm!=null;
    }



}
