package alexbrod.minesweeper.bl;

import java.util.Comparator;

/**
 * Created by Alex Brod on 12/10/2016.
 */

public class RecordByTimeComparator implements Comparator<ScoreRecord> {
    @Override
    public int compare(ScoreRecord r1, ScoreRecord r2) {
        if(r1.getTime() > r2.getTime()){
            return 1;
        }else if(r1.getTime() == r2.getTime()){
            return 0;
        }else{
            return -1;
        }
    }
}
