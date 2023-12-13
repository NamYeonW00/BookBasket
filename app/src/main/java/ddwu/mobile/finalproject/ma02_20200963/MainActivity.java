package ddwu.mobile.finalproject.ma02_20200963;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.btn_bList:
                intent = new Intent(this, BookListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_bSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_s:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
