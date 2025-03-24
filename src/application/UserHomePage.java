package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class UserHomePage {

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, User!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
<<<<<<< HEAD

=======
	    
	    /*String[] roles = user.getRole().split(",");
	    if(roles.length > 1) {
	    	Label navigation = new Label("")
	    }*/
	    
>>>>>>> 2b723f7 (Finished features for admin and new users)
	    layout.getChildren().add(userLabel);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}