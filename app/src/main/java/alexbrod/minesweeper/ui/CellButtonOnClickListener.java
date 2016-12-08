package alexbrod.minesweeper.ui;

import alexbrod.minesweeper.ui.CellButton;

/**
 * Created by Alex Brod on 12/3/2016.
 */

public interface CellButtonOnClickListener  {
    void cellButtonOnClick(CellButton b);
    boolean cellButtonOnLongClick(CellButton b);
}
