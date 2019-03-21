package sample.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sample.Main;
import sample.model.Repository;
import sample.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainController {

    @FXML
    private ChoiceBox<String> chbTask;

    @FXML
    private Label currentNum;

    @FXML
    private Label allNum;

    @FXML
    private Label textQuestion;

    @FXML
    private TextField textAnswer;

    @FXML
    private Button btnAnswer;

    @FXML
    private Button btnNext;

    @FXML
    private Label textResult;

    private Main mainApp;

    private Repository repository;

    private int currentNumberOfVariant;
    private int currentNumberOfTask;

    private int score;

    public MainController() {
        repository = Repository.getInstance();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public int getCurrentNumberOfVariant() {
        return currentNumberOfVariant;
    }

    public int getCurrentNumberOfTask() {
        return currentNumberOfTask;
    }

    public int getScore() {
        return score;
    }

    @FXML
    private void initialize() {
        initChoiceBox();
        initButtons();
    }

    private void initChoiceBox() {
        chbTask.setItems(FXCollections.observableArrayList(getTitlesTask()));

        Preferences prefs = Preferences.userRoot().node("ExamApp").node("tasks");

        currentNumberOfTask = Integer.parseInt(prefs.get("current", "0"));

        if (currentNumberOfTask != 0) {
            chbTask.getSelectionModel().select(getIndexOfTask(currentNumberOfTask) + 1);

            currentNumberOfVariant = Integer.parseInt(prefs.node(String.valueOf(currentNumberOfTask)).get("variant", null));
            score = Integer.parseInt(prefs.node(String.valueOf(currentNumberOfTask)).get("score", null));

            initUI();

            chbTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                saveStat(currentNumberOfTask, currentNumberOfVariant, score);

                if (newValue.equals("Выберите задание")) {
                    resetUI();
                } else {
                    currentNumberOfTask = getNumberOfTask(newValue);
                    currentNumberOfVariant = Integer.parseInt(prefs.node(String.valueOf(currentNumberOfTask)).get("variant", null));
                    score = Integer.parseInt(prefs.node(String.valueOf(currentNumberOfTask)).get("score", null));

                    initUI();
                }
            });

        } else {
            chbTask.getSelectionModel().selectFirst();

            chbTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                saveStat(currentNumberOfTask, currentNumberOfVariant, score);

                if (newValue.equals("Выберите задание")) {
                    resetUI();
                } else {
                    currentNumberOfTask = getNumberOfTask(newValue);
                    currentNumberOfVariant = 1;
                    score = 0;

                    initUI();
                }
            });
        }

    }

    private void initUI() {
        Task currentTask = repository.getTaskByNumber(currentNumberOfTask);
        currentNum.setText(String.valueOf(currentNumberOfVariant));
        allNum.setText(String.valueOf(currentTask.getVariants().size()));
        textQuestion.setText(currentTask.getVariants().get(currentNumberOfVariant - 1).getQuestion());
        textResult.setText("");
        btnAnswer.setDisable(false);
        btnNext.setDisable(false);
    }

    private void resetUI() {
        textQuestion.setText("");
        currentNum.setText("0");
        allNum.setText("0");
        btnAnswer.setDisable(true);
        btnNext.setDisable(true);
    }

    private void initButtons() {
        btnAnswer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkAnswer());
        btnNext.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> nextVariant());
    }

    private void checkAnswer() {
        String answer = textAnswer.getText().trim();
        String originalAnswer = repository.getTaskByNumber(currentNumberOfTask).getVariants().get(currentNumberOfVariant - 1).getAnswer();
        if (answer.equalsIgnoreCase(originalAnswer)) {
            textResult.setText("Правильный ответ");
            score++;
        } else {
            textResult.setText("Неправильный ответ");
        }
    }

    private void nextVariant() {
        if (checkNumberOfVariant()) {
            currentNumberOfVariant++;
            currentNum.setText(String.valueOf(currentNumberOfVariant));
            textQuestion.setText(repository.getTaskByNumber(currentNumberOfTask).getVariants().get(currentNumberOfVariant - 1).getQuestion());
            textResult.setText("");
        } else {
            textQuestion.setText("");
            textResult.setText("Вы закончили данное задание. Выберите другое!\nПравильных ответов: " + score);
            btnAnswer.setDisable(true);
            btnNext.setDisable(true);
        }
        textAnswer.setText("");
    }

    private boolean checkNumberOfVariant() {
        return currentNumberOfVariant <= repository.getTaskByNumber(currentNumberOfTask).getVariants().size() - 1;
    }

    private int getNumberOfTask(String titleTask) {
        return Integer.parseInt(titleTask.split("№")[1]);
    }

    private void saveStat(int task, int variant, int score) {
        Preferences prefs = Preferences.userRoot().node("ExamApp").node("tasks").node(String.valueOf(task));
        prefs.put("variant", String.valueOf(variant));
        prefs.put("score", String.valueOf(score));
    }

    private List<String> getTitlesTask() {
        List<String> list = new ArrayList<>();
        list.add("Выберите задание");
        for (Task task : repository.getTasks()) {
            list.add(String.format("Задание №%d", task.getNumber()));
        }
        return list;
    }

    private int getIndexOfTask(int numberOfTask) {
        List<Task> tasks = repository.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getNumber() == numberOfTask) {
                return i;
            }
        }
        return 0;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void reset() {
        chbTask.getSelectionModel().selectFirst();
        resetUI();
    }
}
