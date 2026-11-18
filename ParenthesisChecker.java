import java.util.Scanner;
public class ParenthesisChecker {
        public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("================================");
        System.out.println("     PARENTHESIS CHECKER");
        System.out.println("================================");

        while (true) {
            System.out.println("\n1. Check Expression");
            System.out.println("2. Correct Expression");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                       System.out.print("Enter expression: ");
                       String expression = sc.nextLine();
                       // Check whether the input is empty
                       if (expression.trim().isEmpty()) {
                             System.out.println("Please enter an expression!");
                                 break;
                         }
                       checkExpression(expression);
                        break;
                case 2:
                        // Accept an expression for automatic correction
                         System.out.print("Enter expression: ");
                         String input = sc.nextLine();
                         // Check whether the input is empty
                          if (input.trim().isEmpty()) {
                            System.out.println("Please enter an expression!");
                              break;
                         }
                          // Generate the corrected expression
                         String corrected = correctExpression(input);
                         System.out.println("\nOriginal Expression: " + input);
                         System.out.println("Corrected Expression: " + corrected);
                         break;
                case 3:
                    System.out.println("Thank you for using Parenthesis Checker!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } 
    }
    // Stack implementation
    static class Stack {
        char[] arr;
        int top;

        Stack(int size) {
            arr = new char[size];
            top = -1;
        }
        void push(char ch) {
            arr[++top] = ch;
        }
        char pop() {
            return arr[top--];
        }
        boolean isEmpty() {
            return top == -1;
        }
        char peek() {
            return arr[top];
        }
    }
    // Check Parenthesis Matching
    // Check parenthesis while ignoring strings and comments
    static void checkExpression(String expression) {

        Stack stack = new Stack(expression.length());
        boolean inString = false;
        boolean inChar = false;
        boolean inSingleComment = false;
        boolean inMultiComment = false;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Handle single-line comments
            if (inSingleComment) {
                if (ch == '\n') {
                    inSingleComment = false;
                }
                continue;
            }
            // Handle multi-line comments
            if (inMultiComment) {
                if (ch == '*' && i + 1 < expression.length()
                        && expression.charAt(i + 1) == '/') {
                    inMultiComment = false;
                    i++;
                }
                continue;
            }
            // Handle double-quoted strings
            if (inString) {
                if (ch == '\\' && i + 1 < expression.length()) {
                    i++;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            // Handle character literals
            if (inChar) {
                if (ch == '\\' && i + 1 < expression.length()) {
                    i++;
                } else if (ch == '\'') {
                    inChar = false;
                }
                continue;
            }
            // Detect the beginning of comments
            if (ch == '/' && i + 1 < expression.length()) {

                char next = expression.charAt(i + 1);

                if (next == '/') {
                    inSingleComment = true;
                    i++;
                    continue;
                }

                if (next == '*') {
                    inMultiComment = true;
                    i++;
                    continue;
                }
            }
            // Detect the beginning of strings and characters
            if (ch == '"') {
                inString = true;
                continue;
            }
            if (ch == '\'') {
                inChar = true;
                continue;
            }
            // Push opening brackets into the stack
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            }

            // Check closing brackets
            else if (ch == ')' || ch == '}' || ch == ']') {

                // Detect extra closing brackets
                if (stack.isEmpty()) {
                    System.out.println("Invalid Expression!");
                    System.out.println("Error: Extra closing bracket '" + ch + "'");
                    System.out.println("Reason: Missing opening bracket.");
                    System.out.println("Position: " + (i + 1));
                    return;
                }
                char top = stack.pop();

                // Detect mismatched brackets
                if ((ch == ')' && top != '(') ||
                    (ch == '}' && top != '{') ||
                    (ch == ']' && top != '[')) {

                    System.out.println("Invalid Expression!");
                    System.out.println("Error: Mismatched brackets");
                    System.out.println("Opening bracket: " + top);
                    System.out.println("Closing bracket: " + ch);
                    System.out.println("Position: " + (i + 1));
                    return;
                }
            }
        }
        // Check for unclosed strings or comments
                if (inString || inChar || inMultiComment) {
                   System.out.println("Invalid Expression!");
                   return;
        }
        // Check for missing closing brackets
        if (stack.isEmpty()) {
            System.out.println("Valid Expression!");
        } else {
            System.out.println("Invalid Expression!");

            char opening = stack.peek();

            if (opening == '(') {
                System.out.println("Missing closing bracket: )");
            } else if (opening == '{') {
                System.out.println("Missing closing bracket: }");
            } else if (opening == '[') {
                System.out.println("Missing closing bracket: ]");
            }

            System.out.println("Position to insert closing bracket: "
                    + (expression.length() + 1));
        }
    } 
    // Automatically correct unmatched and mismatched brackets
    static String correctExpression(String expression) {

        Stack stack = new Stack(expression.length());
        StringBuilder corrected = new StringBuilder();
        boolean inString = false;
        boolean inChar = false;
        boolean inSingleComment = false;
        boolean inMultiComment = false;

        // Check every character in the expression
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Handle single-line comments
            if (inSingleComment) {
                corrected.append(ch);

                if (ch == '\n') {
                    inSingleComment = false;
                }
                continue;
            }

            // Handle multi-line comments
            if (inMultiComment) {
                corrected.append(ch);

                if (ch == '*' && i + 1 < expression.length()
                        && expression.charAt(i + 1) == '/') {
                    corrected.append('/');
                    i++;
                    inMultiComment = false;
                }
                continue;
            }
            // Handle double-quoted strings
            if (inString) {
                corrected.append(ch);
                if (ch == '\\' && i + 1 < expression.length()) {
                    corrected.append(expression.charAt(++i));
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            // Handle character literals
            if (inChar) {
                corrected.append(ch);

                if (ch == '\\' && i + 1 < expression.length()) {
                    corrected.append(expression.charAt(++i));
                } else if (ch == '\'') {
                    inChar = false;
                }
                continue;
            }
            // Detect the beginning of comments
            if (ch == '/' && i + 1 < expression.length()) {
                char next = expression.charAt(i + 1);

                if (next == '/') {
                    corrected.append("//");
                    i++;
                    inSingleComment = true;
                    continue;
                }
                if (next == '*') {
                    corrected.append("/*");
                    i++;
                    inMultiComment = true;
                    continue;
                }
            }
            // Detect strings and character literals
            if (ch == '"') {
                inString = true;
                corrected.append(ch);
                continue;
            }
            if (ch == '\'') {
                inChar = true;
                corrected.append(ch);
                continue;
            }
            // Store actual opening brackets
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
                corrected.append(ch);
            }

            // Handle actual closing brackets
            else if (ch == ')' || ch == '}' || ch == ']') {

                // Ignore extra closing brackets
                if (stack.isEmpty()) {
                    continue;
                }
                char opening = stack.peek();
                char expected = matchingBracket(opening);

                // Replace incorrect closing bracket
                if (ch != expected) {
                    corrected.append(expected);
                } else {
                    corrected.append(ch);
                }

                stack.pop();
            }

            // Keep all other characters unchanged
            else {
                corrected.append(ch);
            }
        }

        // Add all missing closing brackets
        while (!stack.isEmpty()) {
            corrected.append(matchingBracket(stack.pop()));
        }
        return corrected.toString();
    }

    // Return the matching closing bracket
    static char matchingBracket(char ch) {
        if (ch == '(') {
            return ')';
        } else if (ch == '{') {
            return '}';
        } else {
            return ']';
        }
    }
}