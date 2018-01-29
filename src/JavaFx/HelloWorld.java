package JavaFx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloWorld extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //1.登录 取消按钮
        Button button1=new Button("登录");
        button1.setPrefWidth(80);//设置按钮宽
        button1.setPrefHeight(36);//设置按钮高
        Button button2=new Button("取消");
        button2.setPrefWidth(80);//设置按钮宽
        button2.setPrefHeight(36);//设置按钮高
        BorderPane borderPane=new BorderPane();
        //2.HBox
        HBox hbox1=new HBox();
        hbox1.setPadding(new Insets(10,10,30,70));
        hbox1.setSpacing(100);//设置按钮之间的间隔
        hbox1.getChildren().addAll(button1,button2);
        Scene scene=new Scene(borderPane,400,240);//设置窗体大小
        //3.HBox布局
        borderPane.setBottom(hbox1);
        //4.创建创建两个label
        Label label1=new Label("账号");
        Label label2=new Label("密码");
        //5.创建账号框
        TextArea userName=new TextArea();
        PasswordField password=new PasswordField();
        //6.创建VBox 并且组合到一起
        VBox vBox1=new VBox();
        VBox vBox2=new VBox();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
