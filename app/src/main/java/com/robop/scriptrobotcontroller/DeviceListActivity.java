package com.robop.scriptrobotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import me.aflak.bluetooth.Bluetooth;

//Android端末にペアリングされているBlueTooth機器の一覧

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Bluetooth bluetooth;
    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    ListView pairedDevicesListView;
    ArrayList<String> resultPairedDevicesName;  //ペアリングされているデバイスの名前
    ArrayList<String> resultPairedDevicesAddress;   //ペアリングされているデバイスのMACアドレス

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        bluetooth = new Bluetooth(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        resultPairedDevicesName = new ArrayList<>();
        resultPairedDevicesAddress = new ArrayList<>();

        pairedDevicesListView = findViewById(R.id.device_list);
        TextView emptyView = findViewById(R.id.empty_pairing_device);

        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        searchPairedDevices();  //ペアリングされているBlueTooth端末の検索

        pairedDevicesListView.setEmptyView(emptyView);
        pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);

        pairedDevicesListView.setOnItemClickListener(this);

        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#aaaaff"),Color.parseColor("#333333"));
    }

    private void scanDevices(){
        pairedDevicesArrayAdapter.clear();
        searchPairedDevices();
        pairedDevicesArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String tryConnectDeviceAddress = null;
        if(!resultPairedDevicesName.isEmpty() && !resultPairedDevicesAddress.isEmpty()){
            tryConnectDeviceAddress = resultPairedDevicesAddress.get(position);
        }

        Intent intent = new Intent();
        if(tryConnectDeviceAddress != null){
            intent.putExtra("deviceAddress", tryConnectDeviceAddress);  //接続先のMACアドレスをMainActivityに返す
            setResult(RESULT_OK,intent);
            finish();
        }else{
            Toast.makeText(this, "MACアドレスが取得できません", Toast.LENGTH_SHORT).show();
        }

    }

    //ペアリングされているBlueTooth端末の検索
    private void searchPairedDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                resultPairedDevicesName.add(device.getName());
                resultPairedDevicesAddress.add(device.getAddress());
            }
        }else{
            String noDevices = "No devices found";
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            scanDevices();

            // 3秒待機
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    };

    public void close(View view) {
        finish();
    }
}
