public class BattleBoatsBoard {
    private Cell[][] board;
    private long hv;
    private Boats b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
    private Boats[] boatList;
    private int col, row, shots, turns, level, droneUses, missileUses;
    private boolean clear = false;//flag for cell occupancy

    //getters and setters
    public Boats[] getBoatList() { return boatList; }

    public int getShots() { return shots; }

    public int getTurns() { return turns; }

    public int getLevel() { return level; }

    public int getDroneUses() { return droneUses; }

    public int getMissileUses() { return missileUses; }
    
    
    public BattleBoatsBoard(int lvl) { // standard = 1, expert = 2
        if (lvl == 1) {
            board = new Cell[8][8];
            b1 = new Boats(5, '1');
            b2 = new Boats(4, '2');
            b3 = new Boats(3, '3');
            b4 = new Boats(3, '4');
            b5 = new Boats(2, '5');

            boatList = new Boats[]{b1, b2, b3, b4, b5};

            populate(8);
            this.level = lvl;

        } else if (lvl == 2) {
            board = new Cell[12][12];
            b1 = new Boats(5, '1');
            b2 = new Boats(5, '2');
            b3 = new Boats(4, '3');
            b4 = new Boats(4, '4');
            b5 = new Boats(3, '5');
            b6 = new Boats(3, '6');
            b7 = new Boats(3, '7');
            b8 = new Boats(3, '8');
            b9 = new Boats(2, '9');
            b10 = new Boats(2, '0');

            boatList = new Boats[]{b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};

            populate(12);
            this.level = lvl;

        } else {
            System.out.println("invalid entry.");
        }
    }//constructor

    public void populate(int type) {//fills cells with '-' and 'B' based on type, enters coordinates in boats

        for (int i = 0; i < type; i++) {
            for (int j = 0; j < type; j++) {
                Cell cell = new Cell(i, j, '-');
                board[i][j] = cell;
            }
        }

        for (Boats boats : boatList) {//placing the boats
            hv = (Math.round(Math.random()));//either 1 or 0
            if (hv == 1) {//vertical
                while (!clear) {
                    row = (int) Math.floor(Math.random() * (type - boats.getSize()));
                    col = (int) Math.floor(Math.random() * type);
                    for (int m = row; m < (row + boats.getSize()); m++) {
                        if (board[m][col].get_status() == '-') {
                            clear = true;
                        } else {
                            clear = false;
                            break;
                        }
                    }
                }
                clear = false;
                for (int p = 0; p < (boats.getSize()); p++) {//entering coordinates in boat object
                    boats.getBoat()[p][0] = row + p;
                    boats.getBoat()[p][1] = col;
                    board[row + p][col].set_status('B');//set cell status
                    board[row + p][col].set_boatId(boats.getId());//document boat id
                }
            } else {//horizontal
                while (!clear) {
                    col = (int) Math.floor(Math.random() * (type - boats.getSize()));
                    row = (int) Math.floor(Math.random() * type);
                    for (int n = col; n < (col + boats.getSize()); n++) {
                        if (board[row][n].get_status() == '-') {
                            clear = true;
                        } else {
                            clear = false;
                            break;
                        }
                    }
                }
                clear = false;
                for (int p = 0; p < (boats.getSize()); p++) {
                    boats.getBoat()[p][0] = row;
                    boats.getBoat()[p][1] = col + p;
                    board[row][col + p].set_status('B');
                    board[row][col + p].set_boatId(boats.getId());
                }
            }
        }
    }//end populate

    public void fire(int x, int y) {
        turns++;
        if (x > board.length - 1 || y > board[0].length - 1 || board[x][y].get_status() == 'H' || board[x][y].get_status() == 'M') { //if spot is out of bounds or already hit
            System.out.println("Penalty");
            turns++;
        } else if (board[x][y].get_status() == '-') { // if spot has no boat
            System.out.println("Miss");
            board[x][y].set_status('M');
            shots++;
        } else {
            for (int a = 0; a < boatList.length; a++){ // if hit, check which boat was hit, then remove square from hit boat
                Boats boat = boatList[a];
                for (int i = 0; i < boat.getBoat().length; i++) {
                    if (boat.getBoat()[i][0] == x && boat.getBoat()[i][1] == y) {
                        board[x][y].set_boatId('-');

                        int[][] copy = new int[boat.getBoat().length - 1][boat.getBoat()[0].length]; //removes destroyed cell from boat array
                        for (int j = 0, k = 0; j < boat.getBoat().length; j++) {
                            if (j == i) { //idx to be removed
                                continue;
                            } else {
                                copy[k][0] = boat.getBoat()[j][0];
                                copy[k][1] = boat.getBoat()[j][1];
                                k++;
                            }
                        }
                        boat.setBoat(copy);

                        if (boat.getBoat().length == 0) { // if all spots hit from a boat, boat is sunk and removed from boatList
                            Boats[] newBoatList = new Boats[boatList.length-1];
                            for (int l=0, m=0; l < boatList.length; l++){
                                if (l == a){
                                    continue;
                                } else {
                                    newBoatList[m] = boatList[l];
                                    m++;
                                }
                            }
                            boatList = newBoatList;
                            System.out.println("Boat " + boat.getId() + " Sunk!");
                            System.out.println(boatList.length + " boats remaining.");
                        } else {
                            System.out.println("Hit");
                        }
                        board[x][y].set_status('H');
                        shots++;
                        return;
                    }

                }
            }

        }
    }

    public int missile(int x, int y) {
        if (x > board.length - 1 || x < 0 || y > board[0].length - 1 || y < 0){ //if out of bounds
            return -1;
        } else { // if in bounds
            int newX, newY;
            int adjustedTurns = turns + 1;
            for (int i = -1; i <= 1; i++) { //loop covers all spots in a 3x3 square with x, y being the center
                for (int j = -1; j <= 1; j++) {
                    newX = x + i;
                    newY = y + j;
                    if (newX >= 0 && newY >= 0 &&
                            newX < board.length && newY < board[0].length &&
                            board[newX][newY].get_status() != 'H' && board[newX][newY].get_status() != 'M') {
                        fire(newX, newY);
                    }
                }
            }
            missileUses++;
            turns = adjustedTurns;
            return 1;
        }
    }

    public int drone(int direction, int index) {
        if ((direction == 0 && (index > board.length - 1 || index < 0)) ||
                ((direction == 1 && (index > board[0].length - 1 || index < 0)))){ // if out of bounds
            return -1;
        } else { //if in bounds
            int count = 0;
            if (direction == 0) { //row
                for (int i = 0; i < board.length; i++) {
                    if (board[index][i].get_status() == 'B' || board[index][i].get_status() == 'H') { //finds spots with boats (hidden or already hit)
                        count++;
                    }
                }
            }

            if (direction == 1) { //column
                for (int i = 0; i < board[0].length; i++) {
                    if (board[i][index].get_status() == 'B' || board[i][index].get_status() == 'H') {
                        count++;
                    }
                }
            }
            if (direction == 0){
                System.out.println("There are " + count + " squares with detected ships in this row.");
            } else {
                System.out.println("There are " + count + " squares with detected ships in this column.");
            }

            droneUses++;
            turns++;
            return 1;
        }
    }

    public void display() { //displays board for user
        String out = "";
        for (Cell[] cells : this.board) {
            out += "\n";
            for (int j = 0; j < this.board[0].length; j++) {
                switch (cells[j].get_status()) {
                    case '-':
                    case 'B': {
                        out += '*';
                        break;
                    }//hasn't been guessed
                    case 'H': {
                        out += 'X';
                        break;
                    }//guessed, boat present
                    case 'M': {
                        out += 'O';
                        break;
                    }//guessed, no boat
                    default: {System.out.println("invalid status");}
                }
                out += " ";
            }
        }
        System.out.println(out);
    }//end display

    public void print() { //displays board for debugging
        String out = "";
        for (Cell[] cells : this.board) {
            out += "\n";
            for (int j = 0; j < this.board[0].length; j++) {
                if (cells[j].get_status() != 'B') {
                    out += cells[j].get_status() +" ";
                } else {
                    out += cells[j].get_boatId()+" ";
                }
            }
        }
        System.out.print(out +"\n\n");
        for (Boats boats : this.boatList) {//prints boat coordinates
            System.out.println(boats);
        }
    }//end print

    public static void main(String[] args){//testing
        BattleBoatsBoard test = new BattleBoatsBoard(1);
        test.print();
        test.display();

        test.fire(0,0);
        test.fire(0,1);
        test.fire(2,2);

        test.missile(7,7);
        test.drone(0, 0);
        test.print();
        test.display();
    }
}//close class

