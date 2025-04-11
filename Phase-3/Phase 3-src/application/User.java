package application;
import java.util.HashMap;
import java.util.Map;


/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, email, and role.
 */
public class User {
    private String userName;
    private String password; // Hashed password
    private String email; // Added by Abdullah: To store the user's email address
    private String role;
    //Reviewers
    private Map<String, Integer> trustedReviewers = new HashMap<>();

    // Constructor to initialize a new User object with userName, password, email, and role.
    public User(String userName, String password, String email, String role) { // Edited by Abdullah: Added email field
        this.userName = userName;
        this.password = password;
        this.email = email; // Added by Abdullah
        this.role = role;
    }
    
    // Sets the role of the user.
    public void setRole(String role) {
        this.role = role;
    }
    
    // Sets the email of the user.
    public void setEmail(String email) { // Added by Abdullah
        this.email = email;
    }

    // Gets the user's username.
    public String getUserName() {
        return userName;
    }

    // Gets the user's hashed password.
    public String getPassword() {
        return password;
    }

    // Sets a hashed password.
    public void setPassword(String password) { // Edited by Abdullah: Ensure password is hashed before saving
        this.password = password; 
    }

    // Gets the user's role.
    public String getRole() {
        return role;
    }
    
    // Gets the user's email.
    public String getEmail() { // Added by Abdullah
        return email;
    }

    /**
     * Validates the format of the username.
     * @return true if username is valid, otherwise false.
     */
    public static boolean isValidUserName(String userName) { // Added by Abdullah
        return userName.length() >= 6; // Username must have at least 6 characters.
    }

    /**
     * Validates the format of the password.
     * @return true if password is valid, otherwise false.
     */
    public static boolean isValidPassword(String password) { // Added by Abdullah
        // At least 10 characters, one uppercase, one lowercase, one digit, and one special character.
        return password.length() >= 10 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[@#$%^&+=!].*");
    }
    
    //Reviewer Weight Stuff
    public void addTrustedReviewer(String username, int weight) {
        trustedReviewers.put(username, weight);
    }

    public void removeTrustedReviewer(String username) {
        trustedReviewers.remove(username);
    }

    public Map<String, Integer> getTrustedReviewers() {
        return trustedReviewers;
    }

    public void trustReviewer(String reviewerName, int weight) {
        trustedReviewers.put(reviewerName, weight);
    }

    public int getReviewerWeight(String reviewerName) {
        return trustedReviewers.getOrDefault(reviewerName, 0);
    }
    
    /**
     * Validates the format of the email address.
     * @return true if email is valid, otherwise false.
     */
    public static boolean isValidEmail(String email) { // Added by Abdullah
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
