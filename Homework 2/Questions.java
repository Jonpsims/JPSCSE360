package application;

import java.util.HashMap;
import java.util.Map;

public class Questions {
    private Map<Integer, Question> questions = new HashMap<>();
    private int nextId = 1;

    public void addQuestion(String text) {
        Question q = new Question(nextId, text);
        questions.put(nextId, q);
        nextId++;
    }

    public Question getQuestion(int id) {
        return questions.get(id);
    }

    public void updateQuestion(int id, String newText) {
        if (questions.containsKey(id)) {
            questions.get(id).setText(newText);
        } else {
            throw new IllegalArgumentException("Question not found.");
        }
    }

    public void deleteQuestion(int id) {
        if (questions.containsKey(id)) {
            questions.remove(id);
        } else {
            throw new IllegalArgumentException("Question not found.");
        }
    }
}
