package quest;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;


public class QuestController {

    @FXML
    ListView<String> list;
    @FXML
    Label description;
    @FXML
    Label head;
    @FXML
    Button newQuest;
    @FXML
    Button getNew_or_deleteOld;

    private Alert a = new Alert(Alert.AlertType.NONE);
    private ObservableList<String> quests = FXCollections.observableArrayList();
    private ObservableList<String> newQuests = FXCollections.observableArrayList();
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private int iter = 0;
    private int personId;
    private int char_id;
    private List<Integer> rs_id = null;
    private String rs_name = null;
    private String new_quest;
    private int selected_quest_id;

    public void setPersonId(int id)
    {
        this.personId = id;
    }

    public void initId(int c_id, int p_id)
    {
        this.char_id = c_id;
        this.personId = p_id;
        getQuests(c_id);
    }

    public void getQuests(int id) {
        iter = 0;
        quests.clear();
        try {
            databaseConnector.DatabseInit();
            rs_id = databaseConnector.getQuests(id);

            for (Integer i: rs_id) {
                iter++;
                rs_name = databaseConnector.getNameOfQuest(i);
                quests.add(rs_name);
            }
            if (iter == 0) {
                quests.add("There are no active quests.");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        list.setItems(quests);
    }

    public void showQuestDetail() {
        ResultSet newResultSet;
        try {
            String desc = databaseConnector.getQuestInfo(list.getSelectionModel().getSelectedItem());
            selected_quest_id = databaseConnector.getQuestID(list.getSelectionModel().getSelectedItem());
            if (desc != null) {
                description.setText("Description: \n" + desc);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void goBack(javafx.event.ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menu/main_screen.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);

            MenuController controller = loader.getController();
            controller.initData(personId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void getNewQuest(javafx.event.ActionEvent event) {
        if (iter > 3) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You have maximum number of quest!");
            a.show();
            return;
        }
        getNew_or_deleteOld.setText("Take that!");
        head.setText("Choose a new quest");
        if (newQuests.size() < 2) {
            for (int i = 0; i < 3; i++) {
                Random r = new Random();
                int randomQ = r.nextInt(7500);

                try {
                    new_quest = databaseConnector.getNameOfQuest(randomQ);
                    newQuests.add(new_quest);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        list.setItems(newQuests);
    }

    public void showActive() {
        description.setText("");
        getNew_or_deleteOld.setText("Get rid of this!");
        getQuests(char_id);
    }

    public void getNew_or_deleteOld() {
        if (selected_quest_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose a quest!");
            a.show();
            return;
        }

        // ziskanie noveho
        if (getNew_or_deleteOld.getText().equals("Take that!")) {
            try {
                if(databaseConnector.addQuest(selected_quest_id, char_id) == -1) {
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText("You already have this quest!");
                    a.show();
                    return;
                }
                description.setText("");
                showActive();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        // zbavenie sa stareho, strata 10% aktualnych XP, aktualizacia databazy
        else {
            try {
                databaseConnector.deleteQuest(selected_quest_id, char_id);
            } catch (SQLException e) {
                System.out.println(e);
            }
            try {
                databaseConnector.decreaseXP(char_id);
                description.setText("");
                showActive();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
}


// TODO questov je vela a volaju sa rovnako, treba ich cislovat