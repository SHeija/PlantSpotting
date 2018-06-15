package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class AddNewEntry extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving the entry
                EditText addNameEditText = findViewById(R.id.addNameEditText);
                EditText addLatNameEditText = findViewById(R.id.addLatNameEditText);
                EditText addDate = findViewById(R.id.addDateEditText); // jokin muu kuin text!
                EditText addLocation = findViewById(R.id.addLocationEditText); // jokin muu kuin text!
                EditText addNote = findViewById(R.id.addNoteEditText);

                Entry entry = new Entry();
                entry.setName(addNameEditText.getText().toString());
                entry.setLatinName(addLatNameEditText.getText().toString());
                entry.setDate(addDate.getText().toString());
                entry.setLocation(addLocation.getText().toString());
                entry.setNote(addNote.getText().toString());

                //db connection
                db = getDb();
                db.entryDao().insert(entry);

                //moving on
                Intent moveToStart = new Intent(AddNewEntry.this, MainActivity.class);

            }
        });


    }

    public AppDatabase getDb(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();
        return db;
    }
}
