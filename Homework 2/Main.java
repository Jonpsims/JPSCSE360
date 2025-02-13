package application;

public class Main {
    public static void main(String[] args) {
        Questions questions = new Questions();
        Answers answers = new Answers();

        questions.addQuestion("What is Java?");
        answers.addAnswer(1, "Java is a programming language.");

        System.out.println("Question: " + questions.getQuestion(1).getText());
        System.out.println("Answer: " + answers.getAnswer(1).getText());
    }
}
