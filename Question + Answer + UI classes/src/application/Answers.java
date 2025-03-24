package application;
import java.util.ArrayList;



/**
 *  This class is responsible for the CRUD operations of a list of answers.
 */

public class Answers{
	private ArrayList<Answer> answers = new ArrayList<Answer>();
	private Question question;
	
	public Answers(Question question) {
		this.question = question;
	}
	
	
	//add answer to the list (update, create)
	public void addAnswer(Answer answer) {
		answers.add(answer);
	}
	
	//read answer in the list(read)
	public Answer getAnswer(int i) {
		return answers.get(i);
	}
	
	//fetch the corresponding question (read)
	public Question getQuestion() {
		return question;
	}
	
	//remove a given answer from the list (delete)
	public void removeAnswer(Answer answer) {
		answers.remove(answer);
	}
	
	//check if there are answers
	public boolean isEmpty() {
		return answers.size()==0;
	}
	
	//return how many answers there are
	public int getSize() {
		return answers.size();
	}


}