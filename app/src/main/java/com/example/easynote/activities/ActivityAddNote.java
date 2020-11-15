package com.example.easynote.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.easynote.databinding.ActivityAddNoteBinding;
import com.example.easynote.model.ModelNote;
import com.example.easynote.utility;
import com.example.easynote.viewmodel.ViewModelNote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityAddNote extends AppCompatActivity {
    private static final String TAG = "ActivityAddNote";

    private ActivityAddNoteBinding binding;
    private ViewModelNote viewModelNote;
    private String noteid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
        noteid= utility.getRandomString(8);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savenote();
                onBackPressed();
            }
        });
    }
    private void initViewModel(){
        viewModelNote = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelNote.class);
    }

    private void savenote(){
        if(binding.etNotetitle.getText().toString().equals("")&&binding.etNotebody.getText().toString().equals("")){
            Log.d(TAG, "savenote: Empty Note discarded.");
            Toast.makeText(this, "Empty Note discarded.", Toast.LENGTH_SHORT).show();
        }else {

            ModelNote note=new ModelNote(noteid,binding.etNotetitle.getText().toString(),
                    binding.etNotebody.getText().toString(),getCurrentDate(),getCurrentTime(),
                    false,false,false);


            OneTimeWorkRequest request=viewModelNote.addNewNote(note);
            WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(request.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            switch(workInfo.getState().name()){
                                case "FAILED":
                                    Log.d(TAG, "workInfo state FAILED: reason->"+workInfo.getOutputData());
                                    break;
                                default:

                                    Log.d(TAG, "workInfo state: "+workInfo.getState().name());
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        savenote();
        super.onBackPressed();
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
}