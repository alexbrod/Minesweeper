package alexbrod.minesweeper.bl;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Alex Brod on 12/9/2016.
 */

public class SharedPrefManager {

    public static final int NOVICE_LEVEL = 0;
    public static final int ADVANCED_LEVEL = 1;
    public static final int EXPERT_LEVEL = 2;
    private static final String LEVEL = "Level";
    SharedPreferences prefs;

    public SharedPrefManager(Context context){
        prefs = context.getSharedPreferences("alexbrod.minesweeper", Context.MODE_PRIVATE);
    }

    public void setLevel(int level){
        prefs.edit().putInt(LEVEL,level).commit();
    }

    public int getLevel(){
        return prefs.getInt(LEVEL, ADVANCED_LEVEL);
    }

    public int getBestScore(int level) {
        return getBestScoreRecord(level).getTime();
    }

    public int getScoreRecordSortedByTime(int level, int scoreRecordNum){
        return getScoreRecordSortedByTimeFromList(level, scoreRecordNum).getTime();
    }

    private ScoreRecord getScoreRecordSortedByTimeFromList(int level, int scoreRecordNum){
        RecordsList list = getRecordsListSortedByTime(level);
        if(list == null || scoreRecordNum >= list.size()){
            return new ScoreRecord();
        }else{
            return list.get(scoreRecordNum);
        }
    }

    private ScoreRecord getBestScoreRecord(int level){
        RecordsList list = getRecordList(level);
        if(list == null){
            return new ScoreRecord();
        }else{
            return list.getShortestTimeRecord();
        }
    }



    private RecordsList getRecordList(int level){
        Gson gson = new Gson();
        String json = prefs.getString(intLevelToString(level) + "List", "");
        if(json.isEmpty()){
            return null;
        }else{
            return gson.fromJson(json, RecordsList.class);
        }
    }

    public String intLevelToString(int level){
        String str = "";
        switch (level){
            case SharedPrefManager.NOVICE_LEVEL:
                str = "Novice";
                break;
            case SharedPrefManager.ADVANCED_LEVEL:
                str = "Advanced";
                break;
            case SharedPrefManager.EXPERT_LEVEL:
                str = "Expert";
                break;
        }
        return str;
    }


    public void setNewScore(int passedTime, String name, int level) {
        saveRecordToScoreList(new ScoreRecord(passedTime,name),level);
    }

    private void saveRecordToScoreList(ScoreRecord record, int level){
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        RecordsList list = getRecordList(level);
        if(list == null) {
            list = new RecordsList();
        }
        list.add(record);
        String json = gson.toJson(list);
        prefsEditor.putString(intLevelToString(level) + "List", json);
        prefsEditor.commit();
    }

    public void clear() {
        prefs.edit().clear().commit();
    }

    public RecordsList getRecordsListSortedByTime(int level){
        ArrayList<ScoreRecord> al = getRecordList(level);
        if(al == null){
            return null;
        }
        Collections.sort(al,new RecordByTimeComparator());
        return (RecordsList) al;
    }

    public void setNumOfMaxRecordsInTable(int numOfMaxRecordsInTable) {
        prefs.edit().putInt("NumOfMaxRecordsInTable",numOfMaxRecordsInTable).commit();
    }

    public int getNumOfMaxRecordsInTable() {
        return prefs.getInt("NumOfMaxRecordsInTable",1);
    }

    public void clearRecords() {
        prefs.edit().remove(intLevelToString(NOVICE_LEVEL) + "List").commit();
        prefs.edit().remove(intLevelToString(ADVANCED_LEVEL) + "List").commit();
        prefs.edit().remove(intLevelToString(EXPERT_LEVEL) + "List").commit();
    }
}
