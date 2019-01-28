package sample.controller;

import javafx.fxml.FXML;
import sample.Main;
import sample.model.Repository;

public class StatController {

    private Main mainApp;

    private Repository repository;

    public StatController() {
        repository = Repository.getInstance();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}
