package com.example.easynote.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easynote.room.NoteDao;
import com.example.easynote.room.NoteDataBase;
import com.example.easynote.viewmodel.ViewModelNote;

public class WorkUpdateNote extends Worker {
    private static final String TAG = "WorkUpdateNote";
    private NoteDao noteDao;

    public WorkUpdateNote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        NoteDataBase dataBase = NoteDataBase.getDBinstance(context);
        noteDao = dataBase.getNoteDao();
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
        String noteid = data.getString(ViewModelNote.PRAM1);
        String title = data.getString(ViewModelNote.PRAM2);
        String body = data.getString(ViewModelNote.PRAM3);
        String updatedate = data.getString(ViewModelNote.PRAM4);
        String updatetime = data.getString(ViewModelNote.PRAM5);

        try {
            noteDao.updatebyid(noteid,title,body,updatedate,updatetime,false);
            return Result.success();
        } catch (Exception e) {
            Log.d(TAG, "doWork: error" + e.getMessage());
            Data error = new Data.Builder()
                    .putString("SQL_exception", e.getMessage())
                    .build();
            return Result.failure(error);
        }
    }
}
