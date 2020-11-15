package com.example.easynote.retrofit;

import com.example.easynote.model.ModelNote;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface APIinterface {
    @POST("addnote.php/")
    Observable<String> addNote(ModelNote note);
}
