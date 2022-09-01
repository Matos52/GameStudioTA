package sk.tuke.gamestudio.sudoku.core;

import java.util.Random;

public class Field {

    private int rowCount;
    private int columnCount;

    //Cas kedy zacala hra
    private long startMillis;

    Tile[][] tiles;

    private GameState state = GameState.PLAYING;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        this.tiles = new Tile[rowCount][columnCount];
        this.generateField();
        this.populateFieldWithRandomNumbers();
        while(!canBeSolved()) {
            this.generateField();
            this.populateFieldWithRandomNumbers();
        }
        recleanField();
    }

    //Vygeneruje pole s hodnotami 0
    public void generateField() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                tiles[r][c] = new Tile(0,r,c);
            }
        }

        startMillis = System.currentTimeMillis();
    }

    //Vygenerovane pole doplni o random cisla
    public void populateFieldWithRandomNumbers() {
        Random rand = new Random();
        int count = 0;
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                int temp = rand.nextInt(1,10);
                if(validationOfTile(temp,r,c)) {
                    count++;
                    if(count < 35) {
                        tiles[r][c] = new Tile(temp,r,c);
                    }
                }
            }
        }
    }

    //Precisti pole ktore uz je skontrolovane cez Solver a da sa vyhrat. Nahodne v nom vygeneruje polia s nulami.
    public void recleanField() {
        Random r = new Random();
        boolean checker = true;
        int count = 0;
        while(checker) {
            int tempRow = r.nextInt(9);
            int tempColumn = r.nextInt(9);
            if(tiles[tempRow][tempColumn].getValue() != 0) {
                tiles[tempRow][tempColumn].setValue(0);
                count++;
            }

            if(count == 2) {
                checker = false;
            }
        }
    }

    //Finalna funkcia na validaciu vlozeneho cisla
    public boolean validationOfTile(int value, int rowIndex, int columnIndex) {
        if(!rowCheck(value, rowIndex)) {
            return false;
        }
        if(!columnCheck(value, columnIndex)) {
            return false;
        }
        if(!squareCheck(value, rowIndex, columnIndex)) {
            return false;
        }
        return true;
    }

    public boolean rowCheck(int value, int rowIndex) {
        for (int c = 0; c < columnCount; c++) {
            if(tiles[rowIndex][c].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    public boolean columnCheck(int value, int columnIndex) {
        for (int r = 0; r < rowCount; r++) {
            if(tiles[r][columnIndex].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    //kontrola ci hodnota sa uz nachadza v 3x3 stvorceku
    private boolean squareCheck(int value, int rowCheck, int columnCheck){
        int row = -1; //index riadku od ktoreho zacne iteracia
        int col = -1; //index stlpca od ktoreho zacne iteracia

        //riadky dostanu hodnotu podla hodnoty row
        if (rowCheck < 3)  {
            row = 0;
        }
        else if(rowCheck >= 3 && rowCheck < 6) {
            row = 3;
        }
        else if(rowCheck >= 6 && rowCheck < 9) {
            row = 6;
        }

        //stlpce dostanu hodnotu podla hodnoty col
        if (columnCheck < 3) {
            col = 0;
        }
        else if(columnCheck >= 3 && columnCheck < 6) {
            col = 3;
        }
        else if(columnCheck >= 6 && columnCheck < 9) {
            col = 6;
        }
        //teraz vieme v ktorom stlpci by sme mali skontrolovat hodnotu
        int limitCols = col+3; //kde iteracia stlpca konci
        int holdCols = col;    //na prepisanie hodnoty stlca v kazdom riadku
        for(int limitRows = row+3; row < limitRows; row++){
            for(col = holdCols; col < limitCols; col++){
                if (tiles[row][col].getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    //Solver na zistenie ci sa da hra s tymito nahodnymi cislami v poli vyhrat
    public boolean canBeSolved() {

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                if (tiles[r][c].getValue() == 0) {
                    for (int numberToTry = 1; numberToTry <= rowCount; numberToTry++) {
                        if (validationOfTile(numberToTry, r, c)) {
                            tiles[r][c] = new Tile(numberToTry,r,c);

                            if (canBeSolved()) {
                                return true;
                            }
                            else {
                                tiles[r][c] = new Tile(0,r,c);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSolved() {
        int count = 0;
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                if(tiles[r][c].getValue() != 0) {
                    count++;
                }
            }
        }
        if(count == (rowCount * columnCount)) {
            return true;
        }
        return false;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTile(int value, int row, int col) {
        tiles[row][col] = new Tile(value, row, col);
    }

    public void unSetTile(int row, int col) {
        tiles[row][col] = new Tile(0, row, col);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getPlayTimeInSeconds(){
        return (int) ((System.currentTimeMillis() - startMillis)/1000);
    }

    public int getScore() {
        return rowCount * columnCount * 10 - getPlayTimeInSeconds();
    }


}
