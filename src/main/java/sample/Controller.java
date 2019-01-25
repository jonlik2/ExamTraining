package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sample.model.Repository;
import sample.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private ChoiceBox<String> chbTask;

    @FXML
    private Label currentNum;

    @FXML
    private Label allNum;

    @FXML
    private TextArea textQuestion;

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

    public Controller() {
        repository = new Repository();
    }

    @FXML
    private void initialize() {
        chbTask.setItems(FXCollections.observableArrayList(getTitlesTask()));
        chbTask.getSelectionModel().selectFirst();

        chbTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Выберите задание")) {
                textQuestion.setText("");
            } else {
                currentNumberOfTask = getNumberTask(newValue);
                currentNumberOfVariant = 1;
                currentNum.setText(String.valueOf(currentNumberOfVariant));
                Task task = repository.getTaskByNumber(getNumberTask(newValue));
                allNum.setText(String.valueOf(task.getVariants().size()));
                textQuestion.setText(task.getVariants().get(currentNumberOfVariant - 1).getQuestion());
            }
        });

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
            textAnswer.setText("");
        } else {
            currentNumberOfVariant = 1;
            textQuestion.setText("");
            textResult.setText("Вы закончили данное задание. Выберите другое");
            textAnswer.setText("");
        }
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
