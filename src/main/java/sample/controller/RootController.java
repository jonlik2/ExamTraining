package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Repository;

import java.io.IOException;

public class RootController {

    @FXML
    private Label menuStat;

    private Main mainApp;

    private Repository repository;

    public RootController(){
        repository = Repository.getInstance();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleStat() {
        mainApp.initStatLayout();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam Training");
        alert.setHeaderText("Программа для подготовки к ГИА");
        alert.setContentText("Автор: Барболин Семен\nс. Кичменгский Городок");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
