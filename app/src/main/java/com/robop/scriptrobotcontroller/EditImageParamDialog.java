package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditImageParamDialog extends DialogFragment{

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);

        final int orderId =  getArguments().getInt("orderId");
        final int rightSpeed = getArguments().getInt("RightSpeed");
        final int leftSpeed = getArguments().getInt("LeftSpeed");
        final int time = getArguments().getInt("time");
        final int listItemPosition = getArguments().getInt("listItemPosition");

        final ItemDataModel dataModel = new ItemDataModel(orderId,rightSpeed,leftSpeed,time);



        final EditText editSpeed = view.findViewById(R.id.edit_speed);
        editSpeed.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editTime = view.findViewById(R.id.edit_time);
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        //TODO ここ治して！View足りない！

        editSpeed.setText(Integer.toString(dataModel.getRightSpeed()));
        editSpeed.setText(Integer.toString(dataModel.getLeftSpeed()));
        editTime.setText(Integer.toString(dataModel.getTime()));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editSpeed.getText().toString().length() != 0 && editTime.getText().toString().length() != 0){
                            // 数値が入力されてる時
                            //TODO ここ治して！View足りない！
                            dataModel.setRightSpeed(Integer.valueOf(editSpeed.getText().toString()));
                            dataModel.setLeftSpeed(Integer.valueOf(editSpeed.getText().toString()));
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
