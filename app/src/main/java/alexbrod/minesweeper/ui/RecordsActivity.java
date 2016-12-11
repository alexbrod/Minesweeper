package alexbrod.minesweeper.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.RecordsList;
import alexbrod.minesweeper.bl.ScoreRecord;
import alexbrod.minesweeper.bl.SharedPrefManager;

public class RecordsActivity extends AppCompatActivity {
    private static final int TABLE_PADDING = 20;
    private final int MAX_ROWS_IN_TABLE = 10;
    private TableLayout tableLayout;
    private Spinner spinner;
    private SharedPrefManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        prefs = new SharedPrefManager(this);
        prefs.setNumOfMaxRecordsInTable(MAX_ROWS_IN_TABLE);

        createRecordsTable();
        spinnerInit();



    }

    private void spinnerInit() {
        spinner = (Spinner)findViewById(R.id.spnrChooseLevel);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                updateTable(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    private void updateTable(int level) {
        RecordsList list = prefs.getRecordsListSortedByTime(level);
        if(list == null){
            updateTableWithNone(1);
        }else{
            int i;
            //starts from 1 because need to skip header
            for (i = 1; i <= list.size() && i <= MAX_ROWS_IN_TABLE; i++) {
                TableRow row = (TableRow) tableLayout.getChildAt(i);
                ScoreRecord rec = list.get(i-1);
                TextView tvTime = (TextView) row.getChildAt(1);
                TextView tvName = (TextView) row.getChildAt(2);
                TextView tvDate = (TextView) row.getChildAt(3);
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
            TextView tvTime = (TextView) row.getChildAt(1);
            TextView tvName = (TextView) row.getChildAt(2);
            TextView tvDate = (TextView) row.getChildAt(3);
            tvDate.setText("None");
            tvName.setText("None");
            tvTime.setText("None");

        }
    }

    private void createRecordsTable() {
        tableLayout = (TableLayout)findViewById(R.id.recordsTable);
        Drawable drawable = getResources().getDrawable(R.drawable.minesweeper_table,null);
        for (int i = 1; i <= MAX_ROWS_IN_TABLE; i++) {
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);
            TextView tvNumber = new TextView(this);
            TextView tvTime = new TextView(this);
            TextView tvName = new TextView(this);
            TextView tvDate = new TextView(this);

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

            tvNumber.setText(String.format("%d",i));
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
