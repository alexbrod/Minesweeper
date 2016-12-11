package alexbrod.minesweeper.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.SharedPrefManager;

public class MenuActivity extends AppCompatActivity {

    private final static String LEVEL = "Level";
    private Button btnLevel;
    private SharedPrefManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        prefs = new SharedPrefManager(this);

        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intent);

            }
        });

        btnLevel = (Button) findViewById(R.id.btnLevel);
        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LevelActivity.class);
                startActivity(intent);
            }
        });

        Button btnRecords = (Button) findViewById(R.id.btnRecords);
        btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        Button btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        setLevelText();
    }

    @Override
    public void onResume(){
        super.onResume();
        setLevelText();
    }

    private void setLevelText(){
        btnLevel.setText(LEVEL + "\n(" + prefs.intLevelToString(prefs.getLevel()) + ")");
    }
}
