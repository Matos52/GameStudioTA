package sk.tuke.gamestudio.minesweeper.core;

import sk.tuke.gamestudio.minesweeper.core.Clue;
import sk.tuke.gamestudio.minesweeper.core.Mine;

import sk.tuke.gamestudio.minesweeper.core.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    public void testTile() {
        Tile t = new Mine();
        assertInstanceOf(Mine.class, t, "Mina by mala byt typu Mine!");

        int expectedValue = 10;
        Clue c = new Clue(expectedValue);
        t = c;
        assertInstanceOf(Clue.class, t, "Clue by mala byt typu Clue!");
        assertEquals(expectedValue, c.getValue());
    }
}