package fi.haaga_helia.sheija.plantspotting;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class AddNewEntry extends AppCompatActivity {

    private AppDatabase db;
    String imagePath;
    Entry entry;
    Date currentTime;
    File photoFile;
    Location location;
    LocationManager locationManager;
    ImageView previewImageView;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        entry = new Entry();
        currentTime  = Calendar.getInstance().getTime();
        previewImageView = findViewById(R.id.previewImageView);

        //cameraButton
        Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        //location-switch
        final Switch locationSwitch = findViewById(R.id.locationSwitch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    Log.d("click-click", "switch on");
                    //do we have location permission?
                    if (ContextCompat.checkSelfPermission(AddNewEntry.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted, asking for permission
                        ActivityCompat.requestPermissions(AddNewEntry.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                            locationSwitch.setChecked(false);



                    }else{
                        //permissions ok, getting location

                        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                        try{
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }catch (SecurityException e){
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.SECURITYERROR, Toast.LENGTH_LONG);
                            toast.show();

                        }

                    }

                }else{
                    Log.d("click-click", "switch off");
                    location = null;
                }
            }
        });

        //addButton

        Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving the entry
                EditText addNameEditText = findViewById(R.id.addNameEditText);
                EditText addLatNameEditText = findViewById(R.id.addLatNameEditText);
                EditText addNote = findViewById(R.id.addNoteEditText);

                entry.setName(addNameEditText.getText().toString());
                entry.setLatinName(addLatNameEditText.getText().toString());
                entry.setDate(currentTime.toString());

                entry.setNote(addNote.getText().toString());

                if (location!=null){
                    String longLat = "("+Double.toString(location.getLongitude())+", "+Double.toString(location.getLatitude())+")";
                    entry.setLocation(longLat);
                }

                //db connection
                db = getDb();
                db.entryDao().insert(entry);

                //moving on
                Intent moveToStart = new Intent(AddNewEntry.this, MainActivity.class);
                startActivity(moveToStart);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            entry.setImagePath(imagePath);
            File imageFile = new File(entry.getImagePath());
            Glide.with(getApplicationContext()).load(imageFile).into(previewImageView);

        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.IOERROR, Toast.LENGTH_LONG);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fi.haaga_helia.sheija.plantspotting.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = generateFileName();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        return image;
    }

    private String generateFileName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }

    public AppDatabase getDb(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();
        return db;
    }
}
