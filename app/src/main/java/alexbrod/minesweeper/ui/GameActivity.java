package alexbrod.minesweeper.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.Cell;
import alexbrod.minesweeper.bl.GameBoard;
import alexbrod.minesweeper.bl.GameInterfaceListener;
import alexbrod.minesweeper.bl.Mine;
import alexbrod.minesweeper.bl.Number;


public class GameActivity extends AppCompatActivity implements CellButtonOnClickListener, GameInterfaceListener {
    private final int GAME_BOARD_RETIO = 2;
    private int columnCount = 5;
    private int rowCount = 5;
    private int minesNum = 5;

    private GameBoard gameBoard;
    private GridLayout gridLayout;
    private RelativeLayout relativeLayout;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameBoard = new GameBoard(rowCount, columnCount, minesNum);
        setContentView(R.layout.activity_game);

        //set layout parameters
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        gridLayout = new GridLayout(this);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        dm = new DisplayMetrics();
        display.getMetrics(dm);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);

        //start listening to bl events
        gameBoard.setGameInterfaceListener(this);

        initButtonsGrid();

    }


    private void initButtonsGrid(){

        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        relativeLayout.addView(gridLayout);
        int cellButtonWidth = dm.widthPixels/(columnCount + 1);
        int cellButtonHeight = (dm.heightPixels/GAME_BOARD_RETIO)/(rowCount + 1);
        ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(cellButtonWidth, cellButtonHeight);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.GRAY);
        gd.setCornerRadius(5);
        gd.setStroke(1, Color.BLACK);

        for (int i = 0; i < rowCount; i++){
            for (int j = 0; j < columnCount; j++) {
                CellButton cellButton = new CellButton(this);
                cellButton.setPosition(i,j);
                cellButton.setLayoutParams(buttonLayoutParams);
                cellButton.setBackground(gd);
                gridLayout.addView(cellButton);
                cellButton.setCellButtonOnClickListener(this);
            }
        }
    }


    @Override
    public void cellButtonOnClick(CellButton b) {
        int row = b.getRow();
        int col = b.getCol();
        Cell cell = gameBoard.getGameBoardView()[row][col];

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(1, Color.BLACK);

        if(!cell.isRevealed()){
            cell.setRevealed(true);
            if(cell instanceof Mine){
                b.setText("mine");
                gd.setColor(Color.RED);
                b.setBackground(gd);
            }
            else if(cell instanceof Number){
                gd.setColor(Color.LTGRAY);
                b.setBackground(gd);
                b.setText("" + ((Number) cell).getValue());
                b.defineTextColor(((Number) cell).getValue());
            }
            else{
                gd.setColor(Color.LTGRAY);
                b.setBackground(gd);
            }
        }
    }

    @Override
    public void onCellRevealed(int row, int col, CELL_CONTENT cellContent) {

    }

    @Override
    public void onSetFlag(int row, int col) {

    }

    @Override
    public void onClearFlag(int row, int col) {

    }

    @Override
    public void onGameOver() {

    }

    @Override
    public void onVictory() {

    }
}
