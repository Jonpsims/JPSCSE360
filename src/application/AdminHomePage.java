package application;

<<<<<<< HEAD

=======
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
>>>>>>> 2b723f7 (Finished features for admin and new users)
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

<<<<<<< HEAD
=======
import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.ArrayList;
>>>>>>> 2b723f7 (Finished features for admin and new users)

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */
<<<<<<< HEAD

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */

	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    layout.getChildren().add(adminLabel);
	    Scene adminScene = new Scene(layout, 800, 400);
	    
	    // table to display the lists of users and their information
	    
	    
	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
=======
public class AdminHomePage {

    private final DatabaseHelper databaseHelper; // Added by Abdullah: DatabaseHelper instance

    // Constructor to initialize the DatabaseHelper
    public AdminHomePage(DatabaseHelper databaseHelper) { // Added by Abdullah
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the welcome message for the admin
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        layout.getChildren().add(adminLabel);

        // Table to display the list of users and their information
       /* TableView<User> userTable = new TableView<>(); // Added by Abdullah

        // Table columns for username, email, and role
        TableColumn<User, String> userNameColumn = new TableColumn<>("Username"); // Added by Abdullah
        userNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email"); // Added by Abdullah
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role"); // Added by Abdullah
        roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));

        // Add columns to the table
        userTable.getColumns().addAll(userNameColumn, emailColumn, roleColumn); // Added by Abdullah

        // Fetch users from the database and populate the table
        ArrayList<ArrayList<String>> data = databaseHelper.getTable();
        ObservableList<ObservableList<String>> users = FXCollections.observableArrayList();
        for(ArrayList<String> row: data) {
        	users.add(FXCollections.observableArrayList(row));
        }
        tb.setItems();*/
        
        TableView userTable = new TableView();
        
        //Add applicable fields and set them to columns
        ArrayList<ArrayList<String>> data = databaseHelper.getTable();
        ObservableList<ObservableList<String>> users = FXCollections.observableArrayList();
        for(ArrayList<String> row: data) {
        	users.add(FXCollections.observableArrayList(row));
        }
        
        TableColumn<ObservableList<String>, String> usernames = new TableColumn<>("Username");
        usernames.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
        
        
        TableColumn<ObservableList<String>, String> email = new TableColumn("Email Address");     
        email.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
        
        TableColumn<ObservableList<String>, String> roles = new TableColumn<>("Role(s)");
        roles.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));

        //Set the table's items to the corresponding columns
        userTable.getColumns().addAll(usernames, email, roles);
        userTable.setItems(users);

        // Delete user button
        Button deleteButton = new Button("Delete User"); // Added by Abdullah
        deleteButton.setOnAction(event -> {
            ObservableList<String> selection = (ObservableList<String>) userTable.getSelectionModel().getSelectedItem();
            User selectedUser;
			try {
				selectedUser = databaseHelper.getUserByUsername(selection.get(1));

			
            if (selectedUser != null) {
                boolean confirmation = showConfirmationDialog("Are you sure you want to delete " + selectedUser.getUserName() + "?");
                if (confirmation) {
                    try {
                        databaseHelper.deleteUser(selectedUser.getUserName());
                        data.remove(userTable.getSelectionModel().getSelectedIndex()); // Remove user from the table
                        System.out.println("User " + selectedUser.getUserName() + " deleted successfully.");
                    } catch (SQLException e) {
                        System.err.println("Error deleting user: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("No user selected for deletion.");
            }
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        // Add table and delete button to layout
        layout.getChildren().addAll(userTable, deleteButton); // Added by Abdullah

        // Set the scene to primary stage
        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
    }

    /**
     * Shows a confirmation dialog before performing an action.
     * @param message The confirmation message.
     * @return true if the user confirms, false otherwise.
     */
    private boolean showConfirmationDialog(String message) { // Added by Abdullah
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(message);
        alert.setContentText("This action cannot be undone.");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        return alert.showAndWait().orElse(noButton) == yesButton;
>>>>>>> 2b723f7 (Finished features for admin and new users)
    }
}