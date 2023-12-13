package ddwu.mobile.finalproject.ma02_20200963;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BookDao {
    @Query("SELECT * FROM book_table")
    Flowable<List<NaverBook>> getAllBooks();

    @Insert
    Single<Long> insertBook(NaverBook food);

    @Update
    Completable updateBook(NaverBook food);

    @Delete
    Completable deleteBook(NaverBook food);

//    @Query("SELECT * FROM food_table WHERE nation = :nation")
//    Flowable<List<BookDto>> getFoodByNation(String nation);

}
