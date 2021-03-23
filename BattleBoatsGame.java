import java.util.Scanner;

public class BattleBoatsGame {
    static boolean gameRunning;
    static String mode;

    public static void main(String[] args) { //main method to run
        Scanner s = new Scanner(System.in);
        System.out.println("Hello welcome to BattleBoats! Please enter 1 to play in standard mode or 2 to play in expert");
        String level = s.next();
        while (!(level.equals("1")) && !(level.equals("2"))) {
            System.out.println("Invalid input. Please enter 1 to play in standard mode or 2 to play in expert.");
            level = s.next();
        }

        int levelInt = Integer.parseInt(level);

        BattleBoatsBoard gameInstance = new BattleBoatsBoard(levelInt);
        gameRunning = true;
        if(levelInt == 1){
            System.out.println("\nThere are 5 boats on the board. Shoot them down to win.");
        } else {
            System.out.println("\nThere are 10 boats on the board. Shoot them down to win.");
        }

        while (gameRunning) { //while not all boats are sunk, one of 4 modes is running: fire, missile, drone, or print. Mode fire is chosen by default.
            if (gameInstance.getBoatList().length == 0){
                System.out.println("You won! Total turns: " + gameInstance.getTurns() + ". Total shots: " + gameInstance.getShots() + ".");
                gameRunning = false;
                break;
            }
            mode = "fire";
            while (mode.equals("fire")) {
                gameInstance.display();
                System.out.println("\nTurn " + (gameInstance.getTurns() + 1) + ". Select x position to fire. Firing out of bounds results in penalty. \n" +
                        "To use powers, enter 'missile' or 'drone'.");

                while (!s.hasNextInt() && (!s.hasNext("missile") && !s.hasNext("drone") && !s.hasNext("print"))){
                    System.out.println("Invalid. Enter x position or use a power.");
                    s.next();
                }
                if (!s.hasNextInt()) {
                    if (s.hasNext("missile")){
                        mode = "missile";
                        s.next();
                        break;
                    } else if (s.hasNext("drone")){
                        mode = "drone";
                        s.next();
                        break;
                    } else if (s.hasNext("print")){
                        mode = "print";
                        s.next();
                        break;
                    } else {
                        System.out.println("Invalid. Enter x position or use a power.");
                        s.next();
                    }
                }

                int x = s.nextInt();

                System.out.println("Turn " + (gameInstance.getTurns() + 1) + ". Select y position to fire. Firing out of bounds results in penalty.");
                while (!(s.hasNextInt())) {
                    System.out.println("Invalid. Enter y position:");
                    s.next();
                }
                int y = s.nextInt();

                gameInstance.fire(x, y);
                if (gameInstance.getBoatList().length == 0) {break;}
            }

            while (mode.equals("missile")){
                if ((gameInstance.getLevel() == 1 && gameInstance.getMissileUses() == 1) ||
                        (gameInstance.getLevel() == 2 && gameInstance.getMissileUses() == 2)){
                    System.out.println("Maximum number of missiles used.");
                } else {
                    boolean validIdx = true;
                    System.out.println("Select x position to fire. Firing missiles out of bounds is not allowed.");
                    int idxX = 0;
                    int idxY = 0;

                    do {
                        while (!s.hasNextInt()) {
                            System.out.println("Invalid. Enter valid x position.");
                            s.next();
                        }
                        idxX = s.nextInt();

                        System.out.println("Select y position to fire. Firing missiles out of bounds is not allowed.");

                        while (!s.hasNextInt()) {
                            System.out.println("Invalid. Enter valid y position.");
                            s.next();
                        }
                        idxY = s.nextInt();

                        int status = gameInstance.missile(idxX, idxY);
                        if (status == -1) {
                            validIdx = false;
                            System.out.println("Out of bounds. Enter different values." +
                                    "\nSelect x position to fire. Firing missiles out of bounds is not allowed.");
                        } else {
                            validIdx = true;
                        }
                    } while (!validIdx);
                    if (gameInstance.getBoatList().length == 0) {break;}
                }
                mode = "fire";
            }

            while (mode.equals("drone")){
                if ((gameInstance.getLevel() == 1 && gameInstance.getDroneUses() == 1) ||
                        (gameInstance.getLevel() == 2 && gameInstance.getDroneUses() == 2)){
                    System.out.println("Maximum number of drones used.");
                } else {
                    boolean validIdx = true;
                    System.out.println("Would you like to scan a row or column? Type in r for row or c for column.");
                    while (!s.hasNext("r") && !s.hasNext("c")){
                        System.out.println("Invalid input. Type in r for row or c for column.");
                        s.next();
                    }
                    if (s.hasNext("r")) {
                        s.next();
                        System.out.println("Which row would you like to scan?");
                        int idx = 0;

                        do {
                            while (!s.hasNextInt()) {
                                System.out.println("Invalid. Enter valid row number.");
                                s.next();
                            }
                            idx = s.nextInt();
                            int status = gameInstance.drone(0, idx);
                            if (status == -1) {
                                validIdx = false;
                                System.out.println("Out of bounds. Enter valid row number.");
                            } else {
                                validIdx = true;
                            }
                        } while (!validIdx);

                    } else if (s.hasNext("c")){
                        s.next();
                        System.out.println("Which column would you like to scan?");
                        int idx = 0;

                        do {
                            while (!s.hasNextInt()) {
                                System.out.println("Invalid. Enter valid column number.");
                                s.next();
                            }
                            idx = s.nextInt();
                            int status = gameInstance.drone(1, idx);
                            if (status == -1) {
                                validIdx = false;
                                System.out.println("Out of bounds. Enter valid row number.");
                            } else {
                                validIdx = true;
                            }
                        } while (!validIdx);
                    }
                }
                mode = "fire";
            }
            while (mode.equals("print")){
                gameInstance.print();
                mode = "fire";
            }
        }
    }
}
