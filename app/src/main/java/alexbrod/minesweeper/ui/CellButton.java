package alexbrod.minesweeper.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import alexbrod.minesweeper.R;


/**
 * Created by Alex Brod on 11/24/2016.
 */

public class CellButton extends ImageButton implements View.OnClickListener, View.OnLongClickListener{

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

    // -------------------- getters and setters --------------------------------------

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

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }

    @Override
    public void onClick(View view) {
        cellButtonOnClickListener.cellButtonOnClick(this);
    }

    @Override
    public boolean onLongClick(View view) {
        return cellButtonOnClickListener.cellButtonOnLongClick(this);
    }

    public void defineNumberTexture(int textVal){
        switch (textVal){
            case 1:
                setImageResource(R.drawable.number_1);
                break;
            case 2:
                setImageResource(R.drawable.number_2);
                break;
            case 3:
                setImageResource(R.drawable.number_3);
                break;
            case 4:
                setImageResource(R.drawable.number_4);
                break;
            case 5:
                setImageResource(R.drawable.number_5);
                break;
            case 6:
                setImageResource(R.drawable.number_6);
                break;
            case 7:
                setImageResource(R.drawable.number_7);
                break;
            case 8:
                setImageResource(R.drawable.number_8);
                break;

        }
        setScaleType(ScaleType.CENTER_INSIDE);
    }
}
