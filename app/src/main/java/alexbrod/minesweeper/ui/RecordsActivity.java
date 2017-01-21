package alexbrod.minesweeper.ui;

import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import alexbrod.minesweeper.R;

public class RecordsActivity extends AppCompatActivity {


    private static final String GOOGLE_PLAY_MISSING_MSG = "You need to download Google Play Services in order to use this part of the application";
    private Spinner spinner;
    private Button btnShowTable;
    private Button btnShowMap;
    private boolean isTableViewVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        btnShowTable = (Button) findViewById(R.id.btnShowTable);
        btnShowMap = (Button) findViewById(R.id.btnShowMap);

        initButtons();
        spinnerInit();
        updateFragmentHolderWithTable();

    }

    private void spinnerInit() {
        spinner = (Spinner)findViewById(R.id.spnrChooseLevel);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                updateLevelInFragment(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    private void updateLevelInFragment(int pos) {
        //maybe should change to abstract fragment
        RecordsTableFragment tableFragment = (RecordsTableFragment) getSupportFragmentManager()
                .findFragmentByTag("RecordsTableFragment");
        if(tableFragment != null){
            tableFragment.updateTableToLevel(pos);
            return;
        }

        RecordsMapFragment mapFragment = (RecordsMapFragment) getSupportFragmentManager()
                .findFragmentByTag("RecordsMapFragment");
        if(mapFragment != null){
            mapFragment.updateMapToLevel(pos);
            return;
        }

        Log.e(this.getLocalClassName(),"Cant update level, Fragment does not exist");

    }

    private void initButtons(){
        isTableViewVisible = true;
        btnShowTable.setBackground(getResources().getDrawable(
                R.drawable.minesweeper_base_btn_pressed,null));
        btnShowMap.setBackground(getResources().getDrawable(
                R.drawable.minesweeper_base_btn,null));



        btnShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTableViewVisible){
                    btnShowTable.setBackground(getResources().getDrawable(
                            R.drawable.minesweeper_base_btn_pressed,null));
                    btnShowMap.setBackground(getResources().getDrawable(
                            R.drawable.minesweeper_base_btn,null));
                    isTableViewVisible = true;
                    updateFragmentHolderWithTable();

                    btnShowMap.invalidate();
                    btnShowTable.invalidate();
                }
            }
        });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTableViewVisible){
                    btnShowMap.setBackground(getResources().getDrawable(
                            R.drawable.minesweeper_base_btn_pressed,null));
                    btnShowTable.setBackground(getResources().getDrawable(
                            R.drawable.minesweeper_base_btn,null));
                    isTableViewVisible = false;
                    updateFragmentHolderWithMap();
                    btnShowMap.invalidate();
                    btnShowTable.invalidate();
                }
            }
        });
    }

    private void updateFragmentHolderWithTable(){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.records_fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            RecordsTableFragment recordsTableFragment = RecordsTableFragment.newInstance();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.records_fragment_container, recordsTableFragment
                            ,"RecordsTableFragment").commit();
        }
    }

    private void updateFragmentHolderWithMap(){
        //Check if Google Play Services is installed
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (findViewById(R.id.records_fragment_container) != null) {
                // Create a new Fragment to be placed in the activity layout
                RecordsMapFragment recordsMapFragment = RecordsMapFragment.newInstance();
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.records_fragment_container, recordsMapFragment
                                ,"RecordsMapFragment").commit();
            }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(GOOGLE_PLAY_MISSING_MSG)
                .setCancelable(false)
                .create();
            alertDialog.show();
        }

    }
}
