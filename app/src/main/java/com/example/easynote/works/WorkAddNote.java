package com.example.easynote.works;


import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easynote.model.ModelNote;
import com.example.easynote.room.NoteDao;
import com.example.easynote.room.NoteDataBase;
import com.example.easynote.viewmodel.ViewModelNote;

public class WorkAddNote extends Worker {
    private static final String TAG = "WorkAddNote";
    private NoteDao noteDao;

    public WorkAddNote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        NoteDataBase dataBase=NoteDataBase.getDBinstance(context);
        noteDao=dataBase.getNoteDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data=getInputData();
        ModelNote note=new ModelNote(
                data.getString(ViewModelNote.PRAM1),
                data.getString(ViewModelNote.PRAM2),
                data.getString(ViewModelNote.PRAM3),
                data.getString(ViewModelNote.PRAM4),
                data.getString(ViewModelNote.PRAM5),
                data.getBoolean(ViewModelNote.PRAM6,false),
                data.getBoolean(ViewModelNote.PRAM7,false),
                data.getBoolean(ViewModelNote.PRAM8,false) );

        try{
            noteDao.addNote(note);
            return Result.success();
        }catch (Exception e){
            Log.d(TAG, "doWork: error"+e.getMessage());
            Data error=new Data.Builder()
                    .putString("SQL_exception",e.getMessage())
                    .build();
            return Result.failure(error);
        }

    }
}
