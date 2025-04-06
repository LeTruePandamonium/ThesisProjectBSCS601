import java.util.Scanner;

public class DFA {
    private String currentState;

    public DFA() {
        this.currentState = "q0"; // Start state
    }

    public void transition(char input) {
        switch (currentState) {
            case "q0":
                if (input == '1')
                    currentState = "q1";
                break;
            case "q1":
                if (input == '1')
                    currentState = "q2";
                else if (input == '0')
                    currentState = "q0";
                break;
            case "q2":
                if (input == '1')
                    currentState = "q2"; // Stays in q2 on '1'
                else if (input == '0')
                    currentState = "q0";
                break;
        }
    }

    public boolean isAccepted() {
        return currentState.equals("q2"); // Final state
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a binary string: ");
        String input = scanner.next();
        scanner.close();

        DFA dfa = new DFA();

        for (char c : input.toCharArray()) {
            if (c != '0' && c != '1') {
                System.out.println("Invalid input! Only '0' and '1' allowed.");
                return;
            }
            dfa.transition(c);
        }

        if (dfa.isAccepted()) {
            System.out.println("Accepted: The string ends in '11'.");
        } else {
            System.out.println("Rejected: The string does not end in '11'.");
        }
    }
}
