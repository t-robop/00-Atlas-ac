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

        import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ItemDataList item;
    private ListView listView;
    private ListAdapter listAdapter;

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

        Button startButton = findViewById(R.id.startButton);
        Button finishButton = findViewById(R.id.finishButton);

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
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        item.remove(position);
                        listView.setAdapter(listAdapter);
                        return true;
                    }
                }
        );
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.interrupt(0,3);
                listView.setAdapter(listAdapter);
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.interrupt(4,2);
                listView.setAdapter(listAdapter);
            }
        });

    }

    Button setButtonLisetener(final int speed, final int time, final String imageId, int id){

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
