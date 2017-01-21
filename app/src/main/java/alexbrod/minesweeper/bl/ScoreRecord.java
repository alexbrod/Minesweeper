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
    private double longitude;
    private double latitude;

    // ------------------ constructors --------------------------------

    public ScoreRecord(int time){
        this(time, Calendar.getInstance().getTimeInMillis());
    }

    public ScoreRecord(int time, long dateInMilliseconds){
        this(time, dateInMilliseconds, "");
    }

    public ScoreRecord(int time, long dateInMilliseconds, String name){
        this(time, dateInMilliseconds, name, Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public ScoreRecord(int time, String name) {
        this(time, Calendar.getInstance().getTimeInMillis(), name);
    }

    public ScoreRecord(int time, String name, double longitude, double latitude){
        this(time, Calendar.getInstance().getTimeInMillis() , name, longitude, latitude);
    }

    public ScoreRecord(int time, long dateInMilliseconds, String name, double longitude, double latitude){
        this.time = time;
        this.date = dateInMilliseconds;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //---------------------- compare methods -------------------------------
    @Override
    public int compareTo(ScoreRecord scoreRecord) {
        return this.getTime() - scoreRecord.getTime();
    }

}
