package fi.haaga_helia.sheija.plantspotting.db.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EntryDAO {

    @Query("SELECT * FROM entry")
    List<Entry> getAll();

    @Query("SELECT * FROM entry WHERE id = :entryId")
    Entry findById(int entryId);

    @Query("SELECT COUNT(*) FROM entry")
    int countEntries();

    @Query("DELETE FROM entry")
    void exterminatus();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Entry entry);

    @Delete
    void delete(Entry entry);

}
