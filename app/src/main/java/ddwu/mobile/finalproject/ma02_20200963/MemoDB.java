package ddwu.mobile.finalproject.ma02_20200963;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version=1)
public abstract class MemoDB extends RoomDatabase {
    public abstract MemoDao memoDao();

    private static volatile MemoDB INSTANCE;

    static MemoDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MemoDB.class, "memo_db.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
