package sample.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import sample.Main;
import sample.model.Repository;
import sample.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class StatController {

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Label allAnswerText;

    @FXML
    private Label trueAnswerText;

    private Main mainApp;

    private Repository repository;

    public StatController() {
        repository = Repository.getInstance();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        initChoiceBox();
    }

    private void initChoiceBox() {
        choiceBox.setItems(FXCollections.observableArrayList(getTitlesTask()));
        choiceBox.getSelectionModel().selectFirst();

        choiceBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals("Выберите задание")) {
                resetUI();
            } else {
                initUI(getNumberTask(String.valueOf(newValue)));
            }
        }));
    }

    private void resetUI() {
        draw(0, 0, 0.0);
    }

    private void initUI(int numberTask) {
        Preferences prefs = Preferences.userRoot().node("ExamApp").node("tasks");

        int currentNumberOfVariant = Integer.parseInt(prefs.node(String.valueOf(numberTask)).get("variant", null));
        int score = Integer.parseInt(prefs.node(String.valueOf(numberTask)).get("score", null));

        draw(currentNumberOfVariant, score, (double) score / currentNumberOfVariant);
    }

    private void draw(int currentNumberOfVariant, int score, double progress) {
        allAnswerText.setText(String.valueOf(currentNumberOfVariant));
        trueAnswerText.setText(String.valueOf(score));
        progressIndicator.setProgress(progress);
        //progressIndicator.setStyle(String.format("-fx-accent: %s;", colorSelection(progress)));
        progressIndicator.setStyle(String.format("-fx-progress-color: %s;", colorSelection(progress)));
    }

    private String colorSelection(double progress) {
        if (progress >= 0 && progress <= 0.25) {
            return "darkred";
        } else if (progress > 0.25 && progress <= 0.5) {
            return "chocolate";
        } else if (progress > 0.5 && progress <= 0.75) {
            return "orange";
        } else if (progress >= 0.75) {
            return "green";
        }
        return null;
    }

    private List<String> getTitlesTask() {
        List<String> list = new ArrayList<>();
        list.add("Выберите задание");
        for (Task task : repository.getTasks()) {
            list.add(String.format("Задание №%d", task.getNumber()));
        }
        return list;
    }

    private int getNumberTask(String titleTask) {
        return Integer.parseInt(titleTask.split("№")[1]);
    }
}
