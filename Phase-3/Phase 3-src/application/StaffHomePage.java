package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import QuestionAnswerSystem.QASystemDatabase;
import QuestionAnswerSystem.Answer;
import QuestionAnswerSystem.Question;

import java.util.List;

public class StaffHomePage extends Application {

    private QASystemDatabase database;

    @Override
    public void start(Stage stage) {
        database = QASystemDatabase.getInstance();

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");

        List<Question> questionList = database.getQuestions();
        for (Question q : questionList) {
            VBox questionBox = new VBox(5);
            questionBox.setStyle("-fx-border-color: gray; -fx-padding: 10;");
            Label qLabel = new Label("Q: " + q.getInput());
            questionBox.getChildren().add(qLabel);

            List<Answer> answers = database.getAnswersbyQuestion(q);
            for (Answer a : answers) {
                VBox answerBox = new VBox(4);
                answerBox.setStyle("-fx-border-color: lightgray; -fx-background-color: #f9f9f9; -fx-padding: 5;");

                Label aLabel = new Label("A: " + a.getInput());
                Label userLabel = new Label("By User: " + a.getUserName());

                Button deleteBtn = new Button("Delete Answer");
                deleteBtn.setOnAction(e -> {
                    try {
                        int aid = database.getAID(a);
                        database.deleteStaffCommentsByAnswerId(aid); // delete linked staff comments
                        database.deleteAnswer(a);
                        refresh(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                TextField commentField = new TextField();
                commentField.setPromptText("Add staff comment...");

                Button commentBtn = new Button("Comment");
                commentBtn.setOnAction(e -> {
                    try {
                        int aid = database.getAID(a);
                        database.addStaffComment(aid, commentField.getText());
                        commentField.clear();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                CheckBox flagUserCheckbox = new CheckBox();
                boolean alreadyFlagged = database.isUserFlagged(a.getUserName());
                flagUserCheckbox.setSelected(alreadyFlagged);
                flagUserCheckbox.setText(alreadyFlagged ? "Unflag this user" : "Flag this user");

                flagUserCheckbox.setOnAction(e -> {
                    if (flagUserCheckbox.isSelected()) {
                        database.flagUser(a.getUserName());
                        flagUserCheckbox.setText("Unflag this user");
                    } else {
                        database.unflagUser(a.getUserName());
                        flagUserCheckbox.setText("Flag this user");
                    }
                });


                answerBox.getChildren().addAll(aLabel, userLabel, deleteBtn, commentField, commentBtn, flagUserCheckbox);
                questionBox.getChildren().add(answerBox);
            }

            layout.getChildren().add(questionBox);
        }

        ScrollPane scrollPane = new ScrollPane(layout);
        Scene scene = new Scene(scrollPane, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Staff Home Page");
        stage.show();
    }

    private void refresh(Stage stage) {
        start(stage);  // Redraws the page after delete
    }

    public static void main(String[] args) {
        launch(args);
    }
}