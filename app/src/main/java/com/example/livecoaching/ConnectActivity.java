package com.example.livecoaching;

import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.livecoaching.Adapter.ConnectedDevicesAdapter;
import com.example.livecoaching.Model.ApplicationState;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConnectActivity extends AppCompatActivity
        implements ConnectedDevicesAdapter.DeviceClickListener{
    ConnectedDevicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initView();
    }

    public void initView(){
        initTest();
        initToolbar();
        initFloatingButton();
        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerView overview = findViewById(R.id.connect_recyclerView);
        overview.setLayoutManager(new GridLayoutManager(this, 4));
        this.adapter = new ConnectedDevicesAdapter(this);
        overview.setAdapter(adapter);
    }

    public void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = findViewById(R.id.title_connectActivity);
        title.setText(R.string.connectActivity_title);
        title.setTextSize(40);
    }

    public void initTest(){
        Intent intent = getIntent();
        String zeub = intent.getStringExtra("msg");

       getPairedDevices();
        TextView textView = findViewById(R.id.testConnect);
        try {
            textView.setText(ApplicationState.getInstance().getConnectedDevices().get(0).getName());
        } catch (IndexOutOfBoundsException e){
            textView.setText("out of bounds, size of the list : " + ApplicationState.getInstance().getConnectedDevices().size());
        }
    }

    public void getPairedDevices(){
        // updates the list of connected devices in application state
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int REQUEST_ENABLE_BT = 1;
        ArrayList<BluetoothDevice> btDevices = new ArrayList();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            onActivityResult(REQUEST_ENABLE_BT,RESULT_OK);
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice bt : pairedDevices){
                btDevices.add(bt);
            }
        }
        ApplicationState.getInstance().setConnectedDevices(btDevices);

        // update recyclerView
        initRecyclerView();
        return;
    }

    public void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onActivityResult(int reqCode, int resCode){
        if (resCode == RESULT_OK){
            // refresh devices
            System.out.println("Refreshing list of connected devices");
        }
        else {
            // print error code ?
            System.out.println("Bluetooth still not on");
        }
    }

    @Override
    public void onChooseClickListener(int clickedIndex) {
        System.out.println("item clicked : " + ApplicationState.getInstance().getConnectedDevices().get(clickedIndex));
    }
}
