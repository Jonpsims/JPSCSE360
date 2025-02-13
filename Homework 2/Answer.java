package application;

public class Answer {
    private int id;
    private int questionId;
    private String text;

    public Answer(int id, int questionId, String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty.");
        }
        this.id = id;
        this.questionId = questionId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty.");
        }
        this.text = text;
    }
}
