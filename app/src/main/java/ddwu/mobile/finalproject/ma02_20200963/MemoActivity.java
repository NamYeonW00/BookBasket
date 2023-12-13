package ddwu.mobile.finalproject.ma02_20200963;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemoActivity extends AppCompatActivity {
    final static String TAG = "MemoActivity";
    static Memo memoData;

    MemoDB memoDB;
    MemoDao memoDao;

    EditText et_memo;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        memoData = (Memo)getIntent().getSerializableExtra("memoData");
        et_memo = findViewById(R.id.et_memo);
        et_memo.setText(memoData.getMemo_content());

        memoDB = MemoDB.getDatabase(this);
        memoDao = memoDB.memoDao();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_update:
                AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                builder.setTitle("메모 수정")
                        .setMessage("메모를 수정하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                memoData.setMemo_content(et_memo.getText().toString());
                                Completable updateResult = memoDao.updateMemo(memoData);
                                mDisposable.add (
                                        updateResult.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> Toast.makeText(MemoActivity.this, "수정 완료", Toast.LENGTH_SHORT).show(),
                                                        throwable -> Toast.makeText(MemoActivity.this, "수정 실패", Toast.LENGTH_SHORT).show())   );
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
            case R.id.bt_share:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String message = memoData.getMemo_content();
                intent.putExtra(Intent.EXTRA_TEXT, message);
                Intent sharing = Intent.createChooser(intent, "공유하기");
                startActivity(sharing);
                break;
            case R.id.bt_close:
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
