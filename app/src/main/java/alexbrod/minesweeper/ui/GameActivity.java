package alexbrod.minesweeper.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.GameBoard;
import alexbrod.minesweeper.bl.GameInterfaceListener;
import alexbrod.minesweeper.bl.LocationManager;
import alexbrod.minesweeper.bl.SharedPrefManager;
import alexbrod.minesweeper.bl.TiltSamplingBoundService;


public class GameActivity extends AppCompatActivity implements CellButtonOnClickListener,
        GameInterfaceListener, TiltSamplingBoundService.Listener {
    private static final String YOU_WON = "You Won!";
    private static final String PLAY_AGAIN = "Do you want to play another game?";
    private static final String GAME_OVER = "GAME OVER";
    private final int GAME_BOARD_RETIO = 2;

    private int columnCount;
    private int rowCount;
    private int minesNum;
    private boolean isTiltSamplingServiceBound;

    private TextView txtMinesMode;
    private TextView txtTimer;
    private TextView txtMineNum;
    private GameBoard gameBoard;
    private GridLayout gridLayout;
    private DisplayMetrics dm;
    private AlertDialog.Builder playAgagainAlertDialog;
    private SharedPrefManager prefs;
    private LocationManager locationManager;
    private TiltSamplingBoundService tiltSamplingBoundService;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection tiltSamplingServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder iBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            isTiltSamplingServiceBound = true;
            tiltSamplingBoundService = ((TiltSamplingBoundService.LocalBinder)iBinder).getService();
            tiltSamplingBoundService.setCaptureInitialTilt(true);
            tiltSamplingBoundService.startListening(GameActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isTiltSamplingServiceBound = false;
            tiltSamplingBoundService.setCaptureInitialTilt(false);
            tiltSamplingBoundService.stopListening();
        }
    };


    //------------------------ system events ---------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        prefs = SharedPrefManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);
        //set layout parameters
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        dm = new DisplayMetrics();
        display.getMetrics(dm);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);

        gameBoard = new GameBoard(this);
        gameBoard.setGameInterfaceListener(this);

        columnCount = gameBoard.getCols();
        rowCount = gameBoard.getRows();
        minesNum = gameBoard.getMinesNum();

        initButtonsGrid();
        initAlertDialog();
        txtMineNum = (TextView) findViewById(R.id.txtMines);
        txtMineNum.setText(String.format("%d",minesNum));
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtMinesMode = (TextView) findViewById(R.id.txtAddMinesMode);
        isTiltSamplingServiceBound = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Bind to TiltSamplingBoundService
        if (!isTiltSamplingServiceBound) {
            Intent intent = new Intent(this, TiltSamplingBoundService.class);
            bindService(intent, tiltSamplingServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!locationManager.isConnected()){
            locationManager.connect();
        }else{
            locationManager.tryStartLocationUpdates();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.stopLocationUpdates();
        locationManager.disconnect();
        // Unbind from the TiltSamplingBoundService
        if (isTiltSamplingServiceBound) {
            unbindService(tiltSamplingServiceConnection);
            isTiltSamplingServiceBound = false;
        }
    }

    //------------------- events generated by GUI----------------------

    @Override
    public void cellButtonOnClick(CellButton b) {
        gameBoard.revealCell(b.getRow(),b.getCol());
    }

    @Override
    public boolean cellButtonOnLongClick(CellButton b) {
        //set flag or clear flag
        gameBoard.flagEvent(b.getRow(), b.getCol());
        return true;
    }

    //------------------- events generated by bl-----------------------

    @Override
    public void onCellRevealed(int row, int col, CELL_CONTENT cellContent) {
        CellButton cellButton = getButtonFromGrid(row, col);
        GradientDrawable gd = cellButton.getGradientDrawable();

        if(cellContent == CELL_CONTENT.MINE){
            cellButton.setImageResource(R.drawable.mine);
            cellButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        else if(cellContent == CELL_CONTENT.NUMBER){
            gd.setColor(Color.LTGRAY);
            cellButton.defineNumberTexture(gameBoard.getValueOfNumberCell(row,col));
        }
        else{
            gd.setColor(Color.LTGRAY);
        }
        cellButton.invalidate();
    }

    @Override
    public void onSetFlag(int row, int col) {
        CellButton cellButton = getButtonFromGrid(row, col);
        cellButton.setImageResource(R.drawable.flag);
        cellButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    @Override
    public void onClearFlag(int row, int col) {
        CellButton cellButton = getButtonFromGrid(row, col);
        cellButton.setImageResource(android.R.color.transparent);
    }

    @Override
    public void onGameOver() {
        playAgagainAlertDialog.setTitle(GAME_OVER);
        playAgagainAlertDialog.show();
    }

    @Override
    public void onVictory() {
        final EditText input = new EditText(this);
        AlertDialog.Builder winAlertDialog = new AlertDialog.Builder(this);
        winAlertDialog.setTitle(YOU_WON)
                .setView(input)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(locationManager.isLocationUpdated()){
                            gameBoard.setNewScore(input.getText().toString().trim(),
                                    locationManager.getUpdatedLongitude(),
                                    locationManager.getUpdatedLatitude());
                        }else{
                            gameBoard.setNewScore(input.getText().toString().trim());
                        }
                        //must apply show here so that playAgagainAlertDialog wouldn't appear
                        //before winAlertDialog
                        playAgagainAlertDialog.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);

        if(gameBoard.getCurrentScore() < gameBoard.getBestScore()){
            winAlertDialog.setMessage("You Broke A Record!\nYour time is: "
                    + gameBoard.getCurrentScore() + " sec\nEnter name:");

        }else if(gameBoard.getCurrentScore() < gameBoard.getScoreRecordSortedByTime(
                prefs.getNumOfMaxRecordsInTable() - 1)){
            winAlertDialog.setMessage("You Made It To The Score Table!\nYour time is: "
                    + gameBoard.getCurrentScore() + " sec\nEnter name:");
        }
        playAgagainAlertDialog.setTitle(YOU_WON);
        winAlertDialog.show();
    }

    @Override
    public void updateTimeView(int secondsPassed) {
        txtTimer.setText(String.format("%d", secondsPassed));
    }

    @Override
    public void updateNumOfMinesView(int mines) {
        txtMineNum.setText(String.format("%d", mines));

    }

    @Override
    public void onCellCovered(int row, int col) {
        CellButton cellButton = getButtonFromGrid(row, col);
        GradientDrawable gd = cellButton.getGradientDrawable();
        gd.setColor(Color.GRAY);
        cellButton.setImageResource(android.R.color.transparent);
    }

    //------------------- misc methods-------------------------------

    private void initAlertDialog() {
        playAgagainAlertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(PLAY_AGAIN)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //start over
                        Intent intent = new Intent(GameActivity.this, GameActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);
    }

    private CellButton getButtonFromGrid(int row, int col){
        return (CellButton)gridLayout.getChildAt(row * rowCount + col);
    }

    private void initButtonsGrid(){

        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        int cellButtonWidth = dm.widthPixels/(columnCount + 1);
        int cellButtonHeight = (dm.heightPixels/GAME_BOARD_RETIO)/(rowCount + 1);
        ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(cellButtonWidth, cellButtonHeight);

        for (int i = 0; i < rowCount; i++){
            for (int j = 0; j < columnCount; j++) {
                CellButton cellButton = new CellButton(this);
                cellButton.setPosition(i,j);
                cellButton.setLayoutParams(buttonLayoutParams);
                //set drawable properties
                GradientDrawable gd = cellButton.getGradientDrawable();
                gd.setColor(Color.GRAY);
                gd.setCornerRadius(5);
                gd.setStroke(1, Color.BLACK);
                cellButton.setBackground(gd);

                gridLayout.addView(cellButton);
                cellButton.setCellButtonOnClickListener(this);
            }
        }
    }



    @Override
    public void onOrientationChanged(boolean changeEvent) {
        if(changeEvent){
            txtMinesMode.setText("MINES MODE!");
            gameBoard.startMinesModeTimer();
        }else{
            txtMinesMode.setText("");
            gameBoard.stopMinesModeTimer();
        }
    }



}
