package fi.haaga_helia.sheija.plantspotting.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import fi.haaga_helia.sheija.plantspotting.db.models.Entry;
import fi.haaga_helia.sheija.plantspotting.db.models.EntryDAO;
/*

Adapted from room persistence library guide

 */
@Database(entities = {Entry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract EntryDAO entryDao();

    //"singleton"-thingy
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}