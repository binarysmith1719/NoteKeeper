package com.codezilla.arcitectureexample;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository  repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        Log.d("debug", "VVVVView Model");
        repository= new NoteRepository(application);
        allNotes=repository.getAllNotes();
    }

    public void insert(Note note)
    {
        repository.insert(note);
    }
    public void delete(Note note)
    {
        repository.delete(note);
    }
    public void update(Note note)
    {
        repository.update(note);
    }
    public void deleteAllNodes()
    {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
