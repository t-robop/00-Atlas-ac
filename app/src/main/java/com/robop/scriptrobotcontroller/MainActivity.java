package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private int DEFAULT_TIME = 10;
    private int DEFAULT_BLOCK_STATE = 0;

    private int DEFAULT_FRONT_WHEEL_R = 220;
    private int DEFAULT_FRONT_WHEEL_L = 220;
    private int DEFAULT_BACK_WHEEL_R = 220;
    private int DEFAULT_BACK_WHEEL_L = 220;
    private int DEFAULT_R_WHEEL_R = 220;
    private int DEFAULT_R_WHEEL_L = 220;
    private int DEFAULT_L_WHEEL_R = 220;
    private int DEFAULT_L_WHEEL_L = 220;

    private float DEFAULT_POWER = 0.5f;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        connectImg = findViewById(R.id.connect_img);

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
                        Log.d("", "");
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);

                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
                        }
                    }
                });
        itemDecor.attachToRecyclerView(mRecyclerView);

        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(this);
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "まえにすすむ", "パワーと時間を設定して、ロボットを前に動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "うしろにすすむ", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "ひだりにまがる", "パワーと時間を設定して、ロボットを左に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "みぎにまがる", "パワーと時間を設定して、ロボットを右に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, "ループはじまり", "ループの始まり"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, "ループおわり", "ループの終わり"));
        ListView brockList = findViewById(R.id.brock_list);
        brockList.setAdapter(menuItemAdapter);

        brockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // positionが1から始まるため
                int orderId = i + 1;
                switch (orderId) {
                    case 1: //前進
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_FRONT_WHEEL_R, DEFAULT_FRONT_WHEEL_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, DEFAULT_POWER));
                        break;
                    case 2: //後退
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_BACK_WHEEL_R, DEFAULT_BACK_WHEEL_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, DEFAULT_POWER));
                        break;
                    case 3: //左回転
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_L_WHEEL_R, DEFAULT_L_WHEEL_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, DEFAULT_POWER));
                        break;
                    case 4: //右回転
                        mAdapter.addItem(new ItemDataModel(orderId, DEFAULT_R_WHEEL_R, DEFAULT_R_WHEEL_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, DEFAULT_POWER));
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

        preferences = getSharedPreferences("globalSetting", MODE_PRIVATE);

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

                String[] commands = generateBTCommand(getForDeployed());
                for (String command : commands) {
                    // データフォーマット通りの文字列が送信される
                    // 最初に f が2つあったら複数送信 ffのあとに送る回数が入ってる
                    Log.d("SendCommand", command);
                    bluetooth.send(command);
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
                    mAdapter.getItem(position).getRightRelativeSpeed(),
                    mAdapter.getItem(position).getLeftRelativeSpeed(),
                    mAdapter.getItem(position).getTime(),
                    mAdapter.getItem(position).getBlockState(),
                    mAdapter.getItem(position).getSeekBarRate());

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editParamDialog.setArguments(data);
            editParamDialog.show(getFragmentManager(), null);
        }

    }

    //View更新
    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        mAdapter.setItem(listPosition, dataModel);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private String singleBT(ArrayList<ItemDataModel> dataArray) {
        int right = preferences.getInt("frontWheelRight", 220);
        int left = preferences.getInt("frontWheelLeft", 220);

        StringBuilder sendText = new StringBuilder();

        //OrderId、Time、Speedの文字列を連結
        for (int i = 0; i < dataArray.size(); i++) {
            sendText.append(dataArray.get(i).getOrderId());
            sendText.append(String.format("%02d", dataArray.get(i).getTime()));
            int convertRight = (int) (dataArray.get(i).getSeekBarRate() * right);
            int convertLeft = (int) (dataArray.get(i).getSeekBarRate() * left);
            sendText.append(String.format("%03d", convertRight));
            sendText.append(String.format("%03d", convertLeft));
        }
        sendText.append('\0');  //1命令分の終端文字

        return sendText.toString();
    }

    private String[] multiBT(ArrayList<ItemDataModel> dataArray) {
        int right = preferences.getInt("frontWheelRight", 220);
        int left = preferences.getInt("frontWheelLeft", 220);

        int arrayNum = dataArray.size() / 6 + 1;
        String[] strings = new String[arrayNum];
        StringBuilder tmpText = new StringBuilder();
        for (int i = 1; i <= dataArray.size(); i++) {
            tmpText.append(dataArray.get(i - 1).getOrderId());
            tmpText.append(String.format("%02d", dataArray.get(i - 1).getTime()));

            int convertRight = (int) (dataArray.get(i - 1).getSeekBarRate() * right);
            int convertLeft = (int) (dataArray.get(i - 1).getSeekBarRate() * left);
            tmpText.append(String.format("%03d", convertRight));
            tmpText.append(String.format("%03d", convertLeft));

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

    private ArrayList<ItemDataModel> getForDeployed() {
        String str = null;

        if (mAdapter.getItemCount() == 0) {
            Toast.makeText(this, "おくるデータがありません", Toast.LENGTH_SHORT).show();
            return mAdapter.getAllItem();
        }

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            str += mAdapter.getItem(i).getBlockState();
        }

        //  forブロックが入ってなかった時
        if (str.indexOf("1") == -1) {
            // 一致しなかったら
            return mAdapter.getAllItem();

            // forブロックが入ってた時
        } else {
            ArrayList<ItemDataModel> list = mAdapter.getAllItem();
            return evolutionItems(list);
        }

    }


    private String[] generateBTCommand(ArrayList<ItemDataModel> dataArray) {
        if (dataArray.size() < 7) {
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
        RealmResults<ItemDataModel> realmResults = realm.where(ItemDataModel.class).findAll();
        realmResults.deleteAllFromRealm();

        List<ItemDataModel> items = new ArrayList<>();
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            items.add(mAdapter.getItem(i));
        }
        realm.insert(items);
        realm.commitTransaction();
        realm.close();
    }

    public void setAdapter(ItemDataModel dataModel) {
        mAdapter.addItem(dataModel);
        mAdapter.notifyDataSetChanged();
    }

    boolean loopErrorCheck() {
        int loopStart = 0, loopEnd = 0;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).getBlockState() == 1) {
                loopStart++;
            } else if (mAdapter.getItem(i).getBlockState() == 2) {
                loopEnd++;
                if (loopStart < loopEnd) {
                    Toast.makeText(this, "ループブロックがおかしいです", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        if ((loopStart < loopEnd)) {
            Toast.makeText(this, "ループブロックがおかしいです", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loopStart > loopEnd) {
            Toast.makeText(this, "ループブロックがおかしいです", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //todo こいつに送信前のリストデータを与えれば二重loop処理が動くはず
    //完全体に進化するメソッド(結果にCommitします)
    public ArrayList<ItemDataModel> evolutionItems(ArrayList<ItemDataModel> items) {
        return convertLoopItem(items, 0, 0, 0);
    }

    //loop文があったら外してリスト化してくれるメソッド
    public ArrayList<ItemDataModel> convertLoopItem(ArrayList<ItemDataModel> items, int posLoopStart, int cntLoop, int depth) {
        int posEnd = -1;
        int i;

        for (i = depth; i < items.size(); i++) {
            if (items.get(i).getBlockState() == 1) {
                items = convertLoopItem(items, i, items.get(i).getLoopCount(), ++depth);
            }
            if (items.get(i).getBlockState() == 2) {
                posEnd = i;
                break;
            }
        }
        if (items.get(posLoopStart).getBlockState() != 1) {
            return items;
        }

        //loop前の処理を保持
        ArrayList<ItemDataModel> content = new ArrayList<>();
        for (i = 0; i < posLoopStart; i++) {
            content.add(items.get(i));
        }
        //連結
        for (int cnt = 0; cnt < cntLoop; cnt++) {
            for (i = posLoopStart + 1; i < posEnd; i++) {
                content.add(items.get(i));
            }
        }
        //loop外の残りを連結
        for (i = posEnd + 1; i < items.size(); i++) {
            content.add(items.get(i));
        }

        return content;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // 戻るボタンが押されたとき
        if (e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // ボタンが離されたとき
            if (e.getAction() == KeyEvent.ACTION_UP) {
                Toast.makeText(this, "無効です", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }
}
