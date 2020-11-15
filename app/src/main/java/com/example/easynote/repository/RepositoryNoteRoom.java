package com.example.easynote.repository;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.easynote.model.ModelNote;
import com.example.easynote.room.NoteDao;
import com.example.easynote.room.NoteDataBase;

import java.util.List;

public class RepositoryNoteRoom {
    private static final String TAG = "RepositoryNote";
    private NoteDao noteDao;
    private Application application;


    public RepositoryNoteRoom(Application application) {
        this.application=application;
        NoteDataBase dataBase=NoteDataBase.getDBinstance(application);
        noteDao=dataBase.getNoteDao();
    }

    public LiveData<List<ModelNote>> getAllnotes(){
       return noteDao.getAllNotes();
    }

    public void addnote(ModelNote note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<ModelNote ,Void ,Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(ModelNote... notes) {
            noteDao.addNote(notes[0]);
            return null;
        }
    }
}








