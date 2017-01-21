package alexbrod.minesweeper.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.RecordsList;
import alexbrod.minesweeper.bl.ScoreRecord;
import alexbrod.minesweeper.bl.SharedPrefManager;

public class RecordsTableFragment extends Fragment {
    private final int MAX_ROWS_IN_TABLE = 10;
    private static final int TABLE_PADDING = 20;
    private TableLayout tableLayout;
    private SharedPrefManager prefs;

    public RecordsTableFragment() {
        // Required empty public constructor
    }

    public static RecordsTableFragment newInstance() {
        RecordsTableFragment fragment = new RecordsTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.records_table_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity fragAct = getActivity();
        prefs = SharedPrefManager.getInstance(fragAct);
        prefs.setNumOfMaxRecordsToShow(MAX_ROWS_IN_TABLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        createRecordsTable();
        updateTableToLevel(0);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Question: do I need to clear references to activity?
        //for example I gave the activity as context to preference manager
    }


    private void createRecordsTable() {
        tableLayout = (TableLayout)getView().findViewById(R.id.records_table_layout);
        Context context = tableLayout.getContext();
        Drawable drawable = getResources().getDrawable(R.drawable.minesweeper_table,null);
        for (int i = 1; i <= MAX_ROWS_IN_TABLE; i++) {
            TableRow tableRow = new TableRow(context);
            tableLayout.addView(tableRow);
            TextView tvNumber = new TextView(context);
            TextView tvTime = new TextView(context);
            TextView tvName = new TextView(context);
            TextView tvDate = new TextView(context);

            tvNumber.setBackground(drawable);
            tvDate.setBackground(drawable);
            tvName.setBackground(drawable);
            tvTime.setBackground(drawable);

            tvNumber.setPadding(TABLE_PADDING,TABLE_PADDING,TABLE_PADDING,TABLE_PADDING);
            tvDate.setPadding(TABLE_PADDING,TABLE_PADDING,TABLE_PADDING,TABLE_PADDING);
            tvName.setPadding(TABLE_PADDING,TABLE_PADDING,TABLE_PADDING,TABLE_PADDING);
            tvTime.setPadding(TABLE_PADDING,TABLE_PADDING,TABLE_PADDING,TABLE_PADDING);

            tableRow.addView(tvNumber);
            tableRow.addView(tvTime);
            tableRow.addView(tvName);
            tableRow.addView(tvDate);
        }
    }

    public void updateTableToLevel(int level) {
        RecordsList list = prefs.getRecordsListSortedByTime(level);
        if(list == null){
            updateTableWithNone(1);
        }else{
            int i;
            //starts from 1 because need to skip header
            for (i = 1; i <= list.size() && i <= MAX_ROWS_IN_TABLE; i++) {
                TableRow row = (TableRow) tableLayout.getChildAt(i);
                ScoreRecord rec = list.get(i-1);
                TextView tvNumber = (TextView) row.getChildAt(0);
                TextView tvTime = (TextView) row.getChildAt(1);
                TextView tvName = (TextView) row.getChildAt(2);
                TextView tvDate = (TextView) row.getChildAt(3);
                tvNumber.setText(String.format("%d",i));
                tvDate.setText(formatDate(rec.getDate()));
                tvName.setText(rec.getName());
                tvTime.setText(String.format("%d",rec.getTime()));
            }
            updateTableWithNone(i);
        }
    }

    private String formatDate(long milliSeconds){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(new Date(milliSeconds));
    }

    private void updateTableWithNone(int from){
        for (int i = from; i <= MAX_ROWS_IN_TABLE; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView tvNumber = (TextView) row.getChildAt(0);
            TextView tvTime = (TextView) row.getChildAt(1);
            TextView tvName = (TextView) row.getChildAt(2);
            TextView tvDate = (TextView) row.getChildAt(3);
            tvNumber.setText(String.format("%d",i));
            tvDate.setText("None");
            tvName.setText("None");
            tvTime.setText("None");

        }
    }
}
