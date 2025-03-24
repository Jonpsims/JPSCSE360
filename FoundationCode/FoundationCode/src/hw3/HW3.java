package hw3;
public class HW3 {

    
    //too short (less than 8 characters)
    public static void testPassTooShort() {
        String input = "A1b!";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Password Too Short: " + result);
    }

    
    //missing uppercase letters
    public static void testPassMissingUppercase() {
        String input = "abc123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Uppercase: " + result);
    }

    //missing lowercase letters
    public static void testPassMissingLowercase() {
        String input = "ABC123!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Lowercase: " + result);
    }

    
    //missing digits
    public static void testPassMissingDigit() {
        String input = "Abcdef!@";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Digit: " + result);
    }


    //missing special characters
    public static void testPassMissingSpecialChar() {
        String input = "Abcdef12";
        String result = application.PasswordEvaluator.evaluatePassword(input);
        System.out.println("Test Missing Special Char: " + result);
    }


    //eexecute test cases
    public static void main(String[] args) {
        testPassTooShort();
        testPassMissingUppercase();
        testPassMissingLowercase();
        testPassMissingDigit();
        testPassMissingSpecialChar();
    }
}
