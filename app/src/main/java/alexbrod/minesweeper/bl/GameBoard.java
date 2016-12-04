package alexbrod.minesweeper.bl;

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

    public GameBoard(int rows, int cols, int minesNum) {
        this.rows = rows;
        this.cols = cols;
        this.minesNum = minesNum;
        this.leftMines = minesNum;
        gameMatrix = new Cell[rows][cols];
        initGameMatrix();
        setMinesInRandomCells(0, rows * cols);

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
        Cell cell = gameMatrix[row][col];
        if(!cell.isRevealed()){
            gameMatrix[row][col].setRevealed(true);
            if(cell instanceof Mine){
                gameInterfaceListener.onCellRevealed(row,col, GameInterfaceListener.CELL_CONTENT.MINE);
                gameOver();
                return;
            }

            if(cell instanceof Number){
                gameInterfaceListener.onCellRevealed(row,col, GameInterfaceListener.CELL_CONTENT.NUMBER);
            }else{
                gameInterfaceListener.onCellRevealed(row,col, GameInterfaceListener.CELL_CONTENT.EMPTY);
            }

            checkVictory();



        }

    }

    private void checkVictory() {
        gameInterfaceListener.onVictory();
    }

    private void gameOver() {
        gameInterfaceListener.onGameOver();

    }

    public Cell[][] getGameBoardView(){
        return gameMatrix;
    }

    public void setGameInterfaceListener(GameInterfaceListener gameInterfaceListener) {
        this.gameInterfaceListener = gameInterfaceListener;
    }
}
