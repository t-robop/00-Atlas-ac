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

import static android.content.Context.MODE_PRIVATE;

public class EditParamDialog extends DialogFragment {
    SharedPreferences prefs;
    private float seekRate;

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        final SeekBar seekBar = view.findViewById(R.id.powerSeekBar);

        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        //SharePreからデータを受け取る
        prefs = getActivity().getSharedPreferences("globalSetting", MODE_PRIVATE);

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
        editTime.setText(Float.toString((dataModel.getTime() / 10f)));

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String s = dest.toString();
                int UPPER_POINT_DIGITS_NUM = 1;
                int LOWER_POINT_DIGITS_NUM = 1;
                int i = s.indexOf(".");
                if (i >= 0) {
                    // 小数点が入力されている場合
                    if (dstart <= i && i < dend) {
                        // 小数点を含む部分を変更
                        if (!source.equals(".")) {
                            // 小数点を削除する場合
                            if (dest.length() - (dend - dstart) + (end - start) <= UPPER_POINT_DIGITS_NUM) {
                                return null;
                            } else {
                                return ".";
                            }
                        } else {
                            // 小数点を小数点に置き換える場合
                            return null;
                        }
                    }
                    // 小数点を含まない部分を変更
                    if (dstart <= i) {
                        // 小数点以上を変更する場合
                        if (i - (dend - dstart) + (end - start) <= UPPER_POINT_DIGITS_NUM) {
                            return null;
                        } else {
                            return "";
                        }
                    } else {
                        // 小数点以下を変更する場合
                        if (dest.length() - (dend - dstart) + (end - start) - i - 1 <= LOWER_POINT_DIGITS_NUM) {
                            return null;
                        } else {
                            return "";
                        }
                    }
                } else {
                    // 小数点が入力されていない場合
                    if (source.equals(".")) {
                        // 小数点を入力する場合
                        // 例）upper = 4, lower = 2 の時に 1111 → 1.111 や 111 → .111 を許可しない
                        if (dest.length() - dend <= LOWER_POINT_DIGITS_NUM) {
                            return null;
                        } else {
                            return "";
                        }
                    } else {
                        // 数字を入力する場合
                        if (dest.length() - (dend - dstart) + (end - start) <= UPPER_POINT_DIGITS_NUM) {
                            return null;
                        } else {
                            return "";
                        }
                    }
                }
            }
        };
        // フィルターの配列を作成
        InputFilter[] filters = new InputFilter[]{inputFilter};
        // フィルターの配列をセット
        editTime.setFilters(filters);

        editTime.setSelection(editTime.getText().length());

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editTime.getText().toString().length() != 0) {

                            switch (dataModel.getOrderId()) {
                                case 1:
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 2:
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 3:
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                                case 4:
                                    dataModel.setSeekBarRate(seekRate);
                                    break;

                            }
                            dataModel.setTime((int) ((Float.parseFloat(editTime.getText().toString())) * 10));

                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.updateItemParam(listItemPosition, dataModel);
                        }
                    }
                })
                .setNegativeButton("キャンセル", null);

        return builder.create();

    }

}
