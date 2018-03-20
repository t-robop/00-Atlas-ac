package com.robop.scriptrobotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import me.aflak.bluetooth.Bluetooth;

//Android端末にペアリングされているBlueTooth機器の一覧

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Bluetooth bluetooth;
    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    ListView pairedDevicesListView;
    ArrayList<String> resultPairedDevicesName;  //ペアリングされているデバイスの名前
    ArrayList<String> resultPairedDevicesAddress;   //ペアリングされているデバイスのMACアドレス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        bluetooth = new Bluetooth(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        resultPairedDevicesName = new ArrayList<>();
        resultPairedDevicesAddress = new ArrayList<>();
        Button scanButton = findViewById(R.id.scanDevice);

        pairedDevicesListView = findViewById(R.id.deviceList);
        TextView emptyView = findViewById(R.id.emptyTextView);

        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        searchPairedDevices();

        pairedDevicesListView.setEmptyView(emptyView);
        pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);

        pairedDevicesListView.setOnItemClickListener(this);
        scanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //pairedDevicesListViewの更新
        if (view.getId() == R.id.scanDevice){
            pairedDevicesArrayAdapter.clear();
            searchPairedDevices();
            pairedDevicesArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //String tryConnectDeviceName = null;
        String tryConnectDeviceAddress = null;
        if(!resultPairedDevicesName.isEmpty() && !resultPairedDevicesAddress.isEmpty()){
            //tryConnectDeviceName = resultPairedDevicesName.get(position);
            tryConnectDeviceAddress = resultPairedDevicesAddress.get(position);
        }

        //Log.i("result", tryConnectDeviceName);
        //Log.i("result", tryConnectDeviceAddress);

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
}
