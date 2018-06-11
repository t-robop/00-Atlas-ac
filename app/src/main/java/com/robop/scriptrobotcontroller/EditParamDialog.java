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

import static android.content.Context.MODE_PRIVATE;

public class EditParamDialog extends DialogFragment{

    private int DEFAULT_FRONT_WHEEL_R = 100;
    private int DEFAULT_FRONT_WHEEL_L = 100;
    private int DEFAULT_BACK_WHEEL_R = 100;
    private int DEFAULT_BACK_WHEEL_L = 100;
    private int DEFAULT_R_WHEEL_R = 100;
    private int DEFAULT_R_WHEEL_L = 100;
    private int DEFAULT_L_WHEEL_R = 100;
    private int DEFAULT_L_WHEEL_L = 100;

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);

        syncPreference();

        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        final EditText editSpeedRight = view.findViewById(R.id.edit_speed_right);
        editSpeedRight.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editSpeedLeft = view.findViewById(R.id.edit_speed_left);
        editSpeedLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editTime = view.findViewById(R.id.edit_time);
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        editSpeedRight.setText(Integer.toString(dataModel.getRightSpeed()));
        editSpeedLeft.setText(Integer.toString(dataModel.getLeftSpeed()));
        editTime.setText(Integer.toString(dataModel.getTime()));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editSpeedRight.getText().toString().length() != 0 && editSpeedLeft.getText().toString().length() != 0 && editTime.getText().toString().length() != 0){
                            // 数値が入力されてる時
                            double relativeRight = Double.valueOf(editSpeedRight.getText().toString()) / 100;
                            double relativeLeft = Double.valueOf(editSpeedLeft.getText().toString()) / 100;

                            switch (dataModel.getOrderId()){
                                case 1:
                                    dataModel.setRightSpeed((int)(DEFAULT_FRONT_WHEEL_R * relativeRight));
                                    dataModel.setLeftSpeed((int)(DEFAULT_FRONT_WHEEL_L * relativeLeft));
                                    break;

                                case 2:
                                    dataModel.setRightSpeed((int)(DEFAULT_BACK_WHEEL_R * relativeRight));
                                    dataModel.setLeftSpeed((int)(DEFAULT_BACK_WHEEL_L * relativeLeft));
                                    break;

                                case 3:
                                    dataModel.setRightSpeed((int)(DEFAULT_L_WHEEL_R * relativeRight));
                                    dataModel.setLeftSpeed((int)(DEFAULT_L_WHEEL_L * relativeLeft));
                                    break;

                                case 4:
                                    dataModel.setRightSpeed((int)(DEFAULT_R_WHEEL_R * relativeRight));
                                    dataModel.setLeftSpeed((int)(DEFAULT_R_WHEEL_L * relativeLeft));
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

    private void syncPreference(){
        SharedPreferences preferences = getActivity().getSharedPreferences("globalSetting", MODE_PRIVATE);

        DEFAULT_FRONT_WHEEL_R = preferences.getInt("frontWheelRight", 100);
        DEFAULT_FRONT_WHEEL_L = preferences.getInt("frontWheelLeft", 100);
        DEFAULT_BACK_WHEEL_R = preferences.getInt("backWheelRight", 100);
        DEFAULT_BACK_WHEEL_L = preferences.getInt("backWheelLeft", 100);
        DEFAULT_R_WHEEL_R = preferences.getInt("rightWheelRight", 100);
        DEFAULT_R_WHEEL_L = preferences.getInt("rightWheelLeft", 100);
        DEFAULT_L_WHEEL_R = preferences.getInt("leftWheelRight", 100);
        DEFAULT_L_WHEEL_L = preferences.getInt("leftWheelLeft", 100);
    }

}
