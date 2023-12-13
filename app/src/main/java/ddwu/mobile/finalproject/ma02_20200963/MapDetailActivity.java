package ddwu.mobile.finalproject.ma02_20200963;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapDetailActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvPhone;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvName = findViewById(R.id.name);
        tvPhone = findViewById(R.id.number);
        tvAddress = findViewById(R.id.address);
        Intent intent = getIntent();

        tvName.setText(intent.getStringExtra("name"));
        tvPhone.setText(intent.getStringExtra("phone"));
        tvAddress.setText(intent.getStringExtra("address"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
