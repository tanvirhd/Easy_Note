package com.example.easynote.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "note_table")
public class ModelNote implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("noteid")
    @Expose
    private String noteid;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("lastmodified_date")
    @Expose
    private String lastmodified_date;

    @SerializedName("lastmodified_time")
    @Expose
    private String lastmodified_time;

    @SerializedName("important")
    @Expose
    boolean important;

    @SerializedName("backup_status")
    @Expose
    boolean backup_status;

    boolean state;

    public ModelNote(@NonNull String noteid, String title, String body,
                     String lastmodified_date, String lastmodified_time,
                     boolean flod_state, boolean important_note, boolean backup_status) {
        this.noteid = noteid;
        this.title = title;
        this.body = body;
        this.lastmodified_date = lastmodified_date;
        this.lastmodified_time = lastmodified_time;
        this.state = flod_state;
        this.important = important_note;
        this.backup_status = backup_status;
    }

    public ModelNote(@NonNull String noteid, String title, String body,
                     String lastmodified_date, String lastmodified_time,
                     boolean important_note, boolean backup_status) {
        this.noteid = noteid;
        this.title = title;
        this.body = body;
        this.lastmodified_date = lastmodified_date;
        this.lastmodified_time = lastmodified_time;
        this.important = important_note;
        this.backup_status = backup_status;
    }

    public ModelNote() {
    }

    protected ModelNote(Parcel in) {
        noteid = in.readString();
        title = in.readString();
        body = in.readString();
        lastmodified_date = in.readString();
        lastmodified_time = in.readString();
        important = in.readByte() != 0;
        backup_status = in.readByte() != 0;
        state = in.readByte() != 0;
    }

    public static final Creator<ModelNote> CREATOR = new Creator<ModelNote>() {
        @Override
        public ModelNote createFromParcel(Parcel in) {
            return new ModelNote(in);
        }

        @Override
        public ModelNote[] newArray(int size) {
            return new ModelNote[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }

    public String getLastmodified_date() {
        return lastmodified_date;
    }

    public void setLastmodified_date(String lastmodified_date) {
        this.lastmodified_date = lastmodified_date;
    }

    public String getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(String lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isBackup_status() {
        return backup_status;
    }

    public void setBackup_status(boolean backup_status) {
        this.backup_status = backup_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteid);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(lastmodified_date);
        dest.writeString(lastmodified_time);
        dest.writeByte((byte) (important ? 1 : 0));
        dest.writeByte((byte) (backup_status ? 1 : 0));
        dest.writeByte((byte) (state ? 1 : 0));
    }
}
