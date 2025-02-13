package application;

import java.util.HashMap;
import java.util.Map;

public class Answers {
    private Map<Integer, Answer> answers = new HashMap<>();
    private int nextId = 1;

    public void addAnswer(int questionId, String text) {
        Answer a = new Answer(nextId, questionId, text);
        answers.put(nextId, a);
        nextId++;
    }

    public Answer getAnswer(int id) {
        return answers.get(id);
    }

    public void updateAnswer(int id, String newText) {
        if (answers.containsKey(id)) {
            answers.get(id).setText(newText);
        } else {
            throw new IllegalArgumentException("Answer not found.");
        }
    }

    public void deleteAnswer(int id) {
        if (answers.containsKey(id)) {
            answers.remove(id);
        } else {
            throw new IllegalArgumentException("Answer not found.");
        }
    }
}
