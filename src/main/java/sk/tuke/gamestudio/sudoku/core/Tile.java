package sk.tuke.gamestudio.sudoku.core;

public class Tile {

    private int value;
    private int positionRow;
    private int positionColumn;

    public Tile(int value, int positionRow, int positionColumn) {
        this.value = value;
        this.positionRow = positionRow;
        this.positionColumn = positionColumn;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPositionRow() {
        return positionRow;
    }

    public void setPositionRow(int positionRow) {
        this.positionRow = positionRow;
    }

    public int getPositionColumn() {
        return positionColumn;
    }

    public void setPositionColumn(int positionColumn) {
        this.positionColumn = positionColumn;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}
