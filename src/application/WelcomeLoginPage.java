package application;

import javafx.scene.Scene;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
>>>>>>> 2b723f7 (Finished features for admin and new users)

import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {
	

	private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    public void show( Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label welcomeLabel = new Label("Welcome!!");
	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
<<<<<<< HEAD
	    
	    // Button to navigate to the user's respective page based on their role
	    Button continueButton = new Button("Continue to your Page");
	    continueButton.setOnAction(a -> {
	    	String role =user.getRole();
	    	System.out.println(role);
	    	
	    	if(role.equals("admin")) {
	    		new AdminHomePage().show(primaryStage);
	    	}
	    	else if(role.equals("user")) {
	    		new UserHomePage().show(primaryStage);
	    	}
	    });
	    
=======
	    layout.getChildren().add(welcomeLabel);
	    
    	//Edited by Sofia: make a navigation page for users who have multiple roles
    	String[] role =user.getRole().strip().split(",");
    	System.out.println(role);
    	
    	if(role.length>1) {
    		Label navigation = new Label("You have multiple roles");
    	    navigation.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    		layout.getChildren().add(navigation);
    		
    		System.out.println(Arrays.toString(role));
    		//Added one button per user role
    		for(int i = 0; i < role.length; i++) {
    			Button roleButton = new Button("Click here to continue to " + role[i] + " page");
    			roleButton.setOnAction(b -> {
    				new UserHomePage().show(primaryStage);
    			});
    			layout.getChildren().add(roleButton);
    		}
    	}
    	//Edited by Sofia: if the user only has one role, display original button to continue to page
    	else {
	    Button continueButton = new Button("Continue to your Page");
	    layout.getChildren().add(continueButton);
	    continueButton.setOnAction(a -> {

	    	if(role[0].equals("admin")) {
	    		new AdminHomePage(databaseHelper).show(primaryStage);
	    	}
	    	else{
	    		new UserHomePage().show(primaryStage);
	    	}
	    });
    	}
>>>>>>> 2b723f7 (Finished features for admin and new users)
	    // Button to quit the application
	    Button quitButton = new Button("Quit");
	    quitButton.setOnAction(a -> {
	    	databaseHelper.closeConnection();
	    	Platform.exit(); // Exit the JavaFX application
	    });
	    
<<<<<<< HEAD
=======
	    
>>>>>>> 2b723f7 (Finished features for admin and new users)

	    if ("admin".equals(user.getRole())) {
		    // "Invite" button for admin to generate invitation codes
            Button inviteButton = new Button("Invite");
            inviteButton.setOnAction(a -> {
                new InvitationPage().show(databaseHelper, primaryStage);
            });
            
<<<<<<< HEAD
=======
            //Added by Sofia: "One-time Password" button users who have forgotten their password
            Label otPassword = new Label("Generate a one-time password");
            TextField usernameField = new TextField();
            usernameField.setPromptText("Enter user here");
            usernameField.setMaxWidth(250);
            
            Button generate = new Button("Generate Password");
            layout.getChildren().addAll(otPassword, usernameField, generate);
            
            generate.setOnAction(a -> {
            	String userInput = usernameField.getText();
            	try {
            		User u = databaseHelper.getUserByUsername(userInput);
            		String newPW = databaseHelper.generateInvitationCode(u.getRole()); // Generate a random 4-character code (like the invite code)
            		u.setPassword(newPW);
            		databaseHelper.updateUser(u);
            		System.out.println(u.getPassword());
            		Label newPWLabel = new Label("The new password for " + userInput + "is " + newPW);
            		System.out.println(databaseHelper.getTable().toString());
            		layout.getChildren().add(newPWLabel);
            	}catch(SQLException e){
            		e.printStackTrace();
            	}
            });
            
>>>>>>> 2b723f7 (Finished features for admin and new users)
            //Added by Sofia: Displaying the list of users in TableView
            
            TableView tb = new TableView();
            tb.setEditable(true);
            
            //Add applicable fields and set them to columns
            ArrayList<ArrayList<String>> data = databaseHelper.getTable();
            ObservableList<ObservableList<String>> users = FXCollections.observableArrayList();
            for(ArrayList<String> row: data) {
            	users.add(FXCollections.observableArrayList(row));
            }
            
            TableColumn<ObservableList<String>, String> usernames = new TableColumn<>("Username");
            usernames.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
<<<<<<< HEAD

=======
            
>>>>>>> 2b723f7 (Finished features for admin and new users)
            
            TableColumn<ObservableList<String>, String> email = new TableColumn("Email Address");     
            email.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
            
            TableColumn<ObservableList<String>, String> roles = new TableColumn<>("Role(s)");
            roles.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));

            //Set the table's items to the corresponding columns
            tb.getColumns().addAll(usernames, email, roles);
            tb.setItems(users);
            System.out.println(databaseHelper.getTable().toString());
            
            layout.getChildren().addAll(inviteButton, tb);
        }

<<<<<<< HEAD
	    layout.getChildren().addAll(welcomeLabel,continueButton,quitButton);
=======
	    layout.getChildren().add(quitButton);
>>>>>>> 2b723f7 (Finished features for admin and new users)
	    Scene welcomeScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(welcomeScene);
	    primaryStage.setTitle("Welcome Page");
    }
    
    
}