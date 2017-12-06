package com.robop.scriptrobotcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static app.akexorcist.bluetotohspp.library.BluetoothSPP.*;

public class MainActivity extends AppCompatActivity implements OnDataReceivedListener, BluetoothConnectionListener {

    BluetoothSPP bt;

    // 速度の値が入る変数
    // 前進の時
    String frontLeftSpeedStr;
    String frontSpeedRightSpeedStr;
    // 後退の時
    String backSpeedLeftSpeedStr;
    String backRightSpeedStr;
    // 回転の時
    String rotationLeftSpeedStr;
    String rotationRightSpeedStr;

    // 実行時間の値が入る変数
    // 前進のとき
    String frontTimeStr;

    //後退の時
    String backTimeStr;

    //回転の時
    String rotationLeftStr;
    String rotationRightStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new BluetoothSPP(this);

        //端末のBluetooth有効確認
        if(!bt.isBluetoothAvailable()){
            Toast.makeText(this.getApplicationContext(),"BluetoothがOFFになっています",Toast.LENGTH_LONG).show();
            finish();
        }

        bt.setOnDataReceivedListener(this);

        final ListView listView = findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button startButton = findViewById(R.id.startButton);
        Button finishButton = findViewById(R.id.finishButton);

        //listの処理
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), "position = " + i, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int num = i;
                        Toast.makeText(getApplicationContext(), "text = " + num, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add("1");
                listView.setAdapter(adapter);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add("2");
                listView.setAdapter(adapter);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add("3");
                listView.setAdapter(adapter);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add("4");
                listView.setAdapter(adapter);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendData = adapter.getItem(0);
                Log.i("BTData",sendData);
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

        //Bluetooth有効確認
        if (!bt.isBluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,BluetoothState.REQUEST_ENABLE_BT);
        }else{
            if(!bt.isBluetoothAvailable()){
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        bt.stopService();
    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        Toast.makeText(this.getApplicationContext(),"接続 to " + name + "\n" + address,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceDisconnected() {
        bt.stopService();
        Toast.makeText(this.getApplicationContext(),"接続が切れました",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceConnectionFailed() {
        Toast.makeText(this.getApplicationContext(),"接続できません",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.connectBT:
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED){
                    bt.disconnect();    //接続中なら切断する
                }else{
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent,BluetoothState.REQUEST_CONNECT_DEVICE);
                }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE){
            if(resultCode == Activity.RESULT_OK){
                bt.connect(data);
            }
        }else if(requestCode == BluetoothState.REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }else{
                Toast.makeText(getApplicationContext(),"BluetoothがOFFになっています",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
