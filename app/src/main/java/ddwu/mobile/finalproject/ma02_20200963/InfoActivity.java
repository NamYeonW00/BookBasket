package ddwu.mobile.finalproject.ma02_20200963;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class InfoActivity extends AppCompatActivity {
    final static String TAG = "InfoActivity";

    NaverBook bookData;

    ImageView iv;
    TextView tv_title;
    TextView tv_author;
    TextView tv_publisher;
    TextView tv_pubdate;
    TextView tv_price;
    TextView tv_description;

    BookDB bookDB;
    BookDao bookDao;

    MemoDB memoDB;
    MemoDao memoDao;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        bookData = (NaverBook)getIntent().getSerializableExtra("bookData");

        iv = findViewById(R.id.imgView);
        tv_title = findViewById(R.id.tv_title);
        tv_author = findViewById(R.id.tv_author);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_pubdate = findViewById(R.id.tv_pubdate);
        tv_price = findViewById(R.id.tv_price);
        tv_description = findViewById(R.id.tv_description);

        tv_title.setText(bookData.getTitle());
        tv_author.setText(bookData.getAuthor());
        tv_publisher.setText(bookData.getPublisher());
        tv_pubdate.setText(bookData.getPubdate());
        tv_price.setText(bookData.getDiscount());
        tv_description.setText(bookData.getDescription());

        Glide.with(this)
                .load(bookData.getImage())
                .into(iv);

        bookDB = BookDB.getDatabase(this);
        bookDao = bookDB.bookDao();

        memoDB = MemoDB.getDatabase(this);
        memoDao = memoDB.memoDao();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                builder.setTitle("책 추가")
                        .setMessage(bookData.getTitle() + "을(를) 책바구니에 추가하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Single<Long> insertResult = bookDao.insertBook(bookData);
                                mDisposable.add (
                                        insertResult.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(result -> Toast.makeText(InfoActivity.this, "추가 완료", Toast.LENGTH_SHORT).show(),
                                                        throwable -> Toast.makeText(InfoActivity.this, "추가 실패", Toast.LENGTH_SHORT).show())   );

                                Memo memo = new Memo();
                                memo.setBook(bookData.getTitle());
                                Single<Long> iResult = memoDao.insertMemo(memo);
                                mDisposable.add (
                                        iResult.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(result -> Toast.makeText(InfoActivity.this, "추가 완료", Toast.LENGTH_SHORT).show(),
                                                        throwable -> Toast.makeText(InfoActivity.this, "추가 실패", Toast.LENGTH_SHORT).show())   );

                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
            case R.id.btn_close:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
