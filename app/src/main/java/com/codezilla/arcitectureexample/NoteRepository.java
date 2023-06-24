package com.codezilla.arcitectureexample;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.w3c.dom.Node;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    public NoteRepository(Application application)
    {
        Log.d("debug", "RRRRepository");
        NoteDatabase database= NoteDatabase.getInstance(application);
        noteDao = database.noteDao();  // Room creates the body for noteDao()
        allNotes= noteDao.getAllNotes();
    }

    public void insert(Note note){
         new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note)
    {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note)
    {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes()
    {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    public  LiveData<List<Note>> getAllNotes() {
        Log.d("debug", "getAllNotes");
        try {
            Log.d("debug", "getAllNotes  "+ Thread.currentThread().getId());
            Thread.sleep(5000);
            Log.d("debug", "getAllNotes");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allNotes;
    }

    public static  class InsertNoteAsyncTask extends AsyncTask <Note,Void,Void>
    {
        private NoteDao noteDao;
        private InsertNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    public static  class UpdateNoteAsyncTask extends AsyncTask <Note,Void,Void>
    {
        private NoteDao noteDao;
        private UpdateNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    public static  class DeleteNoteAsyncTask extends AsyncTask <Note,Void,Void>
    {
        private NoteDao noteDao;
        private DeleteNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    public static  class DeleteAllNoteAsyncTask extends AsyncTask <Note,Void,Void>
    {
        private NoteDao noteDao;
        private DeleteAllNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
