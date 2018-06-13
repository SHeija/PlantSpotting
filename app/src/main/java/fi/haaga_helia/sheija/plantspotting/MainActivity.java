package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private List<Entry> entryList;
    private RecyclerViewAdapter adapter;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db
        db = getDb();
        //refreshing the entrylist
        refreshList(db);

        //activating the recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(entryList);
        //and making it behave
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //inserting some test data
        addTestData(db, 5);

        //button
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearAllTables();
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = getDb();
        refreshList(db);
        adapter.notifyDataSetChanged();
    }

    private Entry testData(){
        Entry testentry = new Entry();
        testentry.setName(UUID.randomUUID().toString());
        testentry.setLatinName(UUID.randomUUID().toString());
        testentry.setNote(UUID.randomUUID().toString());
        return testentry;
    }

    private void addTestData(AppDatabase db, int howMany){
        for (int i = 0; i<howMany; i++){
            db.entryDao().insert(testData());
        }

        adapter.notifyDataSetChanged();
        showToast("test data added");
    }

    private void refreshList(AppDatabase db){
        entryList = db.entryDao().getAll();
    }

    public AppDatabase getDb(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();
        return db;
    }
    public void showToast(String teksti){
        int aika = Toast.LENGTH_LONG;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, teksti, aika);
        toast.show();
    }
}
