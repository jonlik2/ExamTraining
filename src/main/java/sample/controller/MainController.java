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

    public MainController() {
        repository = Repository.getInstance();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        initChoiceBox();
        initButtons();
    }

    private void initChoiceBox() {
        chbTask.setItems(FXCollections.observableArrayList(getTitlesTask()));
        chbTask.getSelectionModel().selectFirst();

        chbTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Выберите задание")) {
                textQuestion.setText("");
                btnAnswer.setDisable(true);
                btnNext.setDisable(true);
            } else {
                currentNumberOfTask = getNumberTask(newValue);
                currentNumberOfVariant = 1;
                currentNum.setText(String.valueOf(currentNumberOfVariant));
                Task task = repository.getTaskByNumber(getNumberTask(newValue));
                allNum.setText(String.valueOf(task.getVariants().size()));
                textQuestion.setText(task.getVariants().get(currentNumberOfVariant - 1).getQuestion());
                btnNext.setDisable(false);
                btnAnswer.setDisable(false);
                textResult.setText("");
            }
        });
    }

    private void initButtons() {
        btnAnswer.setDisable(true);
        btnNext.setDisable(true);

        btnAnswer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkAnswer());
        btnNext.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> nextVariant());
    }

    private void checkAnswer() {
        String answer = textAnswer.getText();
        String originalAnswer = repository.getTaskByNumber(currentNumberOfTask).getVariants().get(currentNumberOfVariant - 1).getAnswer();
        if (answer.equals(originalAnswer)) {
            textResult.setText("Правильный ответ");
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
            currentNumberOfVariant = 1;
            textQuestion.setText("");
            textResult.setText("Вы закончили данное задание. Выберите другое");
            btnNext.setDisable(true);
            btnAnswer.setDisable(true);
        }
        textAnswer.setText("");
    }

    private boolean checkNumberOfVariant() {
        return currentNumberOfVariant <= repository.getTaskByNumber(currentNumberOfTask).getVariants().size() - 1;
    }

    private int getNumberTask(String titleTask) {
        return Integer.parseInt(titleTask.split("№")[1]);
    }

    private List<String> getTitlesTask() {
        List<String> list = new ArrayList<String>();
        list.add("Выберите задание");
        for (Task task : repository.getTasks()) {
            list.add(String.format("Задание №%d", task.getNumber()));
        }
        return list;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
