package ddwu.mobile.finalproject.ma02_20200963;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BookListActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    final int INFO_CODE = 200;

    BookAdapter adapter;

    ListView lv_list;
    BookDB bookDB;
    BookDao bookDao;
    List<NaverBook> bookList;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        bookList = new ArrayList<NaverBook>();

        lv_list = (ListView)findViewById(R.id.lv_list);
        adapter = new BookAdapter(this, R.layout.listview_book, bookList);
        lv_list.setAdapter(adapter);

        bookDB = BookDB.getDatabase(this);
        bookDao = bookDB.bookDao();

        Flowable<List<NaverBook>> resultBooks = bookDao.getAllBooks();

        mDisposable.add( resultBooks
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                            for (NaverBook aBook : books) {
                                Log.d(TAG, aBook.toString());
                            }
                            bookList.clear();
                            bookList.addAll(books);
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> Log.d(TAG, "error", throwable)) );

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NaverBook bookData = (NaverBook) lv_list.getAdapter().getItem(position);
                Intent intent = new Intent(BookListActivity.this, MyBookInfoActivity.class);
                intent.putExtra("bookData", bookData);
                startActivityForResult(intent, INFO_CODE);
            }
        });

        lv_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                NaverBook deleteBook = (NaverBook) lv_list.getAdapter().getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);
                builder.setTitle("책 삭제")
                        .setMessage(deleteBook.getTitle() + "을(를) 책바구니에서 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Completable deleteResult = bookDao.deleteBook(deleteBook);
                                mDisposable.add(deleteResult
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> Toast.makeText(BookListActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show(),
                                                throwable -> Toast.makeText(BookListActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show()) );
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

       Flowable<List<NaverBook>> resultBooks = bookDao.getAllBooks();

        mDisposable.add( resultBooks
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                            for (NaverBook aBook : books) {
                                Log.d(TAG, aBook.toString());
                            }
                            bookList.clear();
                            bookList.addAll(books);
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> Log.d(TAG, "error", throwable)) );
    }

    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.myBookList:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.bookSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.myBookList:
                intent = new Intent(this, BookListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.search:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.close:
                AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);
                builder.setTitle("앱 종료")
                        .setMessage("앱을 종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        return true;
    }
}
