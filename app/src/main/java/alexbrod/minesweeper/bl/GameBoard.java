package alexbrod.minesweeper.bl;

import android.content.Context;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import alexbrod.minesweeper.bl.GameInterfaceListener.CELL_CONTENT;
/**
 * Created by Alex Brod on 11/25/2016.
 */

public class GameBoard {
    private int rows;
    private int cols;
    private Cell[][] gameMatrix;
    private int minesNum;
    private int leftMines;
    private GameInterfaceListener gameInterfaceListener;
    private Timer playTimer;
    private boolean firstCellReveal;
    private int passedTime;
    private SharedPrefManager prefs;


    public GameBoard(Context context) {
        //ask lecturer how can I avoid entering context
        prefs = new SharedPrefManager(context);
        setGameParametersAccordingLevel();
        gameMatrix = new Cell[rows][cols];
        initGameMatrix();
        setMinesInRandomCells();
        firstCellReveal = false;
        passedTime = 0;
        stopTimer();
    }


    private void setGameParametersAccordingLevel(){
        switch (prefs.getLevel()){
            case SharedPrefManager.NOVICE_LEVEL:
                rows = cols = 5;
                minesNum = leftMines = 3;
                break;
            case SharedPrefManager.ADVANCED_LEVEL:
                rows = cols = 7;
                minesNum = leftMines = 7;
                break;
            case SharedPrefManager.EXPERT_LEVEL:
                rows = cols = 10;
                minesNum = leftMines = 10;
                break;
        }
    }

    private void initGameMatrix(){
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                gameMatrix[i][j] = new Cell();
                gameMatrix[i][j].setRow(i);
                gameMatrix[i][j].setCol(j);
            }

        }
    }

    private void setMinesInRandomCells(){
        int index;
        int tmpMinesNum = 0;
        while (tmpMinesNum != minesNum ) {
            index = (int)(Math.random() * (rows * cols));
            if(!(gameMatrix[index/rows][index%rows] instanceof Mine)){
                gameMatrix[index/rows][index%rows] = new Mine();
                setNumbersAroundMine(index/rows,index%rows);
                tmpMinesNum++;
            }
        }
    }

    /*
    private void setMinesInRandomCells(int from, int to){
        if(leftMines <= 0) {
            return;
        }
        if (to - from == 0) {
            return;
        }
        int index = (int)(Math.random()*(to - from) + from);
        gameMatrix[index/rows][index%rows] = new Mine();
        setNumbersAroundMine(index/rows,index%rows);
        leftMines -= 1;
        setMinesInRandomCells(from,index);
        setMinesInRandomCells(++index,to);
    }
    */
    private void setNumbersAroundMine(int mineRow, int mineCol){
        /*
        go through all cells around the mine and put numbers where
        there is no mine, the center cell checked also from convenience
        */
        for (int i = mineRow - 1; i < mineRow + 2; i++) { //3 rows
            for (int j = mineCol - 1; j < mineCol + 2; j++) { //3 cols
                try{
                    if(gameMatrix[i][j] instanceof Number){
                        ((Number)gameMatrix[i][j]).setValue(
                                ((Number)gameMatrix[i][j]).getValue() + 1);
                    }else if(!(gameMatrix[i][j] instanceof Mine)){
                        gameMatrix[i][j] = new Number(1);
                    }
                } catch (IndexOutOfBoundsException e){
                    //do nothing
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }

        }
    }

    public void revealCell(int row, int col){
        if(!firstCellReveal){
            startTimer();
        }
        Cell cell = gameMatrix[row][col];
        if(!cell.isRevealed()){
            if(cell.getIsFlagged()){
                return;
            }
            if(cell instanceof Mine){
                gameMatrix[row][col].setRevealed(true);
                gameInterfaceListener.onCellRevealed(row,col, CELL_CONTENT.MINE);
                gameOver();
                return;
            }
            //check for numbers or empty cells
            checkCellChain(row,col);

            checkVictory();
        }
    }

    private void checkCellChain(int row, int col) {
        Cell cell;
        try{
            cell = gameMatrix[row][col];
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return;
        }
        if(cell.isRevealed() || cell.getIsFlagged()){
            return;
        }
        if(cell.isEmpty()){
            cell.setRevealed(true);
            gameInterfaceListener.onCellRevealed(row,col, CELL_CONTENT.EMPTY);
            checkCellChain(row - 1, col - 1);
            checkCellChain(row - 1, col);
            checkCellChain(row, col - 1);
            checkCellChain(row - 1, col + 1);
            checkCellChain(row + 1, col - 1);
            checkCellChain(row, col + 1);
            checkCellChain(row + 1, col);
            checkCellChain(row + 1, col + 1);
        }else{
            if(cell instanceof Number){
                cell.setRevealed(true);
                gameInterfaceListener.onCellRevealed(row,col, CELL_CONTENT.NUMBER);
            }
            return;
        }

    }

    private void checkVictory() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = gameMatrix[i][j];
                if(!cell.isRevealed() && !(cell instanceof Mine)){
                    return;
                }
            }
        }
        stopTimer();
        gameInterfaceListener.onVictory();

    }

    private void gameOver() {
        //reveal all mines
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = gameMatrix[i][j];
                if(cell instanceof Mine){
                    cell.setRevealed(true);
                    gameInterfaceListener.onCellRevealed(i,j, CELL_CONTENT.MINE);
                }
            }
        }
        stopTimer();
        gameInterfaceListener.onGameOver();

    }

    public void setGameInterfaceListener(GameInterfaceListener gameInterfaceListener) {
        this.gameInterfaceListener = gameInterfaceListener;
    }

    public int getValueOfNumberCell(int row, int col){
        return ((Number)gameMatrix[row][col]).getValue();
    }

    public void flagEvent(int row, int col) {
        Cell cell = gameMatrix[row][col];
        if(cell.isRevealed()){
            return;
        }
        if(cell.getIsFlagged()){
            cell.setFlagged(false);
            gameInterfaceListener.onClearFlag(row, col);
            gameInterfaceListener.updateNumOfMinesView(++leftMines);
        }else{
            cell.setFlagged(true);
            gameInterfaceListener.onSetFlag(row, col);
            gameInterfaceListener.updateNumOfMinesView(--leftMines);
        }
    }

    public void startTimer() {
        //check with lecturer
        firstCellReveal = true;
        passedTime = 0;
        final Handler handler = new Handler();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameInterfaceListener.updateTimeView(++passedTime);
                    }
                });
            }
        };
        playTimer = new Timer();
        playTimer.schedule(timerTask, 1000, 1000);
    }

    private void stopTimer(){
        if(playTimer != null){
            playTimer.cancel();
            playTimer.purge();
        }
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getMinesNum() {
        return minesNum;
    }

    public int getCurrentScore() {
        return passedTime;
    }

    public int getBestScore() {
        return prefs.getBestScore(prefs.getLevel());
    }

    public int getLastScore(){
        return prefs.getLastScore(prefs.getLevel());
    }

    public void setNewScore(String name) {
        prefs.setNewScore(passedTime, name, prefs.getLevel());
    }
}
