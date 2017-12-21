package com.robop.scriptrobotcontroller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
/**
 * Created by shiromu on 2017/12/11.
 */



public class CustomizedDialog extends DialogFragment{

    private View.OnClickListener okButtonClickLintener = null;
    //Dialogのレイアウト読み込み
    //Dialogレイアウト取得用view
    View inputView;

    Dialog dialog;
    //ダイアログ
    TextView textView;
    //決定・キャンセル ボタン
    Button btset;
    Button btcancel;

    public static CustomizedDialog newInstance(){
        CustomizedDialog fragment = new CustomizedDialog();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //xmlとの紐付け

        LayoutInflater inflater = getActivity().getLayoutInflater();
        inputView = inflater.inflate(R.layout.layout_dialog,null);
        textView = (TextView) inputView.findViewById(R.id.tex);
        btset = (Button)inputView.findViewById(R.id.bt_ok);
        btcancel = (Button)inputView.findViewById(R.id.bt_close);

        //ダイアログ作成
        dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(inputView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //set押した時の処理
        btset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        //クローズボタン押下時はダイアログを消す
        btcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btset.setOnClickListener(okButtonClickLintener);
        return dialog;
    }

    //ダイアログのサイズ等の指定


        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            lp.width = (int) (metrics.widthPixels * 0.8);//横幅を80%
            //lp.height = (int) (metrics.heightPixels * 0.8);//高さを80%
            //lp.x = 100; //表示位置を指定した分、右へ移動
            //lp.y = 200; //表示位置を指定した分、下へ移動
            dialog.getWindow().setAttributes(lp);
        }

        public void setOnSetButtonClickListener(View.OnClickListener listener){
        this.okButtonClickLintener = listener;
        }

        /*public Long getInputValue(){

        }*/

}
