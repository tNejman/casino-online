package pap2024z.z11.Gaming_institution.demos;

import pap2024z.z11.Gaming_institution.exceptions.FullPlayerListException;
import pap2024z.z11.Gaming_institution.game.*;
import pap2024z.z11.Gaming_institution.gamelogic.*;
import pap2024z.z11.Gaming_institution.game.Roulette.Bets;

import java.util.Scanner;
import java.util.Arrays;

public class DemoRoulette {

    public static void main(String[] args) throws FullPlayerListException {
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        Roulette r1 = new Roulette();
        Player p1 = new Player("p1");

        r1.addPlayer(p1);
        p1.joinGame("roulette");

        String line;

        p1.setMoney(1000);
        System.out.println("Welcome to roulette! You have $1000");

        while (true) {
            System.out.println("You have $" + p1.getMoney() + " Type to choose your next move:");
            System.out.println("'exit' - to end the game");
            System.out.println("'bet' - to place a bet");
            line = sc.nextLine();
            if (line.equals("exit")) {
                break;
            }
            try {
                if (line.equals("bet")) {
                    System.out.println("Choose bet");
                    line = sc.nextLine();
                    Bets bet = Roulette.getBet(line);
                    System.out.println("Choose pockets");
                    line = sc.nextLine();
                    int[] pockets;
                    if (line.isEmpty()) {
                        pockets = new int[0];
                    } else {
                        pockets = Arrays.stream(line.split(" "))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                    }
                    System.out.println("Choose how much to bet. You have: $" + p1.getMoney());
                    line = sc.nextLine();
                    int betAmount = Integer.parseInt(line);
                    r1.placeBet(bet, pockets, betAmount);
                    r1.spin();
                    System.out.println("Winning pocket: " + r1.getWinningPocket() + ", "
                            + Roulette.getPocketColor(r1.getWinningPocket()));
                    if (r1.getOutcome() > 0) {
                        System.out.println("You won!");
                    } else {
                        System.out.println("You lose!");
                    }
                }
                System.out.println("You now have: $" + p1.getMoney());
                if (p1.getMoney() == 0) {
                    System.out.println("You lost all your money.");
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Game over.");
        System.exit(0);

    }
}
