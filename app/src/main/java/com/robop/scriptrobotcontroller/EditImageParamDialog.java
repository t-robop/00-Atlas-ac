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

        final int currentImageRightSpeed = getArguments().getInt("currentImageRightSpeed");
        final int currentImageLeftSpeed = getArguments().getInt("currentImageLeftSpeed");
        final int currentImageTime = getArguments().getInt("currentImageTime");
        final int listItemPosition = getArguments().getInt("listItemPosition");

        final EditText editSpeed = view.findViewById(R.id.edit_speed);
        editSpeed.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editTime = view.findViewById(R.id.edit_time);
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        //TODO ここ治して！View足りない！

        editSpeed.setText(Integer.toString(currentImageRightSpeed));
        editSpeed.setText(Integer.toString(currentImageLeftSpeed));
        editTime.setText(Integer.toString(currentImageTime));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editSpeed.getText().toString().length() != 0 && editTime.getText().toString().length() != 0){
                            // 数値が入力されてる時
                            //TODO ここ治して！View足りない！
                            int rightSpeedParam = Integer.valueOf(editSpeed.getText().toString());
                            int leftSpeedParam = Integer.valueOf(editSpeed.getText().toString());
                            int timeParam = Integer.valueOf(editTime.getText().toString());

                            // とりあえず数値丸め処置した
                            if (rightSpeedParam > 255){
                                rightSpeedParam=255;
                            }
                            if (leftSpeedParam > 255){
                                leftSpeedParam=255;
                            }
                            if (timeParam>20){
                                timeParam=20;
                            }

                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.resetItemParam(listItemPosition, rightSpeedParam, leftSpeedParam, timeParam);
                        }

                    }
                })
                .setNegativeButton("キャンセル", null);

        return builder.create();

    }

}
