import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NFA {
    private Set<String> currentStates;

    public NFA() {
        currentStates = new HashSet<>();
        currentStates.add("q0"); // Start state
    }

    public void transition(char input) {
        Set<String> nextStates = new HashSet<>();

        for (String state : currentStates) {
            switch (state) {
                case "q0":
                    if (input == '1')
                        nextStates.add("q1");
                    if (input == '0')
                        nextStates.add("q2"); // Can directly reach an accepting state
                    break;
                case "q1":
                    if (input == '0')
                        nextStates.add("q3");
                    break;
                case "q2":
                    if (input == '0')
                        nextStates.add("q2"); // Stays in q2 (accepting state)
                    break;
                case "q3":
                    if (input == '0')
                        nextStates.add("q2"); // Moves to another accepting state
                    break;
            }
        }

        currentStates = nextStates;
    }

    public boolean isAccepted() {
        return currentStates.contains("q2") || currentStates.contains("q3"); // At least one accepting state
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a binary string: ");
        String input = scanner.next();
        scanner.close();

        NFA nfa = new NFA();

        for (char c : input.toCharArray()) {
            if (c != '0' && c != '1') {
                System.out.println("Invalid input! Only '0' and '1' allowed.");
                return;
            }
            nfa.transition(c);
        }

        if (nfa.isAccepted()) {
            System.out.println("Accepted: The string ends in '00' or '10'.");
        } else {
            System.out.println("Rejected: The string does not end in '00' or '10'.");
        }
    }
}
