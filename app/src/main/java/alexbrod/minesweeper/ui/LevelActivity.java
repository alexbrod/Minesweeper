package alexbrod.minesweeper.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.SharedPrefManager;

public class LevelActivity extends AppCompatActivity {
    private GradientDrawable noviceGradientDrawable;
    private GradientDrawable advancedGradientDrawable;
    private GradientDrawable expertGradientDrawable;
    private Button btnNovice;
    private Button btnAdvanced;
    private Button btnExpert;
    private TextView tvNovice;
    private TextView tvAdvanced;
    private TextView tvExpert;

    private SharedPrefManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        prefs = new SharedPrefManager(this);

        buttonInit();
        textViewInit();


    }

    private void textViewInit() {
        tvNovice = (TextView)findViewById(R.id.txtNoviceBestScore);
        tvAdvanced = (TextView)findViewById(R.id.txtAdvancedBestScore);
        tvExpert = (TextView)findViewById(R.id.txtExpertBestScore);

        tvNovice.setText(formatTime(prefs.getBestScore(SharedPrefManager.NOVICE_LEVEL)));
        tvAdvanced.setText(formatTime(prefs.getBestScore(SharedPrefManager.ADVANCED_LEVEL)));
        tvExpert.setText(formatTime(prefs.getBestScore(SharedPrefManager.EXPERT_LEVEL)));
    }

    private String formatTime(int seconds){
        if(seconds == Integer.MAX_VALUE){
            return "NONE";
        }else{
            return seconds + "(sec)";
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void buttonInit() {
        btnNovice = (Button) findViewById(R.id.btnNovice);
        btnAdvanced = (Button) findViewById(R.id.btnAdvanced);
        btnExpert = (Button) findViewById(R.id.btnExpert);

        noviceGradientDrawable = (GradientDrawable) btnNovice.getBackground().getCurrent();
        noviceGradientDrawable.setStroke(1,Color.BLACK);
        advancedGradientDrawable = (GradientDrawable) btnAdvanced.getBackground().getCurrent();
        advancedGradientDrawable.setStroke(1,Color.BLACK);
        expertGradientDrawable = (GradientDrawable) btnExpert.getBackground().getCurrent();
        expertGradientDrawable.setStroke(1,Color.BLACK);

        btnNovice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noviceGradientDrawable.setStroke(3, Color.WHITE);
                advancedGradientDrawable.setStroke(1,Color.BLACK);
                expertGradientDrawable.setStroke(1,Color.BLACK);
                prefs.setLevel(SharedPrefManager.NOVICE_LEVEL);
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noviceGradientDrawable.setStroke(1, Color.BLACK);
                advancedGradientDrawable.setStroke(3,Color.WHITE);
                expertGradientDrawable.setStroke(1,Color.BLACK);
                prefs.setLevel(SharedPrefManager.ADVANCED_LEVEL);
            }
        });

        btnExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noviceGradientDrawable.setStroke(1, Color.BLACK);
                advancedGradientDrawable.setStroke(1,Color.BLACK);
                expertGradientDrawable.setStroke(3,Color.WHITE);
                prefs.setLevel(SharedPrefManager.EXPERT_LEVEL);
            }
        });
    }
}
