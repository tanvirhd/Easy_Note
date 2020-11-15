package com.example.easynote.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.easynote.R;
import com.example.easynote.databinding.ActivityUpdateNoteBinding;
import com.example.easynote.model.ModelNote;
import com.example.easynote.viewmodel.ViewModelNote;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class ActivityUpdateNote extends AppCompatActivity {
    private static final String TAG = "ActivityUpdateNote";
    private ActivityUpdateNoteBinding binding;
    private Observable<String> observableTitle,observableBody;
    private boolean isTitleUpdated,isBodyUpdated,isDeleted;
    private ModelNote note;
    private ViewModelNote viewModelNote;
    private Dialog dialogDeleteNote;
    private Menu menu;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        note=getIntent().getParcelableExtra(getResources().getString(R.string.parcel));
        if(note!=null){
            binding.etNotetitle.setText(note.getTitle());
            binding.etNotebody.setText(note.getBody());

            if(note.getLastmodified_date().equals(getCurrentDate())){
                getSupportActionBar().setTitle("Edited Today, "+note.getLastmodified_time());
            }else {
                getSupportActionBar().setTitle("Edited "+dateFormatChanger(note.getLastmodified_date()));
            }

        }

        observableTitle= RxTextView.textChanges(binding.etNotetitle).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(@NonNull CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        observableBody= RxTextView.textChanges(binding.etNotebody).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(@NonNull CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        observableTitle.subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                ditectTitleChange(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        observableBody.subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                ditectBodyChange(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        dialogDeleteNote=initDeleteDialog(ActivityUpdateNote.this);
        dialogDeleteNote.findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteNote.dismiss();
            }
        });

        dialogDeleteNote.findViewById(R.id.btn_delete_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelNote.deleteNotebyid(note.getNoteid());
                isDeleted=true;
                onBackPressed();
            }
        });

    }//end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.updatenote_menu,menu);
        this.menu=menu;
        if (note.isImportant()) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_important_marked, null));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_important_unmarked, null));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                dialogDeleteNote.show();
                return true;
            case R.id.important:
                if (!note.isImportant()) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_important_marked, null));
                } else {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_important_unmarked, null));
                }
                viewModelNote.markAsImportant(note.getNoteid(),!note.isImportant());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ditectTitleChange(String s){
        if(!s.equals(note.getTitle()) ){
            isTitleUpdated= true;
        }else {
            isTitleUpdated=false;
        }
    }

    private void ditectBodyChange(String s){
        if(!s.equals(note.getBody()) ){
            isBodyUpdated= true;
        }else {
            isBodyUpdated=false;
        }
    }

    private void initViewModel(){
        viewModelNote = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelNote.class);
    }

    private void updateNote(){
        ModelNote newnote=new ModelNote(note.getNoteid(),binding.etNotetitle.getText().toString(),
                binding.etNotebody.getText().toString(),getCurrentDate(),getCurrentTime(),
                false,false);
        viewModelNote.updateNoteById(newnote);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(calendar.getTime());
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(calendar.getTime());
    }

    private String dateFormatChanger(String date){
         String[] splitedDate=date.split("-",3);
         switch (splitedDate[1]){
             case "01":
                 splitedDate[1]="Jan";
                 break;
             case "02":
                 splitedDate[1]="Feb";
                 break;
             case "03":
                 splitedDate[1]="Mar";
                 break;
             case "04":
                 splitedDate[1]="Apr";
                 break;
             case "05":
                 splitedDate[1]="May";
                 break;
             case "06":
                 splitedDate[1]="Jun";
                 break;
             case "07":
                 splitedDate[1]="Jul";
                 break;
             case "08":
                 splitedDate[1]="Aug";
                 break;
             case "09":
                 splitedDate[1]="Sep";
                 break;
             case "10":
                 splitedDate[1]="Oct";
                 break;
             case "11":
                 splitedDate[1]="Nov";
                 break;
             case "12":
                 splitedDate[1]="Dec";
                 break;
         }
         return splitedDate[1]+"'"+splitedDate[0];
    }
  

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isDeleted){
            if(isBodyUpdated||isTitleUpdated){
                updateNote();
            }else {
                Toast.makeText(ActivityUpdateNote.this, "No Change Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Dialog initDeleteDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_delete_note );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}