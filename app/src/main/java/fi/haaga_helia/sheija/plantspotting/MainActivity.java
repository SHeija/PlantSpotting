package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        refreshList(db);

        //activating the recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(entryList, getApplicationContext());
        //and making it behave
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(db);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cleanRedundantImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuAddEntry:
                Intent moveToAdd = new Intent(this, AddNewEntry.class);
                startActivity(moveToAdd);
                return true;

            case R.id.menuRemoveAll:
                db.entryDao().exterminatus();
                refreshList(db);
                adapter.notifyDataSetChanged();
                recreate();
                return true;

            case R.id.menuCredits:
                Intent moveToCredits = new Intent (this, Credits.class);
                startActivity(moveToCredits);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList(AppDatabase db){
        entryList = db.entryDao().getAll();
    }

    private void cleanRedundantImages(){
        //Kills redundant images created by bad code
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        List<String> paths = new ArrayList<>();
        refreshList(db);
        for (Entry entry : entryList){
            paths.add(entry.getImagePath());
        }

        for (File file : files){
            if(!paths.contains(file.getAbsolutePath())){
                Log.d("Files","Deleted redundant "+file.getAbsolutePath());
                file.delete();
            }
        }
        Log.d("Files", "Size: "+ files.length);

    }

    public AppDatabase getDb(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();
        return db;
    }

    /*
    //Testdata for debugging
    private Entry testData(int i){
        Entry testentry = new Entry();
        testentry.setName("Name #"+i);
        testentry.setLatinName("LatName #"+i);
        Date currentTime = Calendar.getInstance().getTime();
        testentry.setDate(currentTime.toString());
        testentry.setLocation("(50.00, 50.50");
        testentry.setNote("Note #"+i);
        return testentry;
    }

    private void addTestData(AppDatabase db, int howMany){
            for (int i = 0; i<howMany; i++){
                db.entryDao().insert(testData(i));
            }
            showToast("test data added");
            recreate();
    }

    */
}
