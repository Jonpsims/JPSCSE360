package QuestionAnswerSystem;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class StudentHomePage {
	private final  Questions questions = new Questions();
	private boolean filter = false;
	private boolean canFilter = false;
    public void show(Stage primaryStage) {
    	
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");


	    //Label for the list of posted questions
        Label existingQuestionsLabel = new Label("Questions:");
        layout.getChildren().add(existingQuestionsLabel);
	    updatePage(layout);
	    
	    //Textbox for the user to write their question
	    TextField postQuestion = new TextField("Write your Question here");
	    Button submit = new Button("Submit");
	    submit.setOnAction(a -> {
	    	String input = postQuestion.getText();
	    	//Send an error if there is an empty text box
	    	if(!input.isEmpty()) {
		    	Question q = new Question(input);
		    	questions.add(q);
		    	System.out.println("Question: " + q.getInput() + " Successfully posted");
		    	updatePage(layout);	
	    	}else {
	    		System.out.println("Error: Empty question. Please try again");
	    	}
	    });
	    layout.getChildren().addAll(postQuestion, submit);
	    
	    //Checkbox to remove answers that did not resolve the issue
        CheckBox filterCheckbox = new CheckBox("Show only resolved answers");
        filterCheckbox.setOnAction(e -> {
        	//Send an error if there is nothing to filter
        	if(canFilter) {
                filter = filterCheckbox.isSelected();
                updatePage(layout);
        	}else {
        		System.out.println("Error: Nothing to filter. Please try again");
        	}
        });
        layout.getChildren().add(filterCheckbox);
    	
        primaryStage.setScene(new Scene(layout, 1200, 1200));
        primaryStage.setTitle("Questions and Answers");
        primaryStage.show();
    }
    
    public void updatePage(VBox layout) {

        
    	layout.getChildren().removeIf(node -> node instanceof VBox);
    	//add a display for each question
    	for(int i = 0; i < questions.getQLength(); i++) {
    		VBox questionDisplay = new VBox();
	    	Question q = questions.getQ(i);
	    	
    		Label l = new Label("Question: " + q.getInput());
	    	l.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
	    	
	    	//add a button to delete a question
            Button deleteQuestion= new Button("Delete this Question");
            deleteQuestion.setOnAction(e -> {
                questions.remove(q);
                updatePage(layout);
                System.out.println("Question: " + q.getInput() + " successfully deleted");
            });
	    	questionDisplay.getChildren().addAll(l, deleteQuestion);
	    	

            if (!q.getAnswers().isEmpty()) {
            	//have a display for each answer
                Label answerLabel = new Label("Answers");
                questionDisplay.getChildren().add(answerLabel);
                Answers answers = q.getAnswers();
                
                for (int j = 0; j < answers.getSize(); j++) {
                	Answer ans = answers.getAnswer(j);
                	//do not display the answer if it is unresolved and the filter check is on
                	if(filter && !ans.isResolved()) {
                		continue;
                	}
                    Label answerText = new Label(ans.getInput());
                    
                    //Checkbox to mark an answer that resolves the issue
                    CheckBox resolvedCheckBox = new CheckBox("This resolved my issue?");
                    resolvedCheckBox.setOnAction(a -> ans.changeResolve(resolvedCheckBox.isSelected()));
                    
                    //add a button to delete an answer
                    Button deleteAnswer = new Button("Delete this answer");
                    deleteAnswer.setOnAction(e -> {
                        q.getAnswers().removeAnswer(ans);
                        updatePage(layout);
                        System.out.println("Answer deleted: " + ans.getInput());
                    });
                    questionDisplay.getChildren().addAll(answerText, resolvedCheckBox, deleteAnswer);
                }
            }
            
            //add a textfield for the user to write an answer
            TextField postAnswer = new TextField("Write your answer here");
            Button submitAnswer = new Button("Submit Answer");
            submitAnswer.setOnAction(c -> {
            	String input = postAnswer.getText();
            	//send an error if there is an empty text box
                if (!input.isEmpty()) {
                    Answer answer = new Answer(input);
                    q.getAnswers().addAnswer(answer);
                    System.out.println("Answer: " + input + " successfully posted");
                    canFilter = true;
                    updatePage(layout); // Refresh UI to display the new answer
                }else {
                	System.out.println("Error: Empty answer. Please try again");
                }
            });
            
            questionDisplay.getChildren().addAll(postAnswer, submitAnswer);
            layout.getChildren().add(questionDisplay);
    	}
    }
    	
    }