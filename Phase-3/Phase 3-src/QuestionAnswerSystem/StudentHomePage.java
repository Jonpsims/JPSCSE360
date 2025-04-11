package QuestionAnswerSystem;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentHomePage {
    private boolean filter = false;
    private boolean canFilter = false;
    private boolean reviewer = false;
    private final QASystemDatabase QADatabase;
    private application.User user;

    public StudentHomePage(QASystemDatabase QADatabase, application.User user) {
        this.user = user;
        this.QADatabase = QADatabase;
    }

    public void show(Stage primaryStage) {
        String[] role = user.getRole().strip().split(",");
        for (String s : role) {
            if (s.strip().equals("reviewer")) reviewer = true;
        }

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.setFillWidth(true);

        Button sortByWeightButton = new Button("Sort Reviews by Trusted Reviewer Weight");
        sortByWeightButton.setOnAction(e -> updatePage(layout, true));
        layout.getChildren().add(sortByWeightButton);

        layout.getChildren().add(new Label("Questions:"));
        updatePage(layout, false);

        TextField postQuestion = new TextField();
        postQuestion.setPromptText("Write your Question here");
        postQuestion.setMaxWidth(Double.MAX_VALUE);

        Button submit = new Button("Submit");
        submit.setOnAction(a -> {
            String input = postQuestion.getText();
            try {
                QADatabase.addQuestion(new Question(user.getUserName(), input));
                updatePage(layout, false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        HBox questionInput = new HBox(10, postQuestion, submit);
        layout.getChildren().add(questionInput);

        CheckBox filterCheckbox = new CheckBox("Show only resolved answers");
        filterCheckbox.setOnAction(e -> {
            if (canFilter) {
                filter = filterCheckbox.isSelected();
                updatePage(layout, false);
            }
        });
        layout.getChildren().add(filterCheckbox);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        primaryStage.setScene(new Scene(scrollPane, 1000, 600));
        primaryStage.setTitle("Questions and Answers");
        primaryStage.show();
    }

    public void updatePage(VBox layout, boolean sortByWeight) {
        QADatabase.printAnswers(QADatabase.getAnswers());
        layout.getChildren().removeIf(node -> "questionDisplay".equals(node.getId()));

        VBox questionDisplay = new VBox(15);
        questionDisplay.setId("questionDisplay");

        ArrayList<Question> questions = QADatabase.getQuestions();

        for (Question q : questions) {
            VBox questionBox = new VBox(10);
            questionBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f0f0f0;");
            questionBox.setMaxWidth(Double.MAX_VALUE);

            Label ql = new Label("Question: " + q.getInput());
            Label ul = new Label("By User: " + q.getUserName());
            ql.setStyle("-fx-font-weight: bold;");
            ul.setStyle("-fx-font-weight: bold;");

            Button deleteQuestion = new Button("Delete this Question");
            deleteQuestion.setOnAction(e -> {
                try {
                    ArrayList<Answer> linkedAnswers = QADatabase.getAnswersbyQuestion(q);
                    for (Answer ans : linkedAnswers) {
                        int aid = QADatabase.getAID(ans);
                        QADatabase.deleteStaffCommentsByAnswerId(aid);
                        QADatabase.deleteAnswer(ans);
                    }
                    QADatabase.deleteQuestion(q);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                updatePage(layout, sortByWeight);
            });

            HBox questionHeader = new HBox(20, ql, ul, deleteQuestion);
            questionBox.getChildren().add(questionHeader);

            ArrayList<Answer> answers = QADatabase.getAnswersbyQuestion(q);
            if (!answers.isEmpty()) {
                canFilter = true;
                questionBox.getChildren().add(new Label("Answers:"));

                for (Answer ans : answers) {
                    try {
                        if (filter && !QADatabase.isResolved(ans)) continue;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    VBox answerBox = new VBox(5);
                    answerBox.setStyle("-fx-border-color: lightgray; -fx-background-color: #ffffff; -fx-padding: 8;");
                    answerBox.setMaxWidth(Double.MAX_VALUE);

                    Label answerText = new Label(ans.getInput());
                    Label answerFrom = new Label("By User: " + ans.getUserName());

                    
                    if (QADatabase.isUserFlagged(ans.getUserName())) {
                        Label flaggedWarning = new Label("⚠️ This user has been flagged by staff.");
                        flaggedWarning.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        answerBox.getChildren().add(flaggedWarning);
                    }

                    
                    try {
                        int aid = QADatabase.getAID(ans);
                        ArrayList<String> staffComments = QADatabase.getStaffCommentsByAID(aid);
                        for (String comment : staffComments) {
                            Label staffLabel = new Label("Staff Comment: " + comment);
                            staffLabel.setStyle("-fx-font-style: italic; -fx-text-fill: darkblue;");
                            answerBox.getChildren().add(staffLabel);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    CheckBox resolvedCheckBox = new CheckBox("This resolved my issue?");
                    resolvedCheckBox.setOnAction(a -> {
                        ans.changeResolve(resolvedCheckBox.isSelected());
                        try {
                            QADatabase.changeResolveAnswer(ans);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });
                    
                    Button deleteAnswer = new Button("Delete this answer");
                    deleteAnswer.setOnAction(e -> {
                        QADatabase.deleteAnswer(ans);
                        updatePage(layout, sortByWeight);
                    });



                    answerBox.getChildren().addAll(answerText, answerFrom, resolvedCheckBox, deleteAnswer);

                    ArrayList<Review> reviews = QADatabase.getReviewsByAnswer(ans);
                    if (sortByWeight) {
                        reviews.sort((r1, r2) -> Integer.compare(
                                user.getReviewerWeight(r2.getReviewer()),
                                user.getReviewerWeight(r1.getReviewer())
                        ));
                    }

                    for (Review r : reviews) {
                        VBox reviewBox = new VBox(3);
                        reviewBox.setStyle("-fx-border-color: lightblue; -fx-padding: 6;");
                        reviewBox.setMaxWidth(Double.MAX_VALUE);

                        Label rText = new Label("Review: " + r.getInput());
                        int weight = user.getReviewerWeight(r.getReviewer());
                        String reviewerInfo = "By Reviewer: " + r.getReviewer();
                        if (weight > 0) reviewerInfo += " (weight " + weight + ")";
                        Label rFrom = new Label(reviewerInfo);

                        TextField weightField = new TextField();
                        weightField.setPromptText("Weight (1–5)");
                        weightField.setMaxWidth(100);

                        Button trustBtn = new Button("Trust Reviewer");
                        trustBtn.setOnAction(e -> {
                            try {
                                int w = Integer.parseInt(weightField.getText().trim());
                                if (w >= 1 && w <= 5) {
                                    user.trustReviewer(r.getReviewer(), w);
                                    updatePage(layout, sortByWeight);
                                } else {
                                    System.out.println("Weight must be between 1–5");
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println("Invalid weight input.");
                            }
                        });

                        TextField editField = new TextField(r.getInput());
                        editField.setMaxWidth(300);
                        Button editBtn = new Button("Edit Review");
                        editBtn.setOnAction(e -> {
                            String newInput = editField.getText();
                            if (!newInput.isEmpty()) {
                                try {
                                    QADatabase.editReview(r, newInput);
                                    updatePage(layout, sortByWeight);
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        Button deleteBtn = new Button("Delete Review");
                        deleteBtn.setOnAction(e -> {
                            try {
                                QADatabase.deleteReview(r);
                                updatePage(layout, sortByWeight);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        });

                        reviewBox.getChildren().addAll(rText, rFrom, weightField, trustBtn, editField, editBtn, deleteBtn);
                        answerBox.getChildren().add(reviewBox);
                    }

                    if (reviewer) {
                        TextField postReview = new TextField();
                        postReview.setPromptText("Write your Review here");

                        Button submitReview = new Button("Submit Review");
                        submitReview.setOnAction(c -> {
                            String input = postReview.getText();
                            if (!input.isEmpty()) {
                                try {
                                    QADatabase.addReview(new Review(ans, user.getUserName(), input));
                                    updatePage(layout, sortByWeight);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                        answerBox.getChildren().addAll(postReview, submitReview);
                    }

                    questionBox.getChildren().add(answerBox);
                }
            }

            TextField postAnswer = new TextField();
            postAnswer.setPromptText("Write your answer here");
            Button submitAnswer = new Button("Submit Answer");
            submitAnswer.setOnAction(c -> {
                String input = postAnswer.getText();
                if (!input.isEmpty()) {
                    try {
                        QADatabase.addAnswer(new Answer(q, user.getUserName(), input));
                        updatePage(layout, sortByWeight);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            questionBox.getChildren().addAll(postAnswer, submitAnswer);
            questionDisplay.getChildren().add(questionBox);
        }

        layout.getChildren().add(questionDisplay);
    }
}
