package QuestionAnswerSystem;

/**
 * This is the Question class, defining the question's input and whether it is
 * resolved.
 */

public class Question {
	private String input;
	private Answers answers = new Answers(this);

	public Question(String input) {
		this.input = input;
	}
	
	
	//Fetch the question input(read)
	public String getInput() {
		return input;
	}

	//Fetch the answers for the question(read)
	public Answers getAnswers() {
		return answers;
	}

	//set the input of the question(create, update)
	public void setInput(String input) {
		this.input = input;
	}
	
	
}
