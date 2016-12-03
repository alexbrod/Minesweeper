package alexbrod.minesweeper.bl;

/**
 * Created by Alex Brod on 11/26/2016.
 */
public class Cell {
    private boolean isRevealed;

    public Cell(){
        isRevealed = false;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}
