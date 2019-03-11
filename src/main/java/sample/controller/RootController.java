package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sample.Main;
import sample.model.Repository;
import sample.model.Task;

import java.util.List;
import java.util.prefs.Preferences;

public class RootController {

    private Main mainApp;

    private Repository repository;

    public RootController() {
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

    @FXML
    private void handleReset() {
        Preferences preferences = Preferences.userRoot().node("ExamApp");
        Preferences prefs = preferences.node("tasks");
        List<Task> tasks = repository.getTasks();
        for (Task task : tasks) {
            Preferences node = prefs.node(String.valueOf(task.getNumber()));
            node.put("variant", "1");
            node.put("score", "0");
        }
        prefs.put("current", "0");
    }
}
