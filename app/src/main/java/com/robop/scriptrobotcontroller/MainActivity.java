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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static app.akexorcist.bluetotohspp.library.BluetoothSPP.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnDataReceivedListener, BluetoothConnectionListener {

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

        bt = new BluetoothSPP(this);

        //端末のBluetooth有効確認
        if(!bt.isBluetoothAvailable()){
            Toast.makeText(this.getApplicationContext(),"BluetoothがOFFになっています",Toast.LENGTH_LONG).show();
            finish();
        }

        bt.setOnDataReceivedListener(this);

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

        Button startButton = findViewById(R.id.startButton);
        Button finishButton = findViewById(R.id.finishButton);


        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        /*
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

<<<<<<< c14a5b99570a15eb5ce5648b3b5e7853bffa5bc6
////        button1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                item.addSpeed(10);
////                item.addTime(100);
////                item.addImageId("1個目");
////                listView.setAdapter(listAdapter);
////            }
////        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                item.addSpeed(20);
//                item.addTime(200);
//                item.addImageId("2個目");
//                listView.setAdapter(listAdapter);
//            }
//        });
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                item.addSpeed(30);
//                item.addTime(300);
//                item.addImageId("3個目");
//                listView.setAdapter(listAdapter);
//            }
//        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                item.addSpeed(40);
//                item.addTime(400);
//                item.addImageId("4個目");
//                listView.setAdapter(listAdapter);
//            }
//        });
=======
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageList.add("1");

                adapter.add("1");
                listView.setAdapter(adapter);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageList.add("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageList.add("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageList.add("4");
            }
        });
>>>>>>> listから送信する文字列の取得処理を追加
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getCount() > 0){
                    String sendData = adapter.getItem(0);
                    Log.i("BTData",sendData);
                }
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "position = " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "text = " + i, Toast.LENGTH_SHORT).show();
        return false;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE){
            if(resultCode == Activity.RESULT_OK){
                bt.connect(data);   //TODO ヌルポ出て接続できない
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
