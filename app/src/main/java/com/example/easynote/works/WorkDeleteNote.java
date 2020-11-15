package com.example.easynote.works;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.easynote.room.NoteDao;
import com.example.easynote.room.NoteDataBase;
import com.example.easynote.viewmodel.ViewModelNote;

public class WorkDeleteNote extends Worker {
    private NoteDao noteDao;
    public WorkDeleteNote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        NoteDataBase dataBase = NoteDataBase.getDBinstance(context);
        noteDao = dataBase.getNoteDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String noteid = data.getString(ViewModelNote.PRAM1);
        try{
            noteDao.deleteNoteById(noteid);
            return Result.success();
        }catch (Exception e){
            Data error=new Data.Builder().putString("error",e.getMessage()).build();
            return Result.failure(error);
        }
    }
}
