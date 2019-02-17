package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sample.Main;
import sample.model.Repository;

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
