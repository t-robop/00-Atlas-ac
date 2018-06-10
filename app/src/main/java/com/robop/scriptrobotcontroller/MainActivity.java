package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DeviceCallback, RecyclerAdapter.OnRecyclerListener {

    Bluetooth bluetooth;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    private final int REQUEST_CONNECT_DEVICE = 9;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private TextView connectStatus;
    private ImageView connectImg;

    private int DEFAULT_TIME = 2;
    private int DEFAULT_BLOCK_STATE = 0;

    private int DEFAULT_FRONT_R = 100;
    private int DEFAULT_FRONT_L = 100;
    private int DEFAULT_BACK_R = 100;
    private int DEFAULT_BACK_L = 100;
    private int DEFAULT_R_ROTATE_R = 100;
    private int DEFAULT_R_ROTATE_L = 100;
    private int DEFAULT_L_ROTATE_R = 100;
    private int DEFAULT_L_ROTATE_L = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ItemDataModel> query = realm.where(ItemDataModel.class);
        ItemDataModel item = query.findFirst();
        realm.close();
        if (item != null) {
            DataLoadDialogFragment dataLoadDialogFragment = new DataLoadDialogFragment();
            dataLoadDialogFragment.show(getFragmentManager(), null);
        }

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

        bluetooth.setDeviceCallback(this);

        connectStatus = findViewById(R.id.connect_status);
        connectImg=findViewById(R.id.connect_img);

        //RecyclerView処理
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<ItemDataModel> ItemDataArray = new ArrayList<>();
        mAdapter = new RecyclerAdapter(ItemDataArray, this);
        mRecyclerView.setAdapter(mAdapter);


        //ItemTouchHelper
        ItemTouchHelper itemDecor = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        mAdapter.itemMoved(fromPos, toPos);
                        mAdapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        mAdapter.removeItem(fromPos);
                        mAdapter.notifyItemRemoved(fromPos);
                        Log.d("","");
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);

                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG ) {
                            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
                        }
                    }
                });
        itemDecor.attachToRecyclerView(mRecyclerView);

        MenuItemAdapter drawerMenuItemAdapter = new MenuItemAdapter(this);
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "前進", "パワーと時間を設定して、ロボットを前に動かします。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "後退", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "左回転", "パワーと時間を設定して、ロボットを左に回転させます。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "右回転", "パワーと時間を設定して、ロボットを右に回転させます。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, "ループ開始", "ループの始まり"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, "ループ終了", "ループの終わり"));
        ListView drawerMenuList = findViewById(R.id.drawer_list);
        drawerMenuList.setAdapter(drawerMenuItemAdapter);

        drawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // positionが1から始まるため
                int orderId = i + 1;
                switch (orderId) {
                    case 1: //前進
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_FRONT_R, DEFAULT_FRONT_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE));
                        break;
                    case 2: //後退
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_BACK_R, DEFAULT_BACK_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE));
                        break;
                    case 3: //左回転
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_L_ROTATE_R, DEFAULT_L_ROTATE_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE));
                        break;
                    case 4: //右回転
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_R_ROTATE_R, DEFAULT_R_ROTATE_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE));
                        break;
                    case 5: //ループスタート
                        mAdapter.addItem(new ItemDataModel(orderId, 1, 2));
                        break;
                    case 6: //ループエンド
                        mAdapter.addItem(new ItemDataModel(orderId, 2, 0));
                        break;

                }
                mRecyclerView.setAdapter(mAdapter);

            }
        });

        Button startButton = findViewById(R.id.start_button);
        Button connectButton = findViewById(R.id.connect_button);
        Button globalSpeedSettingButton = findViewById(R.id.global_setting);

        startButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);
        globalSpeedSettingButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        bluetooth.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("globalSetting", MODE_PRIVATE);

        DEFAULT_FRONT_R = preferences.getInt("frontRight", 100);
        DEFAULT_FRONT_L = preferences.getInt("frontLeft", 100);
        DEFAULT_BACK_R = preferences.getInt("backRight", 100);
        DEFAULT_BACK_L = preferences.getInt("backLeft", 100);
        DEFAULT_R_ROTATE_R = preferences.getInt("rightRotateRight", 100);
        DEFAULT_R_ROTATE_L = preferences.getInt("rightRotateLeft", 100);
        DEFAULT_L_ROTATE_R = preferences.getInt("leftRotateRight", 100);
        DEFAULT_L_ROTATE_L = preferences.getInt("leftRotateLeft", 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    public void onDeviceConnected(BluetoothDevice device) {
        bluetoothDevice = device;
        this.runOnUiThread(new Runnable() {
            public void run() {
                connectImg.setImageResource(R.drawable.connect);
                connectStatus.setText(bluetoothDevice.getName() + "に接続されています");
                Toast.makeText(MainActivity.this, "接続 to " + bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device, String message) {
        bluetoothDevice = device;
        this.runOnUiThread(new Runnable() {
            public void run() {
                connectImg.setImageResource(R.drawable.disconnect);
                connectStatus.setText("接続されていません");
                Toast.makeText(MainActivity.this, "接続が切れました", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        bluetoothDevice = device;
        this.runOnUiThread(new Runnable() {
            public void run() {
                connectStatus.setText("接続されていません");
                Toast.makeText(MainActivity.this, "接続できません", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_button:
                if (bluetooth.isConnected()) {
                    bluetooth.disconnect();
                } else if (bluetoothAdapter != null) {
                    Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
                break;

            case R.id.global_setting:
                Intent intent = new Intent(this, GlobalSettingActivity.class);
                startActivity(intent);
                break;

            case R.id.start_button:
                if (!loopErrorCheck()) {
                    return;
                }
                fullGenerateDataArray.clear();

                autoSave();
                //BlueToothで送る文字列のnullチェック
                //TODO nullチェック用のメソッドを新規で作るべき
                if (generateBTCommand(mAdapter.getAllItem())[0].length() == 0) {
                    Toast.makeText(MainActivity.this, "送るデータがありません", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Bluetooth接続チェック
                if (!bluetooth.isConnected()) {
                    Toast.makeText(MainActivity.this, "ロボットが接続されていません", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (String BTCommand : GenerateBTWrapper()) {
                    // データフォーマット通りの文字列が送信される
                    // 最初に f が2つあったら複数送信 ffのあとに送る回数が入ってる
                    bluetooth.send(BTCommand);
                }
                break;
        }

    }

    @Override
    public void onRecyclerClicked(View view, int position) {
        Log.d("recyclerView", String.valueOf(position));

        //ループブロックのとき
        if (mAdapter.getItem(position).getBlockState() == 1) {
            EditLoopParamDialog editLoopParamDialog = new EditLoopParamDialog();
            Bundle data = new Bundle();
            ItemDataModel itemDataModel = new ItemDataModel(
                    mAdapter.getItem(position).getOrderId(),
                    mAdapter.getItem(position).getBlockState(),
                    mAdapter.getItem(position).getLoopCount());

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editLoopParamDialog.setArguments(data);
            editLoopParamDialog.show(getFragmentManager(), null);

            //基本動作ブロックのとき
        } else {
            EditParamDialog editParamDialog = new EditParamDialog();
            Bundle data = new Bundle();

            ItemDataModel itemDataModel = new ItemDataModel(
                    mAdapter.getItem(position).getOrderId(),
                    mAdapter.getItem(position).getRightSpeed(),
                    mAdapter.getItem(position).getLeftSpeed(),
                    mAdapter.getItem(position).getTime(),
                    mAdapter.getItem(position).getBlockState());

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editParamDialog.setArguments(data);
            editParamDialog.show(getFragmentManager(), null);
        }

    }

    //View更新
    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        mAdapter.setItem(listPosition,dataModel);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private String singleBT(ArrayList<ItemDataModel> dataArray) {
        StringBuilder sendText = new StringBuilder();

        //OrderId、Time、Speedの文字列を連結
        for (int i = 0; i < dataArray.size(); i++) {
            sendText.append(dataArray.get(i).getOrderId());
            sendText.append(String.format("%02d", dataArray.get(i).getTime()));
            sendText.append(String.format("%03d", dataArray.get(i).getRightSpeed()));
            sendText.append(String.format("%03d", dataArray.get(i).getLeftSpeed()));
        }
        sendText.append('\0');  //1命令分の終端文字

        return sendText.toString();
    }

    @SuppressLint("DefaultLocale")
    private String[] multiBT(ArrayList<ItemDataModel> dataArray) {
        int arrayNum = dataArray.size() / 6 + 1;
        String[] strings = new String[arrayNum];
        StringBuilder tmpText = new StringBuilder();
        for (int i = 1; i <= dataArray.size(); i++) {
            tmpText.append(dataArray.get(i - 1).getOrderId());
            tmpText.append(String.format("%02d", dataArray.get(i - 1).getTime()));
            tmpText.append(String.format("%03d", dataArray.get(i - 1).getRightSpeed()));
            tmpText.append(String.format("%03d", dataArray.get(i - 1).getLeftSpeed()));

            if (i % 6 == 0) {
                tmpText.append('\0');  //1命令分の終端文字
                strings[(i / 6) - 1] = tmpText.toString();
                tmpText.setLength(0);
            }
        }
        tmpText.append('\0');  //1命令分の終端文字
        strings[arrayNum - 1] = tmpText.toString();
        ArrayList<String> multiBTCommand = new ArrayList<>();
        String header = "ff" + strings.length + "\0";
        multiBTCommand.add(header);
        multiBTCommand.addAll(Arrays.asList(strings));
        return multiBTCommand.toArray(new String[multiBTCommand.size()]);
    }

    private String[] GenerateBTWrapper() {
        String str = null;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            str += mAdapter.getItem(i).getBlockState();
        }
        //  forブロックが入ってなかった時
        if (str.indexOf("1") == -1) {
            // 一致しなかったら
            return generateBTCommand(mAdapter.getAllItem());

            // forブロックが入ってた時
        } else {
            convertLoopCommand(0,mAdapter.getItemCount()-1,0);
            return generateBTCommand(fullGenerateDataArray);
        }

    }


    private ArrayList<ItemDataModel> fullGenerateDataArray = new ArrayList<>();
    private void convertLoopCommand(int start,int end, int depth) {

        for (int i = start; i <= end; i++) {
            if (mAdapter.getItem(i).getBlockState() == 1) {
                convertLoopCommand(i + 1, end, depth + 1);
                for (;i < end; i++){
                    if (mAdapter.getItem(i).getBlockState() == 2) {
                        break;
                    }
                }

            }else if (mAdapter.getItem(i).getBlockState() == 2) {
                if (depth != 0) {
                    ArrayList<ItemDataModel> tmpDataArray = new ArrayList<>();
                    ArrayList<ItemDataModel> tmpDataArray2 = new ArrayList<>();
                    for (int j = start; j < i; j++) {
                        tmpDataArray.add(mAdapter.getItem(j));
                    }
                    for (int j = 0; j < mAdapter.getItem(start - 1).getLoopCount(); j++) {
                        tmpDataArray2.addAll(tmpDataArray);
                    }
                    fullGenerateDataArray.addAll(tmpDataArray2);
                    return;
                }
            } else if (depth == 0) {
                fullGenerateDataArray.add(mAdapter.getItem(i));
            }
        }
    }

    private String[] generateBTCommand(ArrayList<ItemDataModel> dataArray) {
        if (mAdapter.getItemCount() < 7) {
            return new String[]{singleBT(dataArray)};
        } else {
            return multiBT(dataArray);
        }
    }
    void autoSave() {
        Realm.init(this);

        //realm 削除
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ItemDataModel> datas = realm.where(ItemDataModel.class).findAll();
        datas.deleteAllFromRealm();

        List<ItemDataModel> items = new ArrayList<>();
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            items.add(mAdapter.getItem(i));
        }
        realm.insert(items);
        realm.commitTransaction();
        realm.close();
    }

    public void setAdapter(ItemDataModel dataModel){
        mAdapter.addItem(dataModel);
        mAdapter.notifyDataSetChanged();
    }

    boolean loopErrorCheck() {
        int loopStart = 0,loopEnd = 0;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).getBlockState() == 1) {
                loopStart++;
            } else if (mAdapter.getItem(i).getBlockState() == 2) {
                loopEnd++;
                if (loopStart < loopEnd) {
                    Toast.makeText(this,"forブロックの始まりより前に終わりが来ています",Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        if ((loopStart < loopEnd)) {
            Toast.makeText(this,"forブロックの終わりの数が多すぎます",Toast.LENGTH_SHORT).show();
            return false;
        } else if (loopStart > loopEnd){
            Toast.makeText(this,"forブロックの始まりの数が多すぎます",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
