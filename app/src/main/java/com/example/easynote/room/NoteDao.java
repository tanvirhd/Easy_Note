package com.example.easynote.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easynote.model.ModelNote;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void addNote(ModelNote note);

    @Query("update note_table set title=:title, body=:body, lastmodified_date=:lastmodified_date " +
            ", lastmodified_time=:lastmodified_time, backup_status=:backup_status where noteid=:noteid ")
    void updatebyid(String noteid, String title,String body,String lastmodified_date,String lastmodified_time,boolean backup_status);

    @Query("delete from note_table where noteid=:noteid")
    void deleteNoteById(String noteid);

    @Query("update note_table set important=:isImportant where noteid=:noteid")
    void markAsImportant(String noteid,boolean isImportant);

    @Query("select * from note_table")
    LiveData<List<ModelNote>> getAllNotes();

    @Query("select * from note_table where important= :importance")
    LiveData<List<ModelNote>> getAllNotes(boolean importance);

}
