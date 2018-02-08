package hk.ust.cse.comp4521.ballgame;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    DrawView drawview;

    private static ImageButton rightButton, leftButton;

    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load the View to draw on
        // Ths basically creates the screen using the DrawView class which is an
        // extension of the View class
        // setContentView(new DrawView(this));
        drawview = (DrawView) findViewById(R.id.drawview);

        leftButton = (ImageButton) findViewById(R.id.left);
        leftButton.setOnClickListener(this);

        rightButton = (ImageButton) findViewById(R.id.right);
        rightButton.setOnClickListener(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        drawview.stopGame();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        drawview.releaseResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    float[] mags = new float[3];
    float[] accels = new float[3];
    float[] RotationMat = new float[9];
    float[] InclinationMat = new float[9];
    float[] attitude = new float[3];
    final static double RAD2DEG = 180/Math.PI;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int type = sensorEvent.sensor.getType();

        // Use this with the sensorsimulator instead of the above
        // int type = event.type;

        switch(type) {
            case Sensor.TYPE_ACCELEROMETER:
                accels = sensorEvent.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = sensorEvent.values;

                break;
        }

        SensorManager.getRotationMatrix(RotationMat,
                InclinationMat, accels, mags);
        SensorManager.getOrientation(RotationMat, attitude);

        double yaw = attitude[0]*RAD2DEG;
        double pitch = attitude[1]*RAD2DEG;
        double roll = attitude[2]*RAD2DEG;

        if (roll > 0) {
            drawview.movePaddleRight((int)(roll/1.8));
        }
        else if (roll < 0) {
            drawview.movePaddleLeft((int)(-roll/1.8));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right:
                drawview.movePaddleRight(20);
                break;
            case R.id.left:
                drawview.movePaddleLeft(20);
                break;
            default:
                break;
        }
    }
}
