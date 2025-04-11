package QuestionAnswerSystem;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import application.User;
public class QASystemDatabase {

	/**
	 * The DatabaseHelper class is responsible for managing the connection to the database,
	 * performing operations such as user registration, login validation, and handling invitation codes.
	 */

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
				printQuestions(getQuestions());
				//printReviews(getReviews());
				printAnswers(getAnswers());
			} catch (ClassNotFoundException e) {
				System.err.println("JDBC Driver not found: " + e.getMessage());
			}
		}

		private void createTables() throws SQLException {
			//question table
			String questionTable = "CREATE TABLE IF NOT EXISTS questionTable ("
		        + "id INT AUTO_INCREMENT PRIMARY KEY, "
		        + "userName VARCHAR(255), "
		        + "input VARCHAR(1000)) ";
			statement.execute(questionTable);

			
			//answer table
		    String answerTable = "CREATE TABLE IF NOT EXISTS answerTable ("
		    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
		            + "qID INT, "
		    		+ "userName VARCHAR(255), "
		            + "input VARCHAR(500), "
                    + "resolved BOOLEAN DEFAULT FALSE, "
                    + "FOREIGN KEY (qID) REFERENCES questionTable(id) ON DELETE CASCADE)";            
		    statement.execute(answerTable);	    
		    
			//review table
		    String reviewTable = "CREATE TABLE IF NOT EXISTS reviewTable ("
		    		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
		            + "aID INT, "
		    		+ "userName VARCHAR(255), "
		            + "input VARCHAR(500), "
                    + "FOREIGN KEY (aID) REFERENCES questionTable(id) ON DELETE CASCADE)";            
		    statement.execute(reviewTable);	 
		}
		
		// Check if the database is empty
		public boolean isDatabaseEmpty() throws SQLException {
			String query = "SELECT COUNT(*) AS count FROM q";
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count") == 0;
			}
			return true;
		}

		
		
		//question functions
		public void addQuestion(Question q) throws SQLException {
		    String insertUser = "INSERT INTO questionTable (userName, input) VALUES (?, ?)"; 
		    try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
		        pstmt.setString(1, q.getUserName());
                System.out.println(q.getUserName() + "\n");
		        pstmt.setString(2, q.getInput());
                System.out.println(q.getInput() + "\n");
		        pstmt.executeUpdate();
		    }
		}
		

		public Question getQuestion(Question q) throws SQLException {
		    String query = "SELECT * FROM questionTable WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, q.getUserName());
		        pstmt.setString(2, q.getInput());
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            // Create and return a Question object
		            return new Question(rs.getString("userName"), rs.getString("input"));
		        }
		    }
		    return null; // Return null if no user is found
		}
		
		public Question getQuestionByID(int ID) throws SQLException {
		    String query = "SELECT * FROM questionTable WHERE id = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, ID);
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            // Create and return a Question object
		            return new Question(rs.getString("userName"), rs.getString("input"));
		        }
		    }
		    return null; // Return null if no user is found
		}
		
		public int getQID(Question q) throws SQLException {
		    String query = "SELECT id FROM questionTable WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, q.getUserName());
		        pstmt.setString(2, q.getInput());
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            
		           return rs.getInt("id");
		        }
		    }
		    return -1;
		}

		public void deleteQuestion(Question q) throws SQLException { 
			 String query = "DELETE FROM questionTable WHERE userName = ? AND input = ?";
			 try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			     pstmt.setString(1, q.getUserName());
                 System.out.println(q.getUserName() + "\n");
			     pstmt.setString(2, q.getInput());
			     int rowsAffected = pstmt.executeUpdate();
			     if (rowsAffected > 0) {
			         System.out.println("Question: " + q.getInput() + " deleted successfully.");
			     } else {
			         System.out.println("No question found with input: " + q.getInput());
			     }
			 } catch (SQLException e) {
			     System.err.println("Error deleting question: " + e.getMessage());
			     throw e; // Rethrow the exception for higher-level handling
			 }
		}
		
		
		
		
		
		
		
		
		
		
		
		
		//answer functions
		
		public void addAnswer(Answer a) throws SQLException {
			String getQuestion = "SELECT id FROM questionTable WHERE input = ?";
		    String insertAnswer = "INSERT INTO answerTable (qID, userName, input) VALUES (?, ?, ?)"; 
	        try (PreparedStatement pstmt1 = connection.prepareStatement(getQuestion)){
	               pstmt1.setString(1, a.getQuestion().getInput());
	               ResultSet rs = pstmt1.executeQuery();
	               if (rs.next()) {
		               try(PreparedStatement pstmt2 = connection.prepareStatement(insertAnswer)) {
		                   int questionId = rs.getInt("id");
		                   System.out.println("Question: " + questionId + " " + a.getQuestion().getInput() + " has been found");
		                   pstmt2.setInt(1, questionId);
		                   pstmt2.setString(2, a.getUserName());
		                   pstmt2.setString(3, a.getInput());
		                   pstmt2.executeUpdate();
		                   System.out.println("answer has been posted");
	               }catch(SQLException e0) {
	            	   e0.printStackTrace();
	               }
	           }
	        } catch (SQLException e) {
	               e.printStackTrace();
	        }
		}
		

		public Answer getAnswer(Answer a) throws SQLException {
		    String query = "SELECT * FROM answerTable WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, a.getUserName());
		        pstmt.setString(2, a.getInput());
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            return new Answer(a.getQuestion(), rs.getString("userName"), rs.getString("input"));
		        }
		    }
		    return null; // Return null if no user is found
		}
		
		public Answer getAnswerByID(int ID) throws SQLException {
		    String query = "SELECT * FROM answerTable WHERE id = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setInt(1, ID);
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            return new Answer(getQuestionByID(rs.getInt("qID")), rs.getString("userName"), rs.getString("input"));
		        }
		    }
		    return null; // Return null if no user is found
		}
		public void deleteAnswer(Answer answer) {
		    try {
		        // First delete all staff comments linked to this answer
		        int aid = getAID(answer);  // get ID of this answer
		        String deleteComments = "DELETE FROM staffCommentTable WHERE aID = ?";
		        try (PreparedStatement stmt = connection.prepareStatement(deleteComments)) {
		            stmt.setInt(1, aid);
		            stmt.executeUpdate();
		        }

		        // Now safely delete the answer itself
		        String sql = "DELETE FROM answerTable WHERE userName = ? AND input = ?";
		        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		            stmt.setString(1, answer.getUserName());
		            stmt.setString(2, answer.getInput());
		            stmt.executeUpdate();
		        }
		    } catch (SQLException e) {
		        System.err.println("Error deleting answer: " + e.getMessage());
		        e.printStackTrace();
		    }
		}

		public boolean isResolved(Answer a) throws SQLException {
			String query = "SELECT * FROM answerTable WHERE userName = ? AND input = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)){
				pstmt.setString(1, a.getUserName());
				pstmt.setString(2, a.getInput());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getBoolean("resolved");
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public void changeResolveAnswer(Answer a) throws SQLException {
			String query = "UPDATE answerTable SET resolved = ? WHERE userName = ? AND input = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)){
				pstmt.setBoolean(1, a.isResolved());
				pstmt.setString(2, a.getUserName());
				pstmt.setString(3,  a.getInput());
				pstmt.executeUpdate();
				System.out.println("Answer: " + a.getInput() + " successfully found and changed");
				System.out.println(isResolved(a));
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		public ArrayList<Answer> getAnswersbyQuestion(Question q){
			ArrayList<Answer> answers = new ArrayList<Answer>();
			String query = "SELECT * FROM answerTable WHERE qID = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, getQID(q));
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					answers.add(new Answer(q, rs.getString("userName"), rs.getString("input")));
				}
				return answers;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public ArrayList<Answer> getResolvedAnswers(){
			ArrayList<Answer> resolved = new ArrayList<Answer>();
			ArrayList<Answer> answers = getAnswers();
			for(Answer a : answers) {
				System.out.println("Answer: " + a.getInput());
				try {
					System.out.println(isResolved(a));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(isResolved(a)) {
						resolved.add(a);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return resolved;
		}
		
		public int getAID(Answer a) throws SQLException {
		    String query = "SELECT id FROM answerTable WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, a.getUserName());
		        pstmt.setString(2, a.getInput());
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            
		           return rs.getInt("id");
		        }
		    }
		    return -1;
		}
		
		
		
		
		
		
		
		
		//review functions
		public void addReview(Review r) throws SQLException {
			String getQuestion = "SELECT id FROM answerTable WHERE input = ?";
		    String insertAnswer = "INSERT INTO reviewTable (aID, userName, input) VALUES (?, ?, ?)"; 
	        try (PreparedStatement pstmt1 = connection.prepareStatement(getQuestion)){
	               pstmt1.setString(1, r.getAnswer().getInput());
	               ResultSet rs = pstmt1.executeQuery();
	               if (rs.next()) {
		               try(PreparedStatement pstmt2 = connection.prepareStatement(insertAnswer)) {
		                   int answerID = rs.getInt("id");
		                   System.out.println("Answer: " + answerID + " " + r.getAnswer().getInput() + " has been found");
		                   pstmt2.setInt(1, answerID);
		                   pstmt2.setString(2, r.getUsername());
		                   pstmt2.setString(3, r.getInput());
		                   pstmt2.executeUpdate();
		                   System.out.println("review has been posted");
	               }catch(SQLException e0) {
	            	   e0.printStackTrace();
	               }
	           }
	        } catch (SQLException e) {
	               e.printStackTrace();
	        }
		}
		
		public void deleteReview(Review r) throws SQLException {
		    String query = "DELETE FROM reviewTable WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, r.getUsername());
		        pstmt.setString(2, r.getInput());
		        int rows = pstmt.executeUpdate();
		        if (rows > 0) {
		            System.out.println("Review deleted: " + r.getInput());
		        } else {
		            System.out.println("No review found for deletion.");
		        }
		    } catch (SQLException e) {
		        System.err.println("Error deleting review: " + e.getMessage());
		        throw e;
		    }
		}

		public void editReview(Review r, String newInput) throws SQLException {
		    String query = "UPDATE reviewTable SET input = ? WHERE userName = ? AND input = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, newInput);
		        pstmt.setString(2, r.getUsername());
		        pstmt.setString(3, r.getInput());
		        int rows = pstmt.executeUpdate();
		        if (rows > 0) {
		            System.out.println("Review updated: " + r.getInput() + " → " + newInput);
		        } else {
		            System.out.println("No review found to update.");
		        }
		    } catch (SQLException e) {
		        System.err.println("Error editing review: " + e.getMessage());
		        throw e;
		    }
		}

		
		public ArrayList<Review> getReviewsByAnswer(Answer a){
			ArrayList<Review> reviews = new ArrayList<Review>();
			String query = "SELECT * FROM reviewTable WHERE aID = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setInt(1, getAID(a));
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					reviews.add(new Review(a, rs.getString("userName"), rs.getString("input")));
				}
				return reviews;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public ArrayList<Review> getReviewsByUser(String username){
			ArrayList<Review> reviews = new ArrayList<Review>();
			String query = "SELECT * FROM reviewTable WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					reviews.add(new Review(getAnswerByID(rs.getInt("aID")), rs.getString("userName"), rs.getString("input")));
				}
				return reviews;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
		
		
		//Staff functions
		
		

	    /**
	     * Adds a staff comment to an answer.
	     *
	     * @param answerId the answer ID to comment on
	     * @param commentText the comment content
	     */
		public void addStaffComment(int answerId, String commentText) {
		    try {
		        String sql = "CREATE TABLE IF NOT EXISTS staffCommentTable (id INT AUTO_INCREMENT PRIMARY KEY, aID INT, comment TEXT, FOREIGN KEY (aID) REFERENCES answerTable(id))";
		        statement.execute(sql);
		        sql = "INSERT INTO staffCommentTable (aID, comment) VALUES (?, ?)";
		        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
		            pstmt.setInt(1, answerId);
		            pstmt.setString(2, commentText);
		            pstmt.executeUpdate();
		            System.out.println("Staff comment added.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		
		

	    /**
	     * Returns a list of staff comments associated with an answer ID.
	     *
	     * @param answerId the answer ID to look up
	     * @return list of comments
	     */
		public ArrayList<String> getStaffCommentsByAID(int answerId) {
		    ArrayList<String> comments = new ArrayList<>();
		    try {
		        String sql = "SELECT comment FROM staffCommentTable WHERE aID = ?";
		        PreparedStatement pstmt = connection.prepareStatement(sql);
		        pstmt.setInt(1, answerId);
		        ResultSet rs = pstmt.executeQuery();
		        while (rs.next()) {
		            comments.add(rs.getString("comment"));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return comments;
		}


		 /**
	     * Flags a user by username for review or moderation.
	     * Creates the flaggedUsers table if it does not exist.
	     * 
	     * @param username the username to flag
	     */
		public void flagUser(String username) {
		    try {
		        String sql = "CREATE TABLE IF NOT EXISTS flaggedUsers (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255))";
		        statement.execute(sql);
		        sql = "INSERT INTO flaggedUsers (username) VALUES (?)";
		        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
		            pstmt.setString(1, username);
		            pstmt.executeUpdate();
		            System.out.println("User " + username + " flagged.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		
		/**
	     * Removes a previously flagged user from the flaggedUsers table.
	     * 
	     * @param username the username to unflag
	     */
		public void unflagUser(String username) {
		    try {
		        String sql = "DELETE FROM flaggedUsers WHERE username = ?";
		        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
		            pstmt.setString(1, username);
		            pstmt.executeUpdate();
		            System.out.println("User " + username + " unflagged.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		
		public void markUserTrusted(String username) {
		    try {
		        String sql = "CREATE TABLE IF NOT EXISTS trustedUsers (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255))";
		        statement.execute(sql);
		        sql = "INSERT INTO trustedUsers (username) VALUES (?)";
		        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
		            pstmt.setString(1, username);
		            pstmt.executeUpdate();
		            System.out.println("User " + username + " marked as trusted.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}


	    /**
	     * Deletes all staff comments associated with a specific answer ID.
	     *
	     * @param aid the answer ID
	     */
		public void deleteStaffCommentsByAnswerId(int aid) throws SQLException {
		    String sql = "DELETE FROM staffCommentTable WHERE aID = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
		        pstmt.setInt(1, aid);
		        pstmt.executeUpdate();
		        System.out.println("Deleted staff comments for answer ID: " + aid);
		    }
		}

		

	    /**
	     * Checks if a given user is currently flagged.
	     * 
	     * @param username the username to check
	     * @return true if flagged, false otherwise
	     */
		public boolean isUserFlagged(String username) {
		    try {
		        String sql = "CREATE TABLE IF NOT EXISTS flaggedUsers (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255))";
		        statement.execute(sql);
		        sql = "SELECT * FROM flaggedUsers WHERE username = ?";
		        PreparedStatement pstmt = connection.prepareStatement(sql);
		        pstmt.setString(1, username);
		        ResultSet rs = pstmt.executeQuery();
		        return rs.next();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}



		
	
		private static QASystemDatabase instance;

		public static QASystemDatabase getInstance() {
		    if (instance == null) {
		        instance = new QASystemDatabase();
		        try {
		            instance.connectToDatabase();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    return instance;
		}

		
		
		
		
		//misc. printing functions
		public ArrayList<Question> getQuestions(){
			ArrayList<Question> questions = new ArrayList<Question>();
			String query = "SELECT * FROM questionTable";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					questions.add(new Question(rs.getString("userName"), rs.getString("input")));
				}
				return questions;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public ArrayList<Answer> getAnswers(){
			ArrayList<Answer> answers = new ArrayList<Answer>();
			String query = "SELECT * FROM answerTable";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					answers.add(new Answer(getQuestionByID(rs.getInt("qID")), rs.getString("userName"), rs.getString("input")));
				}
				return answers;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public ArrayList<Review> getReviews(){
			ArrayList<Review> reviews = new ArrayList<Review>();
			String query = "SELECT * FROM reviewTable";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					reviews.add(new Review(getAnswerByID(rs.getInt("aID")), rs.getString("userName"), rs.getString("input")));
				}
				return reviews;
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public void printQuestions(ArrayList<Question> questions) {
			for(Question q : questions) {
				System.out.println("Username: " + q.getUserName() + "\n");
				System.out.println("Input: " + q.getInput() + "\n");
			}
		}
		
		public void printAnswers(ArrayList<Answer> answers) {
			for(Answer a : answers) {
				System.out.println("Username: " + a.getUserName());
				System.out.println("Input: " + a.getInput());
				System.out.println("Resolved: " + a.isResolved());
			}
		}
		
		public void printReviews(ArrayList<Review> reviews) {
			for(Review a : reviews) {
				System.out.println("Username: " + a.getUsername());
				System.out.println("Input: " + a.getInput());
				System.out.println("Answer: " + a.getAnswer().getInput());
				System.out.println("Question: " + a.getAnswer().getQuestion().getInput());
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


