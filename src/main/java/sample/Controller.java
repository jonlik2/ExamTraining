package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
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
    private Button btnSkip;

    private Main mainApp;

    private Repository repository;
    private int numberTask;

    public Controller() {
        repository = new Repository();
    }

    @FXML
    private void initialize() {
        chbTask.setItems(FXCollections.observableArrayList(getTitlesTask()));

        chbTask.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                numberTask = getNumberTask(newValue);
                Task task = repository.getTaskByNumber(getNumberTask(newValue));
                textQuestion.setText(task.getVariants().get(0).getQuestion());
            }
        });

        btnAnswer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                String answer = textAnswer.getText();
                String originalAnswer = repository.getTaskByNumber(numberTask).getVariants().get(0).getAnswer();
                if (answer.equals(originalAnswer)) {
                    showAlert("YES");
                } else {
                    showAlert("NO");
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int getNumberTask(String titleTask) {
        return Integer.parseInt(titleTask.split("№")[1]);
    }

    private List<String> getTitlesTask() {
        List<String> list = new ArrayList<String>();
        for (Task task : repository.getTasks()) {
            list.add(String.format("Задание №%d", task.getNumber()));
        }
        return list;
    }


}
