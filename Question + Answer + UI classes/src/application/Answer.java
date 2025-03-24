package application;

/**
 * This is the Answer class, defining the answer's input and whether it is resolved.
 */

public class Answer { 
	private String input;
	private boolean resolved;
	public Answer(String input) {
		this.input = input;
		resolved = false;
	}
	
	//Fetch the answer input(read)
	public String getInput() {
		return input;
	}
	
	//Set the status of the answer resolving the issue (update, create)
	public void changeResolve(boolean r) {
		resolved = r;
	}
	
	//check if the answer resolves the issue
	public boolean isResolved() {
		return resolved;
	}
	
	//set the input of the answer(update, create)
	public void setInput(String input) {
		this.input = input;
	}


}
