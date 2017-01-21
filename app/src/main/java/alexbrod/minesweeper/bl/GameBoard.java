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

    private static final int NOVICE_LEVEL_SIZE = 5;
    private static final int NOVICE_LEVEL_MINES_NUM = 3;
    private static final int ADVANCED_LEVEL_SIZE = 7;
    private static final int ADVANCED_LEVEL_MINES_NUM = 7;
    private static final int EXPERT_LEVEL_SIZE = 10;
    private static final int EXPERT_LEVEL_MINES_NUM = 10;
    private static final long MINE_MODE_DROP_RATE = 1000; // in milliSeconds


    private int rows;
    private int cols;
    private Cell[][] gameMatrix;
    private int minesNum;
    private int leftMines;
    private GameInterfaceListener gameInterfaceListener;
    private Timer gameTimer;
    private Timer minesModeTimer;
    private boolean firstCellReveal;
    private int passedTime;
    private SharedPrefManager prefs;


    public GameBoard(Context context) {
        prefs = SharedPrefManager.getInstance(context);
        setGameParametersAccordingLevel();
        gameMatrix = new Cell[rows][cols];
        initGameMatrix();
        setMinesInRandomCells();
        firstCellReveal = false;
        passedTime = 0;
        stopGameTimer();
    }

    // ------------------------- BL methods ---------------------------------

    private void setGameParametersAccordingLevel(){
        switch (prefs.getLevel()){
            case SharedPrefManager.NOVICE_LEVEL:
                rows = cols = NOVICE_LEVEL_SIZE;
                minesNum = leftMines = NOVICE_LEVEL_MINES_NUM;
                break;
            case SharedPrefManager.ADVANCED_LEVEL:
                rows = cols = ADVANCED_LEVEL_SIZE;
                minesNum = leftMines = ADVANCED_LEVEL_MINES_NUM;
                break;
            case SharedPrefManager.EXPERT_LEVEL:
                rows = cols = EXPERT_LEVEL_SIZE;
                minesNum = leftMines = EXPERT_LEVEL_MINES_NUM;
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

    private void setMineInRandomCell(){
        int index = (int)(Math.random() * (rows * cols));
        int startIndex = index;
        boolean mineDropped = false;
        boolean boardRoundTripComplete = false;
        while (!mineDropped && !boardRoundTripComplete) {
            if(!(gameMatrix[index/rows][index%rows] instanceof Mine)){
                gameMatrix[index/rows][index%rows] = new Mine();
                mineDropped = true;
                gameMatrix[index/rows][index%rows].setRevealed(false);
                gameMatrix[index/rows][index%rows].setEmpty(false);
                gameInterfaceListener.onCellCovered(index/rows,index%rows);
                gameInterfaceListener.updateNumOfMinesView(++leftMines);
                setNumbersAroundMineWithGuiUpdates(index/rows,index%rows);
            }
            index++;
            index = index%(rows * cols);
            if(startIndex == index){
                boardRoundTripComplete = true;
            }
        }
    }

    private void setNumbersAroundMineWithGuiUpdates(int mineRow, int mineCol){
        /*
        go through all cells around the mine and put numbers where
        there is no mine, the center cell checked also from convenience reasons
        */
        for (int i = mineRow - 1; i < mineRow + 2; i++) { //3 rows
            for (int j = mineCol - 1; j < mineCol + 2; j++) { //3 cols
                try{
                    if(gameMatrix[i][j] instanceof Number){
                        ((Number)gameMatrix[i][j]).setValue(
                                ((Number)gameMatrix[i][j]).getValue() + 1);
                        gameMatrix[i][j].setRevealed(false);
                        gameMatrix[i][j].setEmpty(false);
                        gameInterfaceListener.onCellCovered(i,j);
                    }else if(!(gameMatrix[i][j] instanceof Mine)){
                        gameMatrix[i][j] = new Number(1);
                        gameMatrix[i][j].setRevealed(false);
                        gameMatrix[i][j].setEmpty(false);
                        gameInterfaceListener.onCellCovered(i,j);
                    }
                } catch (IndexOutOfBoundsException e){
                    //Tried to check cell that is out of the bounds of the matrix
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void setNumbersAroundMine(int mineRow, int mineCol){
        /*
        go through all cells around the mine and put numbers where
        there is no mine, the center cell checked also from convenience reasons
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
                    //Tried to check cell that is out of the bounds of the matrix
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    private void checkCellChain(int row, int col) {
        Cell cell;
        try{
            cell = gameMatrix[row][col];
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return; //Tried to check cell out of bound of the cell matrix
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
        stopGameTimer();
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
        stopGameTimer();
        gameInterfaceListener.onGameOver();

    }

    // ----------------------- GUI issued methods -------------------------

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

    public void revealCell(int row, int col){
        if(!firstCellReveal){
            startGameTimer();
            firstCellReveal = true;
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

    //-------------------------- timer methods ---------------------

    public void startGameTimer() {
        if(gameTimer != null){
            return;
        }
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
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000, 1000);
    }

    public void stopGameTimer(){
        if(gameTimer != null){
            gameTimer.cancel();
            gameTimer.purge();
            gameTimer = null;
        }
    }

    public void startMinesModeTimer() {
        if(minesModeTimer != null){
            return;
        }
        final Handler handler = new Handler();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setMineInRandomCell();
                    }
                });
            }
        };
        minesModeTimer = new Timer();
        minesModeTimer.schedule(timerTask, MINE_MODE_DROP_RATE, MINE_MODE_DROP_RATE);
    }

    public void stopMinesModeTimer(){
        if(minesModeTimer != null){
            minesModeTimer.cancel();
            minesModeTimer.purge();
            minesModeTimer = null;
        }
    }

    //-------------------------------- getters and setters --------------------

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

    public void setGameInterfaceListener(GameInterfaceListener gameInterfaceListener) {
        this.gameInterfaceListener = gameInterfaceListener;
    }

    public int getValueOfNumberCell(int row, int col){
        return ((Number)gameMatrix[row][col]).getValue();
    }

    public boolean getIsFirstCellRevealed(){
        return firstCellReveal;
    }
    //------------------------------ DB methods -----------------------

    public int getScoreRecordSortedByTime(int ScoreRecordNum){
        return prefs.getScoreRecordSortedByTime(prefs.getLevel(), ScoreRecordNum);
    }

    public void setNewScore(String name) {
        prefs.setNewScore(passedTime, name, prefs.getLevel());
    }

    public void setNewScore(String name, double longitude, double latitude) {
        prefs.setNewScore(passedTime, name, longitude, latitude, prefs.getLevel());
    }
}
