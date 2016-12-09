package alexbrod.minesweeper.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.Levels;

public class LevelActivity extends AppCompatActivity {
    GradientDrawable noviceGradientDrawable;
    GradientDrawable advancedGradientDrawable;
    GradientDrawable expertGradientDrawable;
    Button btnNovice;
    Button btnAdvanced;
    Button btnExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);


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
                Levels.setLevel(LevelActivity.this, Levels.NOVICE);
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noviceGradientDrawable.setStroke(1, Color.BLACK);
                advancedGradientDrawable.setStroke(3,Color.WHITE);
                expertGradientDrawable.setStroke(1,Color.BLACK);
                Levels.setLevel(LevelActivity.this, Levels.ADVANCED);
            }
        });

        btnExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noviceGradientDrawable.setStroke(1, Color.BLACK);
                advancedGradientDrawable.setStroke(1,Color.BLACK);
                expertGradientDrawable.setStroke(3,Color.WHITE);
                Levels.setLevel(LevelActivity.this, Levels.EXPERT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
