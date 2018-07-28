package com.robop.scriptrobotcontroller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GlobalSettingActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText frontWheelLeft, frontWheelRight, backWheelLeft, backWheelRight;

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


        final EditText[] editTexts = {frontWheelLeft, frontWheelRight, backWheelLeft, backWheelRight};

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

                if (errorText != null) {
                    Toast.makeText(GlobalSettingActivity.this, errorText, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor;
                    editor = preferences.edit();

                    editor.putInt("frontWheelLeft", Integer.valueOf(frontWheelLeft.getText().toString()));
                    editor.putInt("frontWheelRight", Integer.valueOf(frontWheelRight.getText().toString()));
                    editor.putInt("backWheelLeft", Integer.valueOf(backWheelLeft.getText().toString()));
                    editor.putInt("backWheelRight", Integer.valueOf(backWheelRight.getText().toString()));

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


        int paramFrontLeft = preferences.getInt("frontWheelLeft", 220);
        int paramFrontRight = preferences.getInt("frontWheelRight", 220);
        int paramBackLeft = preferences.getInt("backWheelLeft", 220);
        int paramBackRight = preferences.getInt("backWheelRight", 220);

        frontWheelLeft.setText(String.valueOf(paramFrontLeft));
        frontWheelLeft.setSelection(frontWheelLeft.getText().length());

        frontWheelRight.setText(String.valueOf(paramFrontRight));
        frontWheelRight.setSelection(frontWheelRight.getText().length());

        backWheelLeft.setText(String.valueOf(paramBackLeft));
        backWheelLeft.setSelection(backWheelLeft.getText().length());

        backWheelRight.setText(String.valueOf(paramBackRight));
        backWheelRight.setSelection(backWheelRight.getText().length());
    }

    //設定値が0~255の範囲内にあるかどうか
    boolean sizeCheck(String speedStr) {
        if (speedStr.equals("")) {
            return false;
        }

        int num = Integer.parseInt(speedStr);
        return num >= 0 && num <= 255;
    }
}
