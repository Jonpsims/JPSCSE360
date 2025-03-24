package hw3;
public class HW3 {

    
    //too short (less than 8 characters)
    public static void testPasswordTooShort() {
        String input = "A1b!";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Password Too Short: " + result);
    }

    
    //missing uppercase letters
    public static void testPasswordMissingUppercase() {
        String input = "abc123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Uppercase: " + result);
    }

    //missing lowercase letters
    public static void testPasswordMissingLowercase() {
        String input = "ABC123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Lowercase: " + result);
    }

    
    //missing digits
    public static void testPasswordMissingDigit() {
        String input = "Abcdef!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Digit: " + result);
    }


    //missing special characters
    public static void testPasswordMissingSpecialChar() {
        String input = "Abcdef12";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Special Char: " + result);
    }


    //eexecute test cases
    public static void main(String[] args) {
        testPasswordTooShort();
        testPasswordMissingUppercase();
        testPasswordMissingLowercase();
        testPasswordMissingDigit();
        testPasswordMissingSpecialChar();
    }
}
