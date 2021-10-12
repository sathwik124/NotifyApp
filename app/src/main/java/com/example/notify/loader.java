package com.example.notify;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loader {

    private Activity myActivity;
    private AlertDialog alertDialog;


    loader(Activity myActivity){
        this.myActivity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);

        LayoutInflater inflater = myActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.load_dialog,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
