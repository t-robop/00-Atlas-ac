package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.CommunicationCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CommunicationCallback {

    Bluetooth bluetooth;
    BluetoothAdapter bluetoothAdapter;
    private final int REQUEST_CONNECT_DEVICE = 9;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;

    ArrayList<ItemDataModel> ItemDataArray = new ArrayList<>();
    //private ListView listView;
    //private RecyclerView.Adapter mAdapter;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView connectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BlueTooth処理
        bluetooth = new Bluetooth(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        } else {
            Toast.makeText(this, "BlueTooth機能が見つかりませんでした\n機能が制限されます", Toast.LENGTH_SHORT).show();
        }

        bluetooth.setCommunicationCallback(this);

        connectStatus = findViewById(R.id.connectStatus);

//        //ListView処理
//        listView = findViewById(R.id.listView);
//
//        //item = new ItemDataModel();
//        mAdapter = new ListAdapter(getApplicationContext(), ItemDataArray);
//        listView.setAdapter(mAdapter);
//
//        listView.setOnItemClickListener(this);
//        listView.setOnItemLongClickListener(this);

        //RecyclerView処理
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(getApplicationContext(),ItemDataArray);
        mRecyclerView.setAdapter(mAdapter);

        //ItemTouchHelper
        ItemTouchHelper itemDecor = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        mAdapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        ItemDataArray.remove(fromPos);

                        mAdapter.notifyItemRemoved(fromPos);
                    }
                });
        itemDecor.attachToRecyclerView(mRecyclerView);

        /*Button処理
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        List<Integer> speedParamList = Arrays.asList(sharedPreferences.getInt("speedParam", 100), sharedPreferences.getInt("speedParam", 100));
        */

        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(this);
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "前進", "パワーと時間を設定して、ロボットを前に動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "後退", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "左回転", "パワーと時間を設定して、ロボットを左に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "右回転", "パワーと時間を設定して、ロボットを右に回転させます。"));
        ListView menuList = findViewById(R.id.drawer_list);
        menuList.setAdapter(menuItemAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // positionが1から始まるため
                int id = i + 1;
                ItemDataArray.add(new ItemDataModel(id, 100, 100, 2));
                //listView.setAdapter(mAdapter);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        Button startButton = findViewById(R.id.startButton);
        Button connectButton = findViewById(R.id.connectButton);

        startButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        bluetooth.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ロボットとのBlueTooth接続処理
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                String deviceAddress = data.getStringExtra("deviceAddress");
                Log.i("deviceAddress", deviceAddress);
                bluetooth.connectToAddress(deviceAddress);
            }
        }
        //Bluetoothの有効化処理
        else if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.enable();
            }
        } else {
            Toast.makeText(this, "BluetoothがONになっていません!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onConnect(BluetoothDevice device) {
        connectStatus.setText(device.getName() + "に接続されています");
        Toast.makeText(this, "接続 to " + device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        connectStatus.setText("接続されていません");
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
        connectStatus.setText("接続されていません");
        Toast.makeText(this, "接続できません", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectButton:
                if (bluetooth.isConnected()) {
                    bluetooth.disconnect();
                } else if (bluetoothAdapter != null) {
                    Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
                break;

            case R.id.startButton:
                //BlueToothで送る文字列のnullチェック
                if (generateBTCommand()[0].length() == 0) {
                    Toast.makeText(MainActivity.this, "送るデータがありません", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Bluetooth接続チェック
                if (!bluetooth.isConnected()) {
                    Toast.makeText(MainActivity.this, "ロボットが接続されていません", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (String BTCommand :generateBTCommand()) {
                    // データフォーマット通りの文字列が送信される
                    // 最初に f が2つあったら複数送信 ffのあとに送る回数が入ってる
                    bluetooth.send(BTCommand);
                }
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // ダイアログの表示
        EditImageParamDialog editImageParamDialog = new EditImageParamDialog();
        Bundle data = new Bundle();
        data.putInt("orderId", ItemDataArray.get(position).getOrderId());
        data.putInt("RightSpeed", ItemDataArray.get(position).getRightSpeed());
        data.putInt("LeftSpeed", ItemDataArray.get(position).getLeftSpeed());
        data.putInt("time", ItemDataArray.get(position).getTime());
        data.putInt("listItemPosition", position);
        editImageParamDialog.setArguments(data);
        editImageParamDialog.show(getFragmentManager(), null);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        //とりあえずアイテム消す為。後で消す
        ItemDataArray.remove(position);
        mRecyclerView.setAdapter(mAdapter);
        return true;
    }

    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        ItemDataArray.set(listPosition,dataModel);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private String singleBT() {
        StringBuilder sendText = new StringBuilder();

        //imageId、Time、Speedの文字列を連結
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            sendText.append(mAdapter.getItem(i).getOrderId());
            sendText.append(String.format("%02d", mAdapter.getItem(i).getTime()));
            sendText.append(String.format("%03d", mAdapter.getItem(i).getRightSpeed()));
            sendText.append(String.format("%03d", mAdapter.getItem(i).getLeftSpeed()));
        }
        sendText.append('\0');  //1命令分の終端文字

        return sendText.toString();
    }

    @SuppressLint("DefaultLocale")
    private String[] multiBT() {
        int arrayNum = mAdapter.getItemCount() / 6 + 1;
        String[] strings = new String[arrayNum];
        StringBuilder tmpText = new StringBuilder();
        for (int i = 1; i <= mAdapter.getItemCount(); i++) {
            tmpText.append(mAdapter.getItem(i-1).getOrderId());
            tmpText.append(String.format("%02d", mAdapter.getItem(i-1).getTime()));
            tmpText.append(String.format("%03d", mAdapter.getItem(i-1).getRightSpeed()));
            tmpText.append(String.format("%03d", mAdapter.getItem(i-1).getLeftSpeed()));

            if (i % 6 == 0) {
                tmpText.append('\0');  //1命令分の終端文字
                strings[(i / 6) -1] = tmpText.toString();
                tmpText.setLength(0);
            }
        }
        tmpText.append('\0');  //1命令分の終端文字
        strings[arrayNum-1] = tmpText.toString();
        ArrayList<String> multiBTCommand = new ArrayList<>();
        String header = "ff" + strings.length;
        multiBTCommand.add(header);
        for (String s : strings){
            multiBTCommand.add(s);
        }
        return multiBTCommand.toArray(new String[multiBTCommand.size()]);
    }

    private String[] generateBTCommand() {
        if (mAdapter.getItemCount() < 7) {
            //singleBT();
            return new String[]{singleBT()};
        } else {
            return multiBT();
        }
    }


}
