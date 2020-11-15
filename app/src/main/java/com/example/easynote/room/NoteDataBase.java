package com.example.easynote.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.easynote.model.ModelNote;

@Database(entities = ModelNote.class,version = 1)
public abstract class NoteDataBase extends RoomDatabase {
    private static NoteDataBase dbinstance;
    public abstract NoteDao getNoteDao();

    public static synchronized NoteDataBase getDBinstance(Context context){
        if(dbinstance==null){
            dbinstance= Room.databaseBuilder(context.getApplicationContext(),
            NoteDataBase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbinstance;
    }


}
