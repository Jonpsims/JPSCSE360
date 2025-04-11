package application;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import QuestionAnswerSystem.*;

public class ReviewerHomePage {
	
	   // private boolean filter = false; // Flag to indicate if only resolved answers should be displayed.
	    //private boolean canFilter = false; // Flag to indicate if there are answers available to filter.
	    //private boolean reviewer = false; // Flag to indicate if the current user has the 'reviewer' role (currently not used in the provided code).
	    private final QASystemDatabase QADatabase; // Instance of the database class to interact with the database.
	    private application.User user; // The currently logged-in user.

	    /**
	     * Constructor for the StudentHomePage.
	     * @param QADatabase The database instance to be used.
	     * @param user The currently logged-in user.
	     */
	    public ReviewerHomePage(QASystemDatabase QADatabase, application.User user) {
	        this.user = user;
	        this.QADatabase = QADatabase;
	    }

		//VBox questionDisplay = new VBox();
	    public void show(Stage primaryStage) {
	    	
	    	
	    	VBox layout = new VBox(5);
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");


	        // Label for the list of posted questions.
	        Label existingQuestionsLabel = new Label("Reviews from " + user.getUserName());
	        layout.getChildren().add(existingQuestionsLabel);
	        
	        ArrayList<Review> reviews = QADatabase.getReviewsByUser(user.getUserName());
	        if(!reviews.isEmpty()) {
		        for(int i = 0; i < reviews.size(); i++) {
		        	Review r = reviews.get(i);
		        	System.out.println(r.getInput());
		        	Label review = new Label(r.getInput());
		        	review.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
		        	Label answer = new Label("Original answer: " + r.getAnswer().getInput());
		        	answer.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
		        	Label question = new Label("Original question: " + r.getAnswer().getQuestion().getInput());
		        	question.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
		        	layout.getChildren().addAll(review, answer, question);
		        }
	        }
	    	
	        primaryStage.setScene(new Scene(layout, 800, 400));
	        primaryStage.setTitle("Questions and Answers");
	        primaryStage.show();
	    }
	    
}
