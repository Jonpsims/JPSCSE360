package QuestionAnswerSystem;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

public class QASystemDatabaseTest {

    private static QASystemDatabase db;

    @BeforeAll
    public static void setupDatabase() {
        db = QASystemDatabase.getInstance();
    }

    @Test
    public void testFlagAndUnflagUser() {
        String username = "testUserFlag";

        db.flagUser(username);
        assertTrue(db.isUserFlagged(username), "User should be flagged");

        db.unflagUser(username);
        assertFalse(db.isUserFlagged(username), "User should be unflagged");
    }

    @Test
    public void testAddAndFetchStaffComment() throws SQLException {
        Question q = new Question("staffCommentUser", "Why is the sky blue?");
        db.addQuestion(q);
        db.addAnswer(new Answer(q, "commentPoster", "Because of Rayleigh scattering."));
        Answer a = db.getAnswersbyQuestion(q).get(0);

        int aid = db.getAID(a);
        db.addStaffComment(aid, "Good explanation.");
        ArrayList<String> comments = db.getStaffCommentsByAID(aid);

        assertTrue(comments.contains("Good explanation."), "Comment should be present");
    }

    @Test
    public void testDeleteStaffComments() throws SQLException {
        Question q = new Question("delCommentUser", "Why is the ocean blue?");
        db.addQuestion(q);
        db.addAnswer(new Answer(q, "oceanPoster", "Also Rayleigh scattering."));
        Answer a = db.getAnswersbyQuestion(q).get(0);

        int aid = db.getAID(a);
        db.addStaffComment(aid, "Great answer!");
        db.deleteStaffCommentsByAnswerId(aid);

        ArrayList<String> comments = db.getStaffCommentsByAID(aid);
        assertEquals(0, comments.size(), "Comments should be deleted");
    }
}
