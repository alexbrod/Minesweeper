package alexbrod.minesweeper.bl;

/**
 * Created by Alex Brod on 11/26/2016.
 */
public class Cell {
    private boolean isRevealed;
    private boolean isEmpty;
    private boolean isFlagged;
    private int row;
    private int col;

    public Cell(){
        isRevealed = false;
        isEmpty = true;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean getIsFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
}
