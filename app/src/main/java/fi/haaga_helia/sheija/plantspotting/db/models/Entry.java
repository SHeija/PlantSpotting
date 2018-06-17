package fi.haaga_helia.sheija.plantspotting.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import org.w3c.dom.Text;

import static org.xmlpull.v1.XmlPullParser.TEXT;

@Entity(tableName = "entry")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="latinName")
    private String latinName;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="location")
    private String location;

    @ColumnInfo(name="note")
    private String note;

    @ColumnInfo
    private String imagePath;

    public Entry() {

    }

   public String getName() {
       return name;
   }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
