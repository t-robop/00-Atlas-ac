package com.robop.scriptrobotcontroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yuusuke on 2018/05/09.
 */

public class DataLoadDialogFragment extends DialogFragment {

    Realm realm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("まえのデータがのこっています")
                .setMessage("もどしますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        setRecycleView();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //realm.close();
    }

    RealmResults<ItemDataModel> realmRead() {
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        RealmQuery<ItemDataModel> query = realm.where(ItemDataModel.class);
        RealmResults<ItemDataModel> items = query.findAll();
        return items;



    }
    void setRecycleView(){
        MainActivity mainActivity = (MainActivity) getActivity();
        for (ItemDataModel model : realmRead()) {
            if (model.getOrderId() < 5){
                ItemDataModel basicBlockModel = new ItemDataModel(model.getOrderId(), model.getRightRerativeSpeed(), model.getLeftRerativeSpeed(), model.getTime(), model.getBlockState(), model.getSeekBarRate());
                mainActivity.setAdapter(basicBlockModel);
            }else{
                ItemDataModel loopBlockModel = new ItemDataModel(model.getOrderId(), model.getBlockState(), model.getLoopCount());
                mainActivity.setAdapter(loopBlockModel);
            }
        }
    }
}
