package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

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
        final SeekBar seekBar = view.findViewById(R.id.powerSeekBar);

        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        //SharePreからデータを受け取る
        prefs = getActivity().getSharedPreferences("globalSetting", MODE_PRIVATE);
        GLOBAL_FRONT_WHEEL_R = prefs.getInt("frontWheelRight", 220);
        GLOBAL_FRONT_WHEEL_L = prefs.getInt("frontWheelLeft", 220);
        GLOBAL_BACK_WHEEL_R = prefs.getInt("backWheelRight", 220);
        GLOBAL_BACK_WHEEL_L = prefs.getInt("backWheelLeft", 220);
        GLOBAL_R_WHEEL_R = prefs.getInt("rightWheelRight", 220);
        GLOBAL_R_WHEEL_L = prefs.getInt("rightWheelLeft", 220);
        GLOBAL_L_WHEEL_R = prefs.getInt("leftWheelRight", 220);
        GLOBAL_L_WHEEL_L = prefs.getInt("leftWheelLeft", 220);

        seekBar.setMax(100);
        seekRate = dataModel.getSeekBarRate();
        float rate = seekRate * 100.0f;
        seekBar.setProgress((int) rate);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekRate = (float) progress / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        final EditText editTime = view.findViewById(R.id.edit_time);
        //小数と整数に対応
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int dotPos = -1;
                int len = dest.length();
                for (int i = 0; i < len; i++) {
                    char c = dest.charAt(i);
                    if (c == '.' || c == ',') {
                        dotPos = i;
                        break;
                    }
                }
                if (dotPos >= 0) {

                    // protects against many dots
                    if (source.equals(".") || source.equals(","))
                    {
                        return "";
                    }
                    // if the text is entered before the dot
                    if (dend <= dotPos) {
                        return null;
                    }
                    if (len - dotPos > 2) {
                        return "";
                    }
                }

                return null;
            }
        };
        // フィルターの配列を作成
        InputFilter[] filters = new InputFilter[] {
                inputFilter,
                new InputFilter.LengthFilter(3)
        };
        // フィルターの配列をセット
        editTime.setFilters(filters);

        editTime.setText(Float.toString(((float) (dataModel.getTime()))/10f));
        editTime.setSelection(editTime.getText().length());

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editTime.getText().toString().length() != 0){

                            switch (dataModel.getOrderId()){
                                case 1:
                                    //dataModel.setRightRerativeSpeed((int)(seekRate));
                                    //dataModel.setLeftRerativeSpeed((int)(seekRate));
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 2:
                                    //dataModel.setRightRerativeSpeed((int)(seekRate));
                                    //dataModel.setLeftRerativeSpeed((int)(seekRate));
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 3:
                                    //dataModel.setRightRerativeSpeed((int)(seekRate));
                                    //dataModel.setLeftRerativeSpeed((int)(seekRate));
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 4:
                                    //dataModel.setRightRerativeSpeed((int)(seekRate));
                                    //dataModel.setLeftRerativeSpeed((int)(seekRate));
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                            }
                            dataModel.setTime((int) ((Float.parseFloat(editTime.getText().toString()))*10));

                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.updateItemParam(listItemPosition, dataModel);
                        }
                    }
                })
                .setNegativeButton("キャンセル", null);

        return builder.create();

    }

}
