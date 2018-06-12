package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;
import android.content.Context;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //test garbage
        Context context = getApplicationContext();
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();

        db.entryDao().insert(testData());
        int maara = db.entryDao().countEntries();

        TextView textView = findViewById(R.id.textView);
        textView.setText(Integer.toString(maara));

        */

    }

    private static Entry testData(){
        Entry testentry = new Entry();
        testentry.setName("jyrki Kasvi");
        return testentry;
    }

}
