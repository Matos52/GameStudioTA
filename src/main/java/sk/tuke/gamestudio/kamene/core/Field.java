package sk.tuke.gamestudio.kamene.core;

import java.util.Random;

public class Field {

    private long startMillis;
    private int rowCount;
    private int columnCount;
    GameState gameState = GameState.PLAYING;

    private boolean justFinished=false;

    Tile[][] tiles;
    //suradnice na sledovanie prazdnej dlazdice
    int emptyTileRow;
    int emptyTileColumn;

    //dve premenne int, vyska a sirka
    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        tiles = new Tile[rowCount][columnCount];
        //generate the field content
        generate();
    }

    public void generate() {
        int value = 1;
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                tiles[r][c] = new Tile(value,r,c);
                value++;
            }
        }
        //na poslednu dlazdicu vlozime hodnotu 0
        tiles[this.getRowCount() - 1][this.getColumnCount() - 1] =
                new Tile(0,this.getRowCount() - 1,this.getColumnCount() - 1);
        emptyTileRow = this.getRowCount() - 1;
        emptyTileColumn = this.getColumnCount() - 1;

        //zamiesa dlazdice v poli
        mixTilesInField();
        startMillis = System.currentTimeMillis();
    }

    public void mixTilesInField() {
        Random r = new Random();

        for (int i = 0; i < 5; i++) {
            switch (r.nextInt(4)) {
                case 0:
                    this.moveDown();
                    break;
                case 1:
                    this.moveUp();
                    break;
                case 2:
                    this.moveRight();
                    break;
                case 3:
                    this.moveLeft();
            }
        }
    }

    public boolean isMovementOfEmptyTilePossible(String move) {
        switch (move) {
            case "down":
                if(emptyTileRow + 1 >= rowCount) {
                    return false;
                } else {
                    return true;
                }
            case "up":
                if(emptyTileRow - 1 < 0) {
                    return false;
                } else {
                    return true;
                }
            case "right":
                if(emptyTileColumn + 1 >= columnCount) {
                    return false;
                } else {
                    return true;
                }
            case "left":
                if(emptyTileColumn - 1 < 0) {
                    return false;
                } else {
                    return true;
                }
        }
        return true;
    }

    public void swapUp() {
        Tile temp = tiles[emptyTileRow][emptyTileColumn];
        tiles[emptyTileRow][emptyTileColumn] = tiles[emptyTileRow - 1][emptyTileColumn];
        tiles[emptyTileRow - 1][emptyTileColumn] = temp;
        emptyTileRow--;
    }

    public void moveUp() {
        if(isMovementOfEmptyTilePossible("up")) {
            swapUp();
            System.out.println("Vykonal som posun dlazdice smerom hore");
        } else {
            System.out.println("Posun dlazdice smerom hore nieje mozny");
        }
    }

    public void swapDown() {
        Tile temp = tiles[emptyTileRow][emptyTileColumn];
        tiles[emptyTileRow][emptyTileColumn] = tiles[emptyTileRow + 1][emptyTileColumn];
        tiles[emptyTileRow + 1][emptyTileColumn] = temp;
        emptyTileRow++;
    }

    public void moveDown() {
        if(isMovementOfEmptyTilePossible("down")) {
            swapDown();
            System.out.println("Vykonal som posun dlazdice smerom dole");
        } else {
            System.out.println("Posun dlazdice smerom hore nieje mozny");
        }
    }

    public void swapLeft() {
        Tile temp = tiles[emptyTileRow][emptyTileColumn];
        tiles[emptyTileRow][emptyTileColumn] = tiles[emptyTileRow][emptyTileColumn - 1];
        tiles[emptyTileRow][emptyTileColumn - 1] = temp;
        emptyTileColumn--;
    }

    public void moveLeft() {
        if(isMovementOfEmptyTilePossible("left")) {
            swapLeft();
            System.out.println("Vykonal som posun dlazdice smerom dolava");
        } else {
            System.out.println("Posun dlazdice smerom dolava nieje mozny");
        }
    }

    public void swapRight() {
        Tile temp = tiles[emptyTileRow][emptyTileColumn];
        tiles[emptyTileRow][emptyTileColumn] = tiles[emptyTileRow][emptyTileColumn + 1];
        tiles[emptyTileRow][emptyTileColumn + 1] = temp;
        emptyTileColumn++;
    }

    public void moveRight() {
        if(isMovementOfEmptyTilePossible("right")) {
            swapRight();
            System.out.println("Vykonal som posun dlazdice smerom doprava");
        } else {
            System.out.println("Posun dlazdice smerom doprava nieje mozny");
        }
    }

    public boolean isSolved() {
        int count = 1;
        int idealCount = 1;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if(tiles[i][j].getValue() == idealCount) {
                    idealCount++;
                }
                count++;
            }
        }

        if(tiles[rowCount - 1][columnCount - 1].getValue() == 0) {
            idealCount++;
        }
        return count == idealCount;
    }

    public int getPlayingSeconds() {
        return (int) ((System.currentTimeMillis() - startMillis)/1000);
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getEmptyTileRow() {
        return emptyTileRow;
    }

    public void setEmptyTileRow(int emptyTileRow) {
        this.emptyTileRow = emptyTileRow;
    }

    public int getEmptyTileColumn() {
        return emptyTileColumn;
    }

    public void setEmptyTileColumn(int emptyTileColumn) {
        this.emptyTileColumn = emptyTileColumn;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getScore() {
        return rowCount * columnCount * 10 - getPlayingSeconds();
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }

}
