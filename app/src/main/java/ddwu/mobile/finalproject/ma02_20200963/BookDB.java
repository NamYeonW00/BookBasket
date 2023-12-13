package ddwu.mobile.finalproject.ma02_20200963;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;

@Database(entities = {NaverBook.class}, version=1)
public abstract class BookDB extends RoomDatabase {
    public abstract BookDao bookDao();

    private static volatile BookDB INSTANCE;

    static BookDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BookDB.class, "book_db.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}