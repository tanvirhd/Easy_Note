package com.example.easynote.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.easynote.model.ModelNote;
import com.example.easynote.retrofit.APIinterface;
import com.example.easynote.retrofit.ApiClient;
import com.example.easynote.works.WorkAddNote;
import com.example.easynote.works.WorkBackupNoteToServer;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RepositoryNote {
    private static final String TAG = "RepositoryNote";
    private APIinterface apirequest;
    private Context context;

    public static final String PRAMETER1="keynote01";
    public static final String PRAMETER2="keynote02";
    public static final String PRAMETER3="keynote03";
    public static final String PRAMETER4="keynote04";
    public static final String PRAMETER5="keynote05";
    public static final String PRAMETER6="keynote06";
    public static final String PRAMETER7="keynote07";
    public static final String PRAMETER8="keynote08";

    public RepositoryNote(Context context) {
        apirequest= ApiClient.getApiInterface();
        this.context=context;
    }

    //old style
    /*public LiveData<String> addNote(ModelNote note){
        MutableLiveData<String> response=new MutableLiveData<>();
        apirequest.addNote(note).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        response.postValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error: "+ throwable.getMessage());
                    }
                });
        return response;
    }*/

    //new Style
    public void backupNote(ModelNote note){
        Data dataNote=new Data.Builder()
                .putString(PRAMETER1,note.getNoteid())
                .putString(PRAMETER2,note.getTitle())
                .putString(PRAMETER3,note.getBody())
                .putString(PRAMETER4,note.getLastmodified_date())
                .putString(PRAMETER5,note.getLastmodified_time())
                .putBoolean(PRAMETER6,note.isState())
                .putBoolean(PRAMETER7,note.isImportant())
                .putBoolean(PRAMETER8,note.isBackup_status())
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build();
        PeriodicWorkRequest workRequest=new PeriodicWorkRequest
                .Builder(WorkBackupNoteToServer.class,1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(dataNote)
                .addTag(note.getNoteid())
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
