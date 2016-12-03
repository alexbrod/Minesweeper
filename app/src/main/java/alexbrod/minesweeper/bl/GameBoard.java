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

    public GameBoard(int rows, int cols, int minesNum) {
        this.rows = rows;
        this.cols = cols;
        this.minesNum = minesNum;
        this.leftMines = minesNum;
        gameMatrix = new Cell[rows][cols];
    }

    public void setMinesInRandomCells(int from, int to){
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
            for (int j = mineCol - 1; j < mineCol + 2; j++) {
                try{
                    if (gameMatrix[i][j] == null) {
                        gameMatrix[i][j] = new Number(1);
                    }else if(gameMatrix[i][j] instanceof Number){
                        ((Number)gameMatrix[i][j]).setValue(
                                ((Number)gameMatrix[i][j]).getValue() + 1);
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
        gameMatrix[row][col].setRevealed(true);
    }

    public Cell[][] getGameMatrix(){
        return gameMatrix;
    }
}
