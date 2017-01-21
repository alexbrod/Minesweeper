package alexbrod.minesweeper.bl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Alex Brod on 1/21/2017.
 */

public class TiltSamplingBoundService extends Service implements SensorEventListener {

    private static final int SENSOR_DELAY_MICROS = 250 * 1000; // 250ms
    private static final float DEVIATION = 0.4f;
    private final IBinder iBinder = new LocalBinder();

    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private int lastAccuracy;
    private Listener listener;
    private float xAxisTilt;
    private float yAxisTilt;
    private boolean isFirstCapture;

    public interface Listener {
        void onOrientationChanged(boolean changeEvent);
    }


    public class LocalBinder extends Binder {
        public TiltSamplingBoundService getService() {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            isFirstCapture = false;
            return TiltSamplingBoundService.this;
        }
    }



    public void startListening(Listener listener) {
        if (this.listener == listener) {
            return;
        }
        this.listener = listener;
        if (rotationSensor == null) {
            Log.w(this.getClass().getSimpleName(),
                    "Rotation vector sensor not available; will not provide orientation data.");
            return;
        }
        sensorManager.registerListener(this, rotationSensor, SENSOR_DELAY_MICROS);
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
        listener = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (lastAccuracy != accuracy) {
            lastAccuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (listener == null) {
            return;
        }
        if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == rotationSensor) {
            if(!isFirstCapture) {
                xAxisTilt = event.values[0];
                yAxisTilt = event.values[1];
            }
            updateRotation(event.values);
        }
    }

    private void updateRotation(float[] rotationVector) {
        if(!isFirstCapture){
            return;
        }

        float x = rotationVector[0];
        float y = rotationVector[1];

        if(x < xAxisTilt - DEVIATION || x > xAxisTilt + DEVIATION
                || y < yAxisTilt - DEVIATION || y > yAxisTilt + DEVIATION){
            listener.onOrientationChanged(true);
        }else{
            listener.onOrientationChanged(false);
        }

    }

    public void setCaptureInitialTilt(boolean flag){
        isFirstCapture = flag;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

}
