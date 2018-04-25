package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.CommunicationCallback;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CommunicationCallback {

    Bluetooth bluetooth;
    BluetoothAdapter bluetoothAdapter;
    private final int REQUEST_CONNECT_DEVICE = 9;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;

    //TODO ListViewの項目をドラッグで動かして並び替えられるようにする
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
//
//        FrameLayout fl=findViewById(R.id.back_ground);
//        fl.setBackgroundResource(R.drawable.repeat_rasen);

        //Android端末のBluetooth有効化
        bluetooth = new Bluetooth(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null){
            if(!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        }else{
            Toast.makeText(this, "BlueTooth機能が見つかりませんでした\n機能が制限されます", Toast.LENGTH_SHORT).show();
        }

        bluetooth.setCommunicationCallback(this);

        //final ListView listView = findViewById(R.id.listView);
        listView = findViewById(R.id.listView);
        //RecyclerView recyclerView = findViewById(R.id.listView);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        item = new ItemDataList();
        listAdapter = new ListAdapter(getApplicationContext(),item);
        listView.setAdapter(listAdapter);

        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapter(item);

        //TODO 変数化して値変更・保持できるようにする
        setButtonListener(100, 2, "1", R.id.button1);
        setButtonListener(100, 2, "2", R.id.button2);
        setButtonListener(100, 2, "3", R.id.button3);
        setButtonListener(100, 2, "4", R.id.button4);

        Button startButton = findViewById(R.id.startButton);
        Button connectButton = findViewById(R.id.connectButton);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //BlueToothで送る文字列のnullチェック
                if(!sendBTText().equals("")){
                    Log.i("bt",sendBTText());

                    //Bluetooth接続チェック
                    if(bluetooth.isConnected()){
                        bluetooth.send(sendBTText());   //データフォーマット通りの文字列が送信される     //TODO 10命令以上を送りたい場合の処理を考える
                    }else{
                        Toast.makeText(MainActivity.this, "ロボットが接続されていません", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "送るデータがありません", Toast.LENGTH_SHORT).show();
                }
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(bluetooth.isConnected()){
                    bluetooth.disconnect();
                }else{
                    if (bluetoothAdapter != null){
                        Intent intent = new Intent(MainActivity.this,DeviceListActivity.class);
                        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

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
        //ロボットとのBlueTooth接続処理
        if(requestCode == REQUEST_CONNECT_DEVICE){
            if(resultCode == Activity.RESULT_OK){
                String deviceAddress = data.getStringExtra("deviceAddress");
                Log.i("deviceAddress", deviceAddress);
                bluetooth.connectToAddress(deviceAddress);
            }
        }
        //Bluetoothの有効化処理
        else if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                bluetooth.enable();
            }
        }else{
            Toast.makeText(this, "BluetoothがONになっていません!", Toast.LENGTH_SHORT).show();
            //finish();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        //とりあえずアイテム消す為。後で消す
        item.remove(position);
        listView.setAdapter(listAdapter);
        return false;
    }

    void setButtonListener(final int speed, final int time, final String imageId, int id) {

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
    }

    @SuppressLint("DefaultLocale")
    private String sendBTText(){
        StringBuilder sendText = new StringBuilder();

        //imageId、Time、Speedの文字列を連結
        for(int i=0; i<listAdapter.getCount(); i++){
            sendText.append(item.getImageId(i));
            sendText.append(String.format("%02d", item.getTime(i)));
            sendText.append(String.format("%03d", item.getSpeed(i)));
            sendText.append('\0');  //1命令分の終端文字
        }

        return sendText.toString();
    }
}
