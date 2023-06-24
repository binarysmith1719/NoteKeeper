package com.codezilla.arcitectureexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel viewModel;
    public static final int ADD_REQUEST_CODE=1;
    public static final int EDIT_REQUEST_CODE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "MainActivity tid  "+ Thread.currentThread().getId());
        FloatingActionButton buttonAddNote= findViewById(R.id.fl_action);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_REQUEST_CODE);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager llm=new LinearLayoutManager(this);                  // llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        NoteAdapter adapter= new NoteAdapter();
        recyclerView.setAdapter(adapter);
        Log.d("debug", "onCreate: here1 ");

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update recycler view
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int adapter_pos= viewHolder.getAdapterPosition();
                Note note_at_Adapter_pos= adapter.getNoteAt(adapter_pos);
                viewModel.delete(note_at_Adapter_pos);
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new NoteAdapter.OnItemClickListenerx() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this , AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DES,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIOR,note.getPriority());
                startActivityForResult(intent,EDIT_REQUEST_CODE );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delAllNotes: viewModel.deleteAllNodes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_REQUEST_CODE && resultCode==RESULT_OK)
        {
           String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
           String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DES);
           int priority= data.getIntExtra(AddEditNoteActivity.EXTRA_PRIOR,1);

            Note note= new Note(title,description,priority);
            viewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode==EDIT_REQUEST_CODE && resultCode==RESULT_OK)
        {
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DES);
            int priority= data.getIntExtra(AddEditNoteActivity.EXTRA_PRIOR,1);
            int id= data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            if(id==-1)
            {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
            }
            Note note= new Note(title,description,priority);
            note.setId(id);
            viewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode==ADD_REQUEST_CODE)
        {
            Toast.makeText( this, "Not saved", Toast.LENGTH_SHORT).show();
        }

    }
}