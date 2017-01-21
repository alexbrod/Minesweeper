package alexbrod.minesweeper.bl;

/**
 * Created by Alex Brod on 12/4/2016.
 */

public interface GameInterfaceListener {
    enum CELL_CONTENT {
        EMPTY,
        MINE,
        NUMBER
    }

    void onCellRevealed(int row, int col, CELL_CONTENT cellContent );
    void onSetFlag(int row, int col);
    void onClearFlag(int row, int col);
    void onGameOver();
    void onVictory();
    void updateTimeView(int secondsPassed);
    void updateNumOfMinesView(int mines);
    void onCellCovered(int row, int col);
}
