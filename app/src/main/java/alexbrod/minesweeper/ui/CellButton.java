package alexbrod.minesweeper.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;


/**
 * Created by Alex Brod on 11/24/2016.
 */

public class CellButton extends Button implements View.OnClickListener, View.OnLongClickListener{

    private int col;
    private int row;
    private CellButtonOnClickListener cellButtonOnClickListener;
    private GradientDrawable gradientDrawable;

    public CellButton(Context context) {
        super(context);
        setOnClickListener(this);
        setOnLongClickListener(this);
        gradientDrawable = new GradientDrawable();
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

    public void setCellButtonOnClickListener(CellButtonOnClickListener cellButtonOnClickListener) {
        this.cellButtonOnClickListener = cellButtonOnClickListener;
    }

    @Override
    public void onClick(View view) {
        cellButtonOnClickListener.cellButtonOnClick(this);
    }

    public void defineTextColor(int textVal){
        switch (textVal){
            case 1:
                setTextColor(Color.BLUE);
                break;
            case 2:
                setTextColor(Color.GREEN);
                break;
            case 3:
                setTextColor(Color.RED);
                break;
            case 4:
                setTextColor(Color.YELLOW);  //dark blue
                break;
            case 5:
                setTextColor(Color.CYAN);
                break;
            case 6:
                setTextColor(Color.DKGRAY);
                break;
            case 7:
                setTextColor(Color.MAGENTA);
                break;
            case 8:
                setTextColor(Color.BLACK);
                break;

        }
    }

    @Override
    public boolean onLongClick(View view) {
        return cellButtonOnClickListener.cellButtonOnLongClick(this);
    }

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }
}
