package com.example.easynote.activities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DoSum extends Worker {
    private static final String TAG = "DoSum";
    public DoSum(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data=getInputData();
        int x=data.getInt("num1",0);
        int y=data.getInt("num2",0);
        String sms=data.getString("sms");
        Log.d(TAG, "doWork: sms is="+sms);
         Data data_res=new Data.Builder()
                 .putInt("result",x+y)
                 .build();

        return Result.success(data_res);
    }
}
