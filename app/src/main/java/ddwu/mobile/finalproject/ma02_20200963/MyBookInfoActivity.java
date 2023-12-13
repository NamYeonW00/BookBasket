package ddwu.mobile.finalproject.ma02_20200963;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyBookInfoActivity extends AppCompatActivity {
    final static String TAG = "MyBookInfoActivity";
    //static Memo memoData;

    NaverBook bookData;

    ImageView iv;
    TextView tv_title;
    TextView tv_author;
    TextView tv_publisher;
    TextView tv_pubdate;
    TextView tv_price;
    TextView tv_description;
    RadioGroup radioGroup;
    int radio_checkedId;

    MemoDB memoDB;
    MemoDao memoDao;
    Memo memoData;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        bookData = (NaverBook) getIntent().getSerializableExtra("bookData");

        memoDB = MemoDB.getDatabase(this);
        memoDao = memoDB.memoDao();

        iv = findViewById(R.id.imgView2);
        tv_title = findViewById(R.id.tv_title);
        tv_author = findViewById(R.id.tv_author);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_pubdate = findViewById(R.id.tv_pubdate);
        tv_price = findViewById(R.id.tv_price);
        tv_description = findViewById(R.id.tv_description);
        radioGroup = findViewById(R.id.rdGroup);

        tv_title.setText(bookData.getTitle());
        tv_author.setText(bookData.getAuthor());
        tv_author.setText(bookData.getAuthor());
        tv_publisher.setText(bookData.getPublisher());
        tv_pubdate.setText(bookData.getPubdate());
        tv_price.setText(bookData.getDiscount());
        tv_description.setText(bookData.getDescription());

        Glide.with(this)
                .load(bookData.getImage())
                .into(iv);

        Flowable<List<Memo>> resultMemos = memoDao.getMemoByBook(bookData.getTitle());

        mDisposable.add( resultMemos
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(memos -> {
                            for (Memo aMemo : memos) {
                                Log.d(TAG, aMemo.toString());
                            }
                            memoData = memos.get(0);
                            radioGroup.check(memoData.getState());
                        },
                        throwable -> Log.d(TAG, "error", throwable)) );

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_before:
                    case R.id.rb_ing:
                    case R.id.rb_after:
                        radio_checkedId = i;
                        break;
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change:
                AlertDialog.Builder builder = new AlertDialog.Builder(MyBookInfoActivity.this);
                builder.setTitle("책 상태 변경")
                        .setMessage("책 상태를 변경하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                memoData.setState(radio_checkedId);
                                Completable updateResult = memoDao.updateMemo(memoData);
                                mDisposable.add (
                                        updateResult.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> Toast.makeText(MyBookInfoActivity.this, "변경 완료", Toast.LENGTH_SHORT).show(),
                                                        throwable -> Toast.makeText(MyBookInfoActivity.this, "변경 실패", Toast.LENGTH_SHORT).show())   );
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
            case R.id.btn_memo:
                Intent intent = new Intent(MyBookInfoActivity.this, MemoActivity.class);
                intent.putExtra("memoData", memoData);
                startActivity(intent);
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
