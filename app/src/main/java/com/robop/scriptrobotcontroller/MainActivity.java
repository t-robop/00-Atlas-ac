package com.robop.scriptrobotcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.CommunicationCallback;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CommunicationCallback {

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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ItemDataList item;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final ListView listView = findViewById(R.id.listView);
        listView = findViewById(R.id.listView);
        //RecyclerView recyclerView = findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1);

        item = new ItemDataList();
        listAdapter = new ListAdapter(getApplicationContext(),item);
        listView.setAdapter(listAdapter);

        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapter(item);

        //Button button1 = findViewById(R.id.button1);
        setButtonLisetener(10, 100, "1個目", R.id.button1);
        setButtonLisetener(20, 200, "2個目", R.id.button2);
        setButtonLisetener(30, 300, "3個目", R.id.button3);
        setButtonLisetener(40, 400, "4個目", R.id.button4);

        bluetooth = new Bluetooth(this);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }

        Button startButton = findViewById(R.id.startButton);
        Button finishButton = findViewById(R.id.finishButton);


        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        //listの処理
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), "position = " + i, Toast.LENGTH_SHORT).show();
                        // ダイアログの表示
                        final CustomizedDialog dialog = CustomizedDialog.newInstance();
                        dialog.setOnSetButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //ダイアログから値を取得して、output用のTextViewに表示
                                //tvOutput.setText(String.format("%1$,3d", dialog.getInputValue()));
                                //ダイアログを消す
                                dialog.dismiss();
                            }
                        });
                        dialog.show(getFragmentManager(), "dialog_fragment");
                        dialog.setCancelable(false);
                    }
                }
        );
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), "text = " + item.getImageId(i), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "position = " + i, Toast.LENGTH_SHORT).show();
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

    Button setButtonLisetener(final int speed, final int time, final String imageId, int id) {

        Button button = findViewById(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.addSpeed(speed);
                item.addTime(time);
                item.addImageId(imageId);
                listView.setAdapter(listAdapter);
            }
        });
        return button;
    }
}
