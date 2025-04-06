import java.util.Scanner;

enum State {
    OFF, ON
}

public class LightSwitchFSM {
    private State currentState;

    public LightSwitchFSM() {
        this.currentState = State.OFF; // Start in OFF state
    }

    public void toggle() {
        if (currentState == State.OFF) {
            currentState = State.ON;
        } else {
            currentState = State.OFF;
        }
        System.out.println("Light is now: " + currentState);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LightSwitchFSM lightSwitch = new LightSwitchFSM();

        System.out.println("Light Switch FSM (Type 'toggle' to switch, 'exit' to quit)");

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.next().toLowerCase();

            if (input.equals("toggle")) {
                lightSwitch.toggle();
            } else if (input.equals("exit")) {
                System.out.println("Exiting Light Switch FSM...");
                break;
            } else {
                System.out.println("Invalid command! Type 'toggle' or 'exit'.");
            }
        }

        scanner.close();
    }
}
