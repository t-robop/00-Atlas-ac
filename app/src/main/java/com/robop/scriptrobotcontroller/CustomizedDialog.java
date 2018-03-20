package com.robop.scriptrobotcontroller;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

//TODO Android公式のAlertDialogデザインに従ったものにしよう。カスタムレイアウトの項目を参照
//https://developer.android.com/guide/topics/ui/dialogs.html?hl=ja
public class CustomizedDialog extends DialogFragment{

    private View.OnClickListener okButtonClickListener = null;    //Dialogのレイアウト読み込み
    //Dialogレイアウト取得用view
    View inputView;

    Dialog dialog;
    //ダイアログ
    TextView textView;
    TextView timeText, speedText;

    NumberPicker timePic, speedPic;
    //決定・キャンセル ボタン
    Button btSet;
    Button btCancel;

    public static CustomizedDialog newInstance(){
        return new CustomizedDialog();
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //xmlとの紐付け

        LayoutInflater inflater = getActivity().getLayoutInflater();
        inputView = inflater.inflate(R.layout.layout_dialog,null);
        timeText = inputView.findViewById(R.id.timetext);
        speedText = inputView.findViewById(R.id.speedtext);
        timePic = inputView.findViewById(R.id.timepic);
        speedPic = inputView.findViewById(R.id.speedpic);
        btSet = inputView.findViewById(R.id.bt_ok);
        btCancel = inputView.findViewById(R.id.bt_close);
        timePic.setMaxValue(20);
        timePic.setMinValue(1);
        speedPic.setMaxValue(20);
        speedPic.setMinValue(1);



        //ダイアログ作成
        dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(inputView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //TODO set押した時の処理
        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //クローズボタン押下時はダイアログを消す
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btSet.setOnClickListener(okButtonClickListener);
        return dialog;
    }

    //ダイアログのサイズ等の指定


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        lp.width = (int) (metrics.widthPixels * 0.8);//横幅を80%
        lp.height = (int) (metrics.heightPixels * 0.8);//高さを80%
        //lp.x = 100; //表示位置を指定した分、右へ移動
        //lp.y = 200; //表示位置を指定した分、下へ移動
        dialog.getWindow().setAttributes(lp);
    }

    public void setOnSetButtonClickListener(View.OnClickListener listener){
        this.okButtonClickListener = listener;
    }

    /*public Long getInputValue(){

    }*/

}
