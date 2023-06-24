package com.codezilla.arcitectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDes;
    private NumberPicker nump;
    public static final String EXTRA_ID = "com.codezilla.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.codezilla.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DES = "com.codezilla.architectureexample.EXTRA_DES";

    public static final String EXTRA_PRIOR = "com.codezilla.architectureexample.EXTRA_PRIOR";
    public static final int RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTitle = findViewById(R.id.edit_text_title);
        editDes = findViewById(R.id.exit_text_desc);
        nump = findViewById(R.id.number_picker_priority);

        nump.setMinValue(1);
        nump.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editDes.setText(intent.getStringExtra(EXTRA_DES));
            nump.setValue(intent.getIntExtra(EXTRA_PRIOR, 1));
        } else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void saveNote() {
        String title = editTitle.getText().toString();
        String desc = editDes.getText().toString();
        int priority = nump.getValue();

        if (title.trim().isEmpty() || desc.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DES, desc);
        data.putExtra(EXTRA_PRIOR, priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if(id!=-1)
        {
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}