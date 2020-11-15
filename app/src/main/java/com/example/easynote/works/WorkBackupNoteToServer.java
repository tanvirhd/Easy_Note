package com.example.easynote.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easynote.model.ModelNote;
import com.example.easynote.repository.RepositoryNote;
import com.example.easynote.retrofit.APIinterface;
import com.example.easynote.retrofit.ApiClient;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WorkBackupNoteToServer extends Worker {
    private static final String TAG = "WorkBackupNoteToServer";
    private APIinterface apirequest;


    public WorkBackupNoteToServer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        apirequest= ApiClient.getApiInterface();
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data=getInputData();
        ModelNote note=new ModelNote(
                 data.getString(RepositoryNote.PRAMETER1),
                 data.getString(RepositoryNote.PRAMETER2),
                 data.getString(RepositoryNote.PRAMETER3),
                 data.getString(RepositoryNote.PRAMETER4),
                 data.getString(RepositoryNote.PRAMETER5),
                 data.getBoolean(RepositoryNote.PRAMETER6,false),
                 data.getBoolean(RepositoryNote.PRAMETER7,false),
                true);

        apirequest.addNote(note)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                 .subscribe(new Consumer<String>() {
                     @Override
                     public void accept(String s) throws Exception {
                        if(s.equals("200")){
                            //call to update note status
                        }else if(s.equals("000")){
                            //note update faild.
                        }
                     }
                 }, new Consumer<Throwable>() {
                     @Override
                     public void accept(Throwable throwable) throws Exception {
                         Log.d(TAG, "error: "+throwable.getMessage());
                     }
                 });

        return null;
    }
}
