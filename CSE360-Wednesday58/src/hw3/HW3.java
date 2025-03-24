package hw3;

/**
 * HW3 Automated Testing Mainline
 * This file contains five automated tests for the PasswordEvaluator class.
 * Each test validates one key aspect of the password policy.
 * 
 * @author Your Name
 * @version 1.0
 */
public class HW3 {

    /**
     * Test: Password too short (less than 8 characters)
     */
    public static void testPasswordTooShort() {
        String input = "A1b!";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Password Too Short: " + result);
    }

    /**
     * Test: Password missing uppercase letters
     */
    public static void testPasswordMissingUppercase() {
        String input = "abc123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Uppercase: " + result);
    }

    /**
     * Test: Password missing lowercase letters
     */
    public static void testPasswordMissingLowercase() {
        String input = "ABC123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Lowercase: " + result);
    }

    /**
     * Test: Password missing digits
     */
    public static void testPasswordMissingDigit() {
        String input = "Abcdef!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Digit: " + result);
    }

    /**
     * Test: Password missing special characters
     */
    public static void testPasswordMissingSpecialChar() {
        String input = "Abcdef12";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Special Char: " + result);
    }

    /**
     * Mainline to execute all test cases
     */
    public static void main(String[] args) {
        testPasswordTooShort();
        testPasswordMissingUppercase();
        testPasswordMissingLowercase();
        testPasswordMissingDigit();
        testPasswordMissingSpecialChar();
    }
}
