package com.example.livecoaching;

import android.app.Activity;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.livecoaching.Adapter.ConnectedDevicesAdapter;
import com.example.livecoaching.Model.ApplicationState;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ConnectActivity extends AppCompatActivity
        implements ConnectedDevicesAdapter.DeviceClickListener {
    ConnectedDevicesAdapter adapter;
    private static final String TAG = ConnectActivity.class.getSimpleName();
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 2;

    //sensors
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initView();
    }

    public void initView() {
        initToolbar();
        getPairedDevices();
        initRecyclerView();
        initSensors();
    }

    public void initRecyclerView() {
        RecyclerView overview = findViewById(R.id.connect_recyclerView);
        overview.setLayoutManager(new GridLayoutManager(this, 4));
        this.adapter = new ConnectedDevicesAdapter(this);
        overview.setAdapter(adapter);
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = findViewById(R.id.title_connectActivity);
        title.setText(R.string.connectActivity_title);
        title.setTextSize(40);
    }

    public void getPairedDevices() {
        // updates the list of connected devices in application state
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            int REQUEST_ENABLE_BT = 1;
            ArrayList<BluetoothDevice> btDevices = new ArrayList();
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                onActivityResult(REQUEST_ENABLE_BT, RESULT_OK);
            } else {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bt : pairedDevices) {
                    btDevices.add(bt);
                }
            }
            ApplicationState.getInstance().setConnectedDevices(btDevices);

            // update recyclerView
            initRecyclerView();
        } catch (NullPointerException e){
            System.out.println("bluetoothAdapter is null, are you on an emulated device ?");
        }

        return;
    }

    public void onActivityResult(int reqCode, int resCode) {
        if (resCode == RESULT_OK) {
            // refresh devices
            System.out.println("Refreshing list of connected devices");
        } else {
            // print error code ?
            System.out.println("Bluetooth still not on");
        }
    }

    @Override
    public void onChooseClickListener(int clickedIndex) {
        System.out.println("item clicked : " + ApplicationState.getInstance().getConnectedDevices().get(clickedIndex));
    }

    public void initTEST(){
        System.out.println("init TEST");
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
            System.out.println("requesting permission");
        } else {
            System.out.println("trying to access googlefit");
            accessGoogleFit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("wanting to connect");
        System.out.println(resultCode);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("result was ok");
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                System.out.println("trying to access googlefit");
                accessGoogleFit();
            }
        }
    }


    public void initSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        System.out.println(sensors);
        //Log.d(TAG, "" + sensors);
    }

    private void accessGoogleFit() {
        System.out.println("accessing goggle Fit");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();



        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d(TAG, "onSuccess()");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure()", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    /*
    public void initTest(){
        Intent intent = getIntent();
        String zeub = intent.getStringExtra("msg");

        TextView textView = findViewById(R.id.testConnect);
        try {
            textView.setText(ApplicationState.getInstance().getConnectedDevices().get(0).getName());
        } catch (IndexOutOfBoundsException e){
            textView.setText("out of bounds, size of the list : " + ApplicationState.getInstance().getConnectedDevices().size());
        }
    }
    */

   /* public void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
*/

}
