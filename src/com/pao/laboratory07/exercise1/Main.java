package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul de rulare: 0 = demo, 1 = input manual");
        int optiune = Integer.parseInt(scanner.nextLine().trim());

        List<String> liniiInput;
        if (optiune == 0) {
            liniiInput = demoInput();
        } else {
            liniiInput = new ArrayList<>();
            System.out.println("Introdu starea initiala:");
            liniiInput.add(scanner.nextLine().trim());
            System.out.println("Introdu comenzile: next, cancel, undo, QUIT");
            while (scanner.hasNextLine()) {
                String linie = scanner.nextLine().trim();
                liniiInput.add(linie);
                if ("QUIT".equals(linie)) {
                    break;
                }
            }
        }

        OrderState initialState = OrderState.valueOf(liniiInput.get(0));
        Order order = new Order(initialState);
        System.out.println("Initial order state: " + order.getState());

        for (int i = 1; i < liniiInput.size(); i++) {
            String commandText = liniiInput.get(i).trim();
            if ("QUIT".equals(commandText)) {
                System.out.println("User quit the program.");
                return;
            }

            OrderCommand command = OrderCommand.valueOf(commandText);
            switch (command) {
                case next -> {
                    try {
                        order.nextState();
                        System.out.println("Order state updated to: " + order.getState());
                    } catch (OrderIsAlreadyFinalException e) {
                        System.out.println("Order is already in a final state.");
                    }
                }
                case cancel -> {
                    try {
                        order.cancel();
                        System.out.println("Order has been canceled.");
                    } catch (CannotCancelFinalOrderException e) {
                        System.out.println("Cannot cancel a final state order.");
                    }
                }
                case undo -> {
                    try {
                        order.undoState();
                        System.out.println("Order state reverted to: " + order.getState());
                    } catch (CannotRevertInitialOrderStateException e) {
                        System.out.println("Cannot undo the initial order state.");
                    }
                }
            }
        }
    }

    private static List<String> demoInput() {
        return Arrays.asList(
                "PLACED",
                "next",
                "next",
                "cancel",
                "undo",
                "QUIT"
        );
    }
}
