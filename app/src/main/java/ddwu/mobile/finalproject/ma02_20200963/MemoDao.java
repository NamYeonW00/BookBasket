package ddwu.mobile.finalproject.ma02_20200963;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MemoDao {
    @Query("SELECT * FROM memo_table")
    Flowable<List<Memo>> getAllMemos();

    @Insert
    Single<Long> insertMemo(Memo memo);

    @Update
    Completable updateMemo(Memo memo);

    @Delete
    Completable deleteMemo(Memo memo);

    @Query("SELECT * FROM memo_table WHERE book = :book")
    Flowable<List<Memo>> getMemoByBook(String book);
}
