package com.robop.scriptrobotcontroller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GlobalSettingActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText frontLeft, frontRight, backLeft, backRight, leftRotateLeft, leftRotateRight, rightRotateLeft, rightRotateRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_setting);

        frontLeft = findViewById(R.id.global_front_L);
        frontRight = findViewById(R.id.global_front_R);
        backLeft = findViewById(R.id.global_back_L);
        backRight = findViewById(R.id.global_back_R);
        leftRotateLeft = findViewById(R.id.global_left_L);
        leftRotateRight = findViewById(R.id.global_left_R);
        rightRotateLeft = findViewById(R.id.global_right_L);
        rightRotateRight = findViewById(R.id.global_right_R);

        final EditText[] editTexts = {frontLeft, frontRight, backLeft, backRight, leftRotateLeft, leftRotateRight, rightRotateLeft, rightRotateRight};

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

                    editor.putInt("frontLeft", Integer.valueOf(frontLeft.getText().toString()));
                    editor.putInt("frontRight", Integer.valueOf(frontRight.getText().toString()));
                    editor.putInt("backLeft", Integer.valueOf(backLeft.getText().toString()));
                    editor.putInt("backRight", Integer.valueOf(backRight.getText().toString()));
                    editor.putInt("leftRotateLeft", Integer.valueOf(leftRotateLeft.getText().toString()));
                    editor.putInt("leftRotateRight", Integer.valueOf(leftRotateRight.getText().toString()));
                    editor.putInt("rightRotateLeft", Integer.valueOf(rightRotateLeft.getText().toString()));
                    editor.putInt("rightRotateRight", Integer.valueOf(rightRotateRight.getText().toString()));

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

        preferences = getSharedPreferences("globalSetting", MODE_PRIVATE);

        int paramFrontLeft = preferences.getInt("frontLeft", 100);
        int paramFrontRight = preferences.getInt("frontRight", 100);
        int paramBackLeft = preferences.getInt("backLeft", 100);
        int paramBackRight = preferences.getInt("backRight", 100);
        int paramLeftRotateLeft = preferences.getInt("leftRotateLeft", 100);
        int paramLeftRotateRight = preferences.getInt("leftRotateRight", 100);
        int paramRightRotateLeft = preferences.getInt("rightRotateLeft", 100);
        int paramRightRotateRight = preferences.getInt("rightRotateRight", 100);

        frontLeft.setText(String.valueOf(paramFrontLeft));
        frontRight.setText(String.valueOf(paramFrontRight));
        backLeft.setText(String.valueOf(paramBackLeft));
        backRight.setText(String.valueOf(paramBackRight));
        leftRotateLeft.setText(String.valueOf(paramLeftRotateLeft));
        leftRotateRight.setText(String.valueOf(paramLeftRotateRight));
        rightRotateLeft.setText(String.valueOf(paramRightRotateLeft));
        rightRotateRight.setText(String.valueOf(paramRightRotateRight));
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
