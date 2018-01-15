package com.robop.scriptrobotcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.CommunicationCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,CommunicationCallback {

    Bluetooth bluetooth;
    private final int REQUEST_CONNECT_DEVICE = 9;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;

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

    ArrayAdapter<String> adapter;
    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = new Bluetooth(this);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }

        ListView listView = findViewById(R.id.listView);
        imageList = new ArrayList<>(); //動作コードのリスト
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,imageList);
        listView.setAdapter(adapter);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(this);


        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        bluetooth.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CONNECT_DEVICE){
            if(resultCode == Activity.RESULT_OK){
                //TODO DeviceListActivityから接続先デバイスの名前を取得
                //bluetooth.connectToName(deviceName);
            }
        }
        else if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                bluetooth.enable();
            }
        }else{
            Toast.makeText(this, "BluetoothがONになっていません!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Toast.makeText(this, "接続 to " + device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Toast.makeText(this, "接続が切れました", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        Toast.makeText(this, "接続できません", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                imageList.add("0001");
                break;

            case R.id.button2:
                imageList.add("0002");
                break;

            case R.id.button3:
                imageList.add("0003");
                break;

            case R.id.button4:
                imageList.add("0004");
                break;

            case R.id.startButton:
                //Arduinoに送るデータ
                StringBuilder btData = new StringBuilder();

                if(!imageList.isEmpty()){
                    //スクリプトリストの値をとってくる
                    for(int i=0; i<imageList.size(); i++){
                        btData.append(imageList.get(i));
                    }
                    Log.i("btData",btData.toString());

                }
                break;

            case R.id.finishButton:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "position = " + i, Toast.LENGTH_SHORT).show();
        //TODO ダイアログ表示処理追加
        CustomizedDialog dialog = new CustomizedDialog();
        dialog.show(getFragmentManager(), "dialog_fragment");
        dialog.setCancelable(false);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "text = " + i, Toast.LENGTH_SHORT).show();
        return false;
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
                if(bluetooth.isConnected()){
                    bluetooth.disconnect();
                }else{
                    Intent intent = new Intent(this,DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
                break;
        }
        return true;
    }

}
