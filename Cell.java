public class Cell {
    public int row, col;
    public char status, boatId;

    public char get_status() {
        return this.status;
    }
    public void set_status(char c) {
        this.status = c;
    }
    public void set_boatId(char id) {
        this.boatId = id;
    }
    public char get_boatId() {
        return this.boatId;
    }

    public Cell(int row, int col, char status) {
        this.row = row;
        this.col = col;
        this.status = status;
    }

}
