package alexbrod.minesweeper.bl;

/**
 * Created by Alex Brod on 12/2/2016.
 */

public class Number extends Cell {

    private int value;

    public Number(int value)
    {
        this.value = value;
        setEmpty(false);
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
