package alexbrod.minesweeper.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import alexbrod.minesweeper.controller.CellButtonOnClickListener;


/**
 * Created by Alex Brod on 11/24/2016.
 */

public class CellButton extends Button {

    private int col;
    private int row;
    private CellButtonOnClickListener listener;

    public CellButton(Context context) {
        super(context);
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setListener(CellButtonOnClickListener listener) {
        this.listener = listener;
    }
}
