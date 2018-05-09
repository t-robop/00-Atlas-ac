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
                .setTitle("前回のデータが残っています")
                .setMessage("復元しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        setRecycleView();
                    }
                })
                .setNegativeButton("Cancel", null)
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
            ItemDataModel model2 = new ItemDataModel(model.getOrderId(),model.getRightSpeed(),model.getLeftSpeed(),model.getTime(),0,1);
            mainActivity.setAdapter(model2);
        }
    }
}
