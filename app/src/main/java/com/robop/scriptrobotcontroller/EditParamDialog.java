package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import static android.content.Context.MODE_PRIVATE;

public class EditParamDialog extends DialogFragment{
    SharedPreferences prefs;
    private float seekRate;

    private int GLOBAL_FRONT_WHEEL_R;
    private int GLOBAL_FRONT_WHEEL_L;
    private int GLOBAL_BACK_WHEEL_R;
    private int GLOBAL_BACK_WHEEL_L;
    private int GLOBAL_R_WHEEL_R;
    private int GLOBAL_R_WHEEL_L;
    private int GLOBAL_L_WHEEL_R;
    private int GLOBAL_L_WHEEL_L;

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);


        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        //SharePreからデータを受け取る
        prefs = getActivity().getSharedPreferences("globalSetting", MODE_PRIVATE);
        GLOBAL_FRONT_WHEEL_R = prefs.getInt("frontWheelRight", 0);
        GLOBAL_FRONT_WHEEL_L = prefs.getInt("frontWheelLeft", 0);
        GLOBAL_BACK_WHEEL_R = prefs.getInt("backWheelRight", 0);
        GLOBAL_BACK_WHEEL_L = prefs.getInt("backWheelLeft", 0);
        GLOBAL_R_WHEEL_R = prefs.getInt("rightWheelRight", 0);
        GLOBAL_R_WHEEL_L = prefs.getInt("rightWheelLeft", 0);
        GLOBAL_L_WHEEL_R = prefs.getInt("leftWheelRight", 0);
        GLOBAL_L_WHEEL_L = prefs.getInt("leftWheelLeft", 0);


        final SeekBar seekBar = view.findViewById(R.id.powerSeekBar);
        seekRate = dataModel.getSeekBarRate();
        seekBar.setProgress((int) seekRate * 100);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekRate = (float) progress / 100;
                //dataModel.setSeekBarRate(seekRate);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        final EditText editTime = view.findViewById(R.id.edit_time);
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        editTime.setText(Integer.toString(dataModel.getTime()));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editTime.getText().toString().length() != 0){

                            switch (dataModel.getOrderId()){
                                case 1:
                                    dataModel.setRightRerativeSpeed((int)(GLOBAL_FRONT_WHEEL_R * seekRate));
                                    dataModel.setLeftRerativeSpeed((int)(GLOBAL_FRONT_WHEEL_L * seekRate));
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 2:
                                    dataModel.setRightRerativeSpeed((int)(GLOBAL_BACK_WHEEL_R * seekRate));
                                    dataModel.setLeftRerativeSpeed((int)(GLOBAL_BACK_WHEEL_L * seekRate));
                                    break;

                                case 3:
                                    dataModel.setRightRerativeSpeed((int)(GLOBAL_L_WHEEL_R * seekRate));
                                    dataModel.setLeftRerativeSpeed((int)(GLOBAL_L_WHEEL_L * seekRate));
                                    break;

                                case 4:
                                    dataModel.setRightRerativeSpeed((int)(GLOBAL_R_WHEEL_R * seekRate));
                                    dataModel.setLeftRerativeSpeed((int)(GLOBAL_R_WHEEL_L * seekRate));
                                    break;

                            }
                            dataModel.setTime(Integer.valueOf(editTime.getText().toString()));

                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.updateItemParam(listItemPosition, dataModel);
                        }
                    }
                })
                .setNegativeButton("キャンセル", null);

        return builder.create();

    }

}
