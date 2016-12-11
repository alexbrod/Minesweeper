package alexbrod.minesweeper.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Alex Brod on 12/9/2016.
 */

public class RecordsList extends ArrayList<ScoreRecord>  {

    private ScoreRecord lastTimeRecord;

    public ScoreRecord getShortestTimeRecord(){
        ScoreRecord shortestTimeRecord = new ScoreRecord();
        for (int i = 0; i < this.size(); i++) {
            if(this.get(i).getTime() < shortestTimeRecord.getTime()){
                shortestTimeRecord = this.get(i);
            }
        }
        return shortestTimeRecord;
    }





}
