package alexbrod.minesweeper.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.SharedPrefManager;

public class SettingsActivity extends AppCompatActivity {
    private SharedPrefManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = SharedPrefManager.getInstance(this);

        Button btnClearRecords = (Button)findViewById(R.id.btnClearRecords);
        btnClearRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.clearRecords();
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Records Cleared")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
    }
}
