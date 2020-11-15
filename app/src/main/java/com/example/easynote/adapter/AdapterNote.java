package com.example.easynote.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easynote.interfaces.AdapterNoteCallback;
import com.example.easynote.R;
import com.example.easynote.model.ModelNote;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolderAdapterNote>{
    private static final String TAG = "AdapterNote";
    Context context;
    List<ModelNote> allnotes;
    AdapterNoteCallback callback;

    public AdapterNote(Context context, List<ModelNote> allnotes, AdapterNoteCallback callback) {
        this.context = context;
        this.allnotes = allnotes;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolderAdapterNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_note,parent,false);
        ViewHolderAdapterNote vh=new ViewHolderAdapterNote(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapterNote holder, int position) {
         /*if(allnotes.get(position).getTitle().equals("")){
             holder.title.setVisibility(View.GONE);
             Log.d(TAG, "onBindViewHolder: title blank "+position);
         }else {
             holder.title.setText(allnotes.get(position).getTitle());
         }

        if(allnotes.get(position).getBody().equals("")){
            holder.body.setVisibility(View.GONE);
            Log.d(TAG, "onBindViewHolder: body blank "+position);
        }else {
            holder.body.setText(allnotes.get(position).getBody());
        }*/

        holder.title.setText(allnotes.get(position).getTitle());
        holder.body.setText(allnotes.get(position).getBody());

         holder.noteContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 callback.onNoteClickCallBack(position);
             }
         });

         holder.sync.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 callback.onSyncClickCallBAck(allnotes.get(position).getNoteid());
             }
         });

    }

    @Override
    public int getItemCount() {
        return allnotes.size();
    }

    class ViewHolderAdapterNote extends RecyclerView.ViewHolder{
        ImageView sync;
        TextView title,body;
        ConstraintLayout noteContainer;
        public ViewHolderAdapterNote(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tv_title);
            body=itemView.findViewById(R.id.tv_body);;
            noteContainer=itemView.findViewById(R.id.note_container);
            sync=itemView.findViewById(R.id.sync);
        }
    }
}
