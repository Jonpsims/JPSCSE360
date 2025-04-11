package QuestionAnswerSystem;

import java.util.ArrayList;



/**
 * This class is responsible for the CRUD operations of a list of questions.
 */

public class Questions {
	private ArrayList<Question> questions;
	public Questions() {
		questions = new ArrayList<Question>();
	}
	
	//Get question (read)
	public Question getQ(int i) {
		return questions.get(i);
	}
	
	//Add a question to the list (create, update)
	public void add(Question question) {
		questions.add(question);
	}
	
	//Remove a question from the list(delete)
	public void remove(Question question) {
		questions.remove(question);
	}
	
	//Fetch the length of the list
	public int getQLength() {
		return questions.size();
	}

	//check if the list is empty
	public boolean isEmpty() {
		return questions.size()==0;
	}
}