package com.example.easynote.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.easynote.R;
import com.example.easynote.adapter.AdapterNote;
import com.example.easynote.databinding.ActivityNoteBinding;
import com.example.easynote.interfaces.AdapterNoteCallback;
import com.example.easynote.model.ModelNote;
import com.example.easynote.utility;
import com.example.easynote.viewmodel.ViewModelNote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ActivityNote extends AppCompatActivity implements AdapterNoteCallback {
    private static final String TAG = "ActivityNote";
    private ActivityNoteBinding binding;
    private AdapterNote adapterNote,adapterNote_important;
    private List<ModelNote> notes;
    private List<ModelNote> importantnotes;
    private ViewModelNote viewModelNote;
    boolean isImportant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();

        notes=new ArrayList<>();importantnotes=new ArrayList<>();
        adapterNote=new AdapterNote(this,notes,this);
        adapterNote_important=new AdapterNote(this,importantnotes,this);
        binding.recycNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recycNotes.setAdapter(adapterNote);

        // all button action defined here
        binding.leftbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppearance("left");
            }
        });

        binding.rightbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeAppearance("right");
            }
        });

        binding.newnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityNote.this,ActivityAddNote.class));
            }
        });
    }//end of onCreate

    void getNotes(){
        viewModelNote.getAllNotes().observe(this, new Observer<List<ModelNote>>() {
            @Override
            public void onChanged(List<ModelNote> modelNotes) {
                notes.clear();importantnotes.clear();
                Collections.reverse(modelNotes);
                notes.addAll(modelNotes);
                adapterNote.notifyDataSetChanged();
                binding.tvLeftBottom.setText(notes.size()+" notes");

                for(ModelNote note:notes){
                    if(note.isImportant())
                        importantnotes.add(note);
                }
                adapterNote_important.notifyDataSetChanged();
                binding.tvRightBottom.setText(importantnotes.size()+" notes");
            }
        });
    }

    @Override
    public void onNoteClickCallBack(int position) {
        if(isImportant){
            startActivity(new Intent(ActivityNote.this,ActivityUpdateNote.class)
                    .putExtra(getString(R.string.parcel),importantnotes.get(position)));
        }else {
            startActivity(new Intent(ActivityNote.this,ActivityUpdateNote.class)
                    .putExtra(getString(R.string.parcel),notes.get(position)));
        }

    }

    @Override
    public void onSyncClickCallBAck(String noteid) {
        Log.d(TAG, "onSyncClickCallBAck: noteid="+noteid);
        Toast.makeText(this, "Syncing", Toast.LENGTH_SHORT).show();
    }

    void changeAppearance(String side){
        if(side.equals("left")){
            binding.recycNotes.setAdapter(adapterNote);isImportant=false;
            binding.leftbox.setBackground(getResources().getDrawable(R.drawable.box_background_solid,null));
            binding.tvLeftTop.setTextColor(getResources().getColor(R.color.white,null));
            binding.tvLeftBottom.setTextColor(getResources().getColor(R.color.white,null));

            binding.rightbox.setBackground(getResources().getDrawable(R.drawable.box_background,null));
            binding.tvRightTop.setTextColor(getResources().getColor(R.color.lite_black,null));
            binding.tvRightBottom.setTextColor(getResources().getColor(R.color.lite_black,null));

            binding.tvWhichbox.setText("All notes");
        }else if(side.equals("right")){
            binding.recycNotes.setAdapter(adapterNote_important);isImportant=true;
            binding.leftbox.setBackground(getResources().getDrawable(R.drawable.box_background,null));
            binding.tvLeftTop.setTextColor(getResources().getColor(R.color.lite_black,null));
            binding.tvLeftBottom.setTextColor(getResources().getColor(R.color.lite_black,null));

            binding.rightbox.setBackground(getResources().getDrawable(R.drawable.box_background_solid,null));
            binding.tvRightTop.setTextColor(getResources().getColor(R.color.white,null));
            binding.tvRightBottom.setTextColor(getResources().getColor(R.color.white,null));

            binding.tvWhichbox.setText("Important notes");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
    }

    void initViewModel(){
        viewModelNote = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelNote.class);
    }

    class BackupNoteThread implements Runnable{
        List<ModelNote> notes;
        ViewModelNote viewModelNote=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelNote.class);
        BackupNoteThread (List<ModelNote> notes){
            this.notes=notes;
        }
        @Override
        public void run() {
            filterNotes();
            for(int position=0;position<notes.size();position++) {
                WorkManager.getInstance(getApplicationContext())
                        .getWorkInfosByTag(notes.get(position).getNoteid()).cancel(true);
                viewModelNote.backupNoteToServer(notes.get(position));
            }
        }

        void filterNotes(){
            for(int position=0;position<notes.size();position++) {
                if(notes.get(position).isState())
                    notes.remove(position);
            }
        }
    }
}


/*void dummyNotes(){
        notes.add(new ModelNote("Bill","Pay current bill by 21st oct"));
        notes.add(new ModelNote("Medicine","Seclo 1 Pata, Tafnil 2piece"));
        notes.add(new ModelNote("Link","facebook.com/groups/bogurafoodlovers"));
        notes.add(new ModelNote("Delivery Charge","1-11-2020=> 415 - 400\n" + "100 disi manager er flexi load"));
        notes.add(new ModelNote("EBL","Cash incharge bogura \n" + "Motin vai \n" + "\n" + "Head Branch  Dhaka\n" + "Hafij vai"));
        adapterNote.notifyDataSetChanged();
    }*/