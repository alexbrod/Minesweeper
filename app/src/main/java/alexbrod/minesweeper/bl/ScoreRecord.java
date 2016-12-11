package alexbrod.minesweeper.bl;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex Brod on 12/9/2016.
 */

public class ScoreRecord implements Comparable<ScoreRecord> {
    private int time;
    private long date;
    private String name;

    // ------------------ constructors --------------------------------

    public ScoreRecord(){
        this(Integer.MAX_VALUE);
    }

    public ScoreRecord(int time){
        this(time, Calendar.getInstance().getTimeInMillis());
    }

    public ScoreRecord(int time, long dateInMilliseconds){
        this(time, dateInMilliseconds, "");
    }

    public ScoreRecord(int time, long dateInMilliseconds, String name){
        this.time = time;
        this.date = dateInMilliseconds;
        this.name = name;
    }

    public ScoreRecord(int time, String name) {
        this(time, Calendar.getInstance().getTimeInMillis(), name);
    }

    // --------------- getters and setters ---------------------------------

    public int getTime() {
        return time;
    }

    public long getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    //---------------------- compare methods -------------------------------
    @Override
    public int compareTo(ScoreRecord scoreRecord) {
        return this.getTime() - scoreRecord.getTime();
    }

}
