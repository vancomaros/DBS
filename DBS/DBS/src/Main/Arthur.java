package Main;

import Database.DatabaseConnector;
import com.github.javafaker.Faker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLOutput;


public class Arthur extends Application{

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{


        for (int i = 0; i < 1; i++) {
            DatabaseConnector dataCon = new DatabaseConnector();
            dataCon.DatabseInit();
            for (int j = 0; j < 1; j++) {
                Faker faker = new Faker();
                String name = faker.name().username();
                String password = faker.pokemon().name();
                String email = faker.internet().emailAddress();

//            System.out.println(name);
//            System.out.println(password);
//            System.out.println(email);

                int flag = dataCon.addUser(password, name, email);

                //System.out.println(flag);
            }
            dataCon.connectionClose();
        }

        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("../Login/sample.fxml"));
        primaryStage.setTitle("Arthur");
        primaryStage.setScene(new Scene(root, 635, 400));
        primaryStage.show();

    }

    public int loginUser(String username, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.DatabseInit();
        int flag = databaseConnector.checkUser(username, password);
        databaseConnector.connectionClose();
        return flag;
    }


    public void sceneChange(String fxml) throws Exception, NullPointerException{

    }


    public static void main(String[] args) {
        launch(args);
    }
}