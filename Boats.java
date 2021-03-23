public class Boats {
    private int size;
    private int[][] boat;
    private char id;

    public int getSize() {
        return this.size;
    }
    public int[][] getBoat() {
        return this.boat;
    }
    public void setBoat(int[][] boat) {
        this.boat = boat;
    }
    public char getId() {
        return this.id;
    }

    public Boats(int size, char id) {
        this.size = size;
        this.id = id;
        switch (size) {
            case 2 : {boat = new int[2][2]; break;}
            case 3 : {boat = new int[3][2]; break;}
            case 4 : {boat = new int[4][2]; break;}
            case 5 : {boat = new int[5][2]; break;}
            default : {System.out.println("invalid entry");}
        }//end switch
    }//end constructor



    public boolean vertical(Boats b) {
        return (this.getBoat()[0] != this.getBoat()[1]);
    }

    public String toString() {
        String out = "Boat "+this.getId()+" coordinates: (row, column)\n";
        for (int i = 0; i < this.getBoat().length; i ++) {
            out += "(";
            for (int j = 0; j < 2; j ++) {
                if (j == 1) {out += this.getBoat()[i][j];}
                else {
                out += this.getBoat()[i][j]+",";}
            }
            out += ") ";
        }
        out += "\n";
        return out;
    }
}
