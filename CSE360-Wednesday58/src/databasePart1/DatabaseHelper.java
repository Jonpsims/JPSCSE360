package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import application.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");
			createTables();  // Create the necessary tables if they don't exist
			System.out.println(getTable().toString());
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
        + "id INT AUTO_INCREMENT PRIMARY KEY, "
        + "userName VARCHAR(255) UNIQUE, "
        + "password VARCHAR(255), "
        + "email VARCHAR(255), " // Added by Abdullah: Email field for users
        + "role VARCHAR(200))";
statement.execute(userTable);

		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE, "
	            + "roles VARCHAR(255))";
	    statement.execute(invitationCodesTable);
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
	
    String insertUser = "INSERT INTO cse360users (userName, password, email, role) VALUES (?, ?, ?, ?)"; // Edited by Abdullah: Added email
    try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getEmail()); // Added by Abdullah
        pstmt.setString(4, user.getRole());
        pstmt.executeUpdate();
    }
}

	public void updateUser(User user) throws SQLException {
	    String updateUser = "UPDATE cse360users SET password = ? WHERE username = ?"; //Added by Sofia: update password for one-time password feature
	    try (PreparedStatement pstmt = connection.prepareStatement(updateUser)) {
	        pstmt.setString(1, user.getPassword());
	        pstmt.setString(2, user.getUserName());
	        pstmt.executeUpdate();
	    }
	}


	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Added by Abdullah: Checks if an email already exists in the database.
public boolean doesEmailExist(String email) {
    String query = "SELECT COUNT(*) FROM cse360users WHERE email = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // Email exists if count > 0
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Assume email doesn't exist on error
}


	// Added by Abdullah: Fetch user by email
public User getUserByEmail(String email) throws SQLException {
    String query = "SELECT * FROM cse360users WHERE email = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            // Create and return a User object
            return new User(rs.getString("userName"), rs.getString("password"), rs.getString("email"), rs.getString("role"));
        }
    }
    return null; // Return null if no user is found
}

// Added by Abdullah: Fetch user by username
public User getUserByUsername(String username) throws SQLException {
    String query = "SELECT * FROM cse360users WHERE userName = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            // Create and return a User object
            return new User(rs.getString("userName"), rs.getString("password"), rs.getString("email"), rs.getString("role"));
        }
    }
    return null; // Return null if no user is found
}


//Added by Abdullah: Deletes a user from the database based on their username
public void deleteUser(String userName) throws SQLException { 
 String query = "DELETE FROM cse360users WHERE userName = ?";
 try (PreparedStatement pstmt = connection.prepareStatement(query)) {
     pstmt.setString(1, userName);
     int rowsAffected = pstmt.executeUpdate();
     if (rowsAffected > 0) {
         System.out.println("User " + userName + " deleted successfully.");
     } else {
         System.out.println("No user found with username: " + userName);
     }
 } catch (SQLException e) {
     System.err.println("Error deleting user: " + e.getMessage());
     throw e; // Rethrow the exception for higher-level handling
 }
}


	
	//Added by Sofia: Returns the list of users in the database (for Admin purposes)
	public ArrayList<ArrayList<String>> getTable() {
		String query = "SELECT * FROM cse360users";
		try (PreparedStatement pstmt = connection.prepareStatement(query)){
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
			int columns = rs.getMetaData().getColumnCount();
			
			while(rs.next()) {
				ArrayList<String> row = new ArrayList<String>();
				for(int i = 1; i <= columns; i++) {
					row.add(rs.getString(i));
				}
				table.add(row);
			}
			
			return table;
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	//Added by Sofia: Fetch the given column as a string arrayList
	public ArrayList<String> getColumn(String columnName) {
	    String query = "SELECT " + columnName + " FROM cse360users";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {

	        ResultSet rs = pstmt.executeQuery();
	        ArrayList<String> column = new ArrayList<String>();
	        
	        while(rs.next()) {
	            column.add(rs.getString(columnName));
	        }
	        
	        return column;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code with assigned roles and inserts it into the database.
	public String generateInvitationCode(String roles) { //Edited by Sofia: add a parameter for the admin's role assignments
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code, isUsed, roles) VALUES (?, FALSE, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, roles);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	//Added by Sofia: returns the role that the admin has assigned to the new user
	public String getInvitedRole(String code) {
	    String query = "SELECT roles FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("roles"); // Return the roles if the code exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}


}
