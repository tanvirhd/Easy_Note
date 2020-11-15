package com.example.easynote.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.easynote.model.ModelNote;
import com.example.easynote.repository.RepositoryNote;
import com.example.easynote.repository.RepositoryNoteRoom;
import com.example.easynote.works.WorkAddNote;
import com.example.easynote.works.WorkDeleteNote;
import com.example.easynote.works.WorkMaskAsImportant;
import com.example.easynote.works.WorkUpdateNote;


import java.util.List;

public class ViewModelNote extends AndroidViewModel {
    private static final String TAG = "ViewModelNote";
    private Context context;
    private RepositoryNote repositoryNote;
    private RepositoryNoteRoom repositoryNoteRoom;

    public static final String PRAM1="keynote1";
    public static final String PRAM2="keynote2";
    public static final String PRAM3="keynote3";
    public static final String PRAM4="keynote4";
    public static final String PRAM5="keynote5";
    public static final String PRAM6="keynote6";
    public static final String PRAM7="keynote7";
    public static final String PRAM8="keynote8";


    public ViewModelNote(@NonNull Application application) {
        super(application);
        context=application.getApplicationContext();
        repositoryNoteRoom =new RepositoryNoteRoom(application);
        repositoryNote=new RepositoryNote(application.getApplicationContext());
    }

    public LiveData<List<ModelNote>> getAllNotes(){
        return repositoryNoteRoom.getAllnotes();
    }

    public OneTimeWorkRequest addNewNote(ModelNote note ){
        Data dataNote=new Data.Builder()
                .putString(PRAM1,note.getNoteid())
                .putString(PRAM2,note.getTitle())
                .putString(PRAM3,note.getBody())
                .putString(PRAM4,note.getLastmodified_date())
                .putString(PRAM5,note.getLastmodified_time())
                .putBoolean(PRAM6,note.isState())
                .putBoolean(PRAM7,note.isImportant())
                .putBoolean(PRAM8,note.isBackup_status())
                .build();
        OneTimeWorkRequest workRequest=new OneTimeWorkRequest.Builder(WorkAddNote.class)
                .setInputData(dataNote)
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
        return workRequest;
    }

    public void updateNoteById(ModelNote note){
        Data data=new Data.Builder()
                .putString(PRAM1,note.getNoteid())
                .putString(PRAM2,note.getTitle())
                .putString(PRAM3,note.getBody())
                .putString(PRAM4,note.getLastmodified_date())
                .putString(PRAM5,note.getLastmodified_time())
                .build();
        OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(WorkUpdateNote.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(request);
    }

    public void deleteNotebyid(String noteid){
        Data data=new Data.Builder()
                .putString(PRAM1,noteid).build();
        OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(WorkDeleteNote.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(request);
    }

    public void markAsImportant(String noteid,boolean isImportant){
        Data data=new Data.Builder()
                .putString(PRAM1,noteid)
                .putBoolean(PRAM2,isImportant)
                .build();
        OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(WorkMaskAsImportant.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(request);
    }

    public void backupNoteToServer(ModelNote note){
        repositoryNote.backupNote(note);
    }
}
