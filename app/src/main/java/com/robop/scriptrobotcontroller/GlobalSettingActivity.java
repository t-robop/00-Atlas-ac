package com.robop.scriptrobotcontroller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GlobalSettingActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText frontWheelLeft, frontWheelRight, backWheelLeft, backWheelRight, leftWheelLeft, leftWheelRight, rightWheelLeft, rightWheelRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_setting);
        preferences = getSharedPreferences("globalSetting", MODE_PRIVATE);

        frontWheelLeft = findViewById(R.id.global_front_L);
        frontWheelLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        frontWheelRight = findViewById(R.id.global_front_R);
        frontWheelRight.setInputType(InputType.TYPE_CLASS_NUMBER);
        backWheelLeft = findViewById(R.id.global_back_L);
        backWheelLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        backWheelRight = findViewById(R.id.global_back_R);
        backWheelRight.setInputType(InputType.TYPE_CLASS_NUMBER);
        leftWheelLeft = findViewById(R.id.global_left_L);
        leftWheelLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        leftWheelRight = findViewById(R.id.global_left_R);
        leftWheelRight.setInputType(InputType.TYPE_CLASS_NUMBER);
        rightWheelLeft = findViewById(R.id.global_right_L);
        rightWheelLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        rightWheelRight = findViewById(R.id.global_right_R);
        rightWheelRight.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText[] editTexts = {frontWheelLeft, frontWheelRight, backWheelLeft, backWheelRight, leftWheelLeft, leftWheelRight, rightWheelLeft, rightWheelRight};

        Button saveButton = findViewById(R.id.global_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorText = null;

                for (EditText editText : editTexts) {
                    if (!sizeCheck(editText.getText().toString())) {
                        errorText = "値が0~255の範囲内にありません";
                        break;
                    }
                }

                if (errorText != null){
                    Toast.makeText(GlobalSettingActivity.this, errorText, Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor;
                    editor = preferences.edit();

                    editor.putInt("frontWheelLeft", Integer.valueOf(frontWheelLeft.getText().toString()));
                    editor.putInt("frontWheelRight", Integer.valueOf(frontWheelRight.getText().toString()));
                    editor.putInt("backWheelLeft", Integer.valueOf(backWheelLeft.getText().toString()));
                    editor.putInt("backWheelRight", Integer.valueOf(backWheelRight.getText().toString()));
                    editor.putInt("leftWheelLeft", Integer.valueOf(leftWheelLeft.getText().toString()));
                    editor.putInt("leftWheelRight", Integer.valueOf(leftWheelRight.getText().toString()));
                    editor.putInt("rightWheelLeft", Integer.valueOf(rightWheelLeft.getText().toString()));
                    editor.putInt("rightWheelRight", Integer.valueOf(rightWheelRight.getText().toString()));

                    editor.apply();
                    Toast.makeText(GlobalSettingActivity.this, "保存しました", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        int paramFrontLeft = preferences.getInt("frontWheelLeft", 100);
        int paramFrontRight = preferences.getInt("frontWheelRight", 100);
        int paramBackLeft = preferences.getInt("backWheelLeft", 100);
        int paramBackRight = preferences.getInt("backWheelRight", 100);
        int paramLeftRotateLeft = preferences.getInt("leftWheelLeft", 100);
        int paramLeftRotateRight = preferences.getInt("leftWheelRight", 100);
        int paramRightRotateLeft = preferences.getInt("rightWheelLeft", 100);
        int paramRightRotateRight = preferences.getInt("rightWheelRight", 100);

        frontWheelLeft.setText(String.valueOf(paramFrontLeft));
        frontWheelRight.setText(String.valueOf(paramFrontRight));
        backWheelLeft.setText(String.valueOf(paramBackLeft));
        backWheelRight.setText(String.valueOf(paramBackRight));
        leftWheelLeft.setText(String.valueOf(paramLeftRotateLeft));
        leftWheelRight.setText(String.valueOf(paramLeftRotateRight));
        rightWheelLeft.setText(String.valueOf(paramRightRotateLeft));
        rightWheelRight.setText(String.valueOf(paramRightRotateRight));
    }

    //設定値が0~255の範囲内にあるかどうか
    boolean sizeCheck(String speedStr){
        if (speedStr.equals("")){
            return false;
        }

        int num = Integer.parseInt(speedStr);
        return num >= 0 && num <= 255;
    }
}
