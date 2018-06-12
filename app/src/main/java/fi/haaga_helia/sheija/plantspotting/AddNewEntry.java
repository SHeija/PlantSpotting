package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class AddNewEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);



        Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving the entry
                EditText addEditText = findViewById(R.id.addEditText);
                String nimi = addEditText.getText().toString();

                //moving on
                Intent move = new Intent(AddNewEntry.this, MainActivity.class);
                startActivity(move);
            }
        });


    }
}
