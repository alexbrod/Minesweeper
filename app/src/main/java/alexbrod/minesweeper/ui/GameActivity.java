package alexbrod.minesweeper.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.GameBoard;
import alexbrod.minesweeper.bl.Mine;
import alexbrod.minesweeper.bl.Number;


public class GameActivity extends AppCompatActivity {
    private int columnCount = 4;
    private int rowCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        relativeLayout.addView(gridLayout);


        GameBoard gb = new GameBoard(rowCount,columnCount,5);
        gb.setMinesInRandomCells(0,rowCount * columnCount);
        for (int i = 0; i < rowCount ; i++){
            for (int j = 0; j < columnCount; j++) {
                CellButton cellButton = new CellButton(this);
                cellButton.setPosition(i,j);
                if(!gb.getGameMatrix()[i][j].isRevealed()){
                    continue;
                }
                if (gb.getGameMatrix()[i][j] instanceof Mine) {
                    cellButton.setText("mine");
                }
                else if(gb.getGameMatrix()[i][j] instanceof Number){
                    cellButton.setText(((Number)gb.getGameMatrix()[i][j]).getValue() + "");
                }
                else{
                    cellButton.setText(i + "," + j);
                }
                gridLayout.addView(cellButton);
            }
        }

    }
}
