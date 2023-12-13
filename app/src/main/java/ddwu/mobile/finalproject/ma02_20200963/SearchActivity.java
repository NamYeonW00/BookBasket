package ddwu.mobile.finalproject.ma02_20200963;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.finalproject.ma02_20200963.json.BookRoot;
import ddwu.mobile.finalproject.ma02_20200963.json.NaverBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";
    final int INFO_CODE = 200;

    EditText etTarget;
    ListView lvList;
    String apiAddress;

    String query;

    BookAdapter adapter;
    List<NaverBook> resultList;

    Retrofit retrofit;
    INaverBookSearchService naverApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etTarget = findViewById(R.id.etTarget);
        lvList = findViewById(R.id.lv_list);

        resultList = new ArrayList();
        adapter = new BookAdapter(this, R.layout.listview_book, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);


//        Retrofit 생성
        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(apiAddress)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        Retrofit Service 생성
        naverApiService = retrofit.create(INaverBookSearchService.class);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NaverBook bookData = resultList.get(position);
                Intent intent = new Intent(SearchActivity.this, InfoActivity.class);
                intent.putExtra("bookData", bookData);
                startActivityForResult(intent, INFO_CODE);
            }
        });

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Glide 를 사용하여 이미지 파일을 외장메모리에 저장
                // 해당 부분은 파일매니저로 분리 필요

                Glide.with(SearchActivity.this)
                        .asBitmap()
                        .load(resultList.get(position).getImage())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                
                                // 파일 처리 클래스로 분리 필요
                                
                                if (isExternalStorageWritable()) {
                                    File file = new File (getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                            "myalbum");
                                    if (!file.mkdirs()) {
                                        Log.d(TAG, "directory not created");
                                    }
                                    File saveFile = new File(file.getPath(), "test.jpg");
                                    try {
                                        FileOutputStream fos = new FileOutputStream((saveFile));
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                        Toast.makeText(SearchActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) { e.printStackTrace(); }
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                return true;
            }
        });

    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_search:
                query = etTarget.getText().toString();  // UTF-8 인코딩 필요

//                Retrofit 사용 시 인코딩 불필요
//                try {
//                    query = URLEncoder.encode(query, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

//                Retrofit 서비스 호출
                Call<BookRoot> apiCall = naverApiService.getBooks(getResources().getString(R.string.client_id),
                        getResources().getString(R.string.client_secret),
                        50,1, query);

                apiCall.enqueue(naverApiCallBack);

                break;
        }
    }


    Callback<BookRoot> naverApiCallBack = new Callback<BookRoot>() {
        @Override
        public void onResponse(Call<BookRoot> call, Response<BookRoot> response) {
            if (response.isSuccessful()) {
                // 파싱 결과를 adapter 에 설정
                BookRoot bookRoot = response.body();
                resultList.clear();
                resultList.addAll(bookRoot.getItems());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<BookRoot> call, Throwable t) {
            Log.d(TAG, t.toString());
            Toast.makeText(SearchActivity.this, "Cannot Access OpenAPI Server", Toast.LENGTH_SHORT).show();
        }
    };

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
