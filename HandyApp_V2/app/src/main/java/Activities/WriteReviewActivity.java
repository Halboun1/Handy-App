package Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handyapp_v2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class WriteReviewActivity extends AppCompatActivity {

    TextView rateCount;
    RatingBar ratingBar;
    float rateValue;
    Button submit;
    EditText Review;
    private String proID = "",uid;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        proID = getIntent().getStringExtra("proid");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        rateCount = findViewById(R.id.rate_count);
        ratingBar = findViewById(R.id.ratingbar);
        submit = findViewById(R.id.submit_review);
        Review = findViewById(R.id.editText);
        uid = firebaseAuth.getCurrentUser().getUid();

        rateValue = ratingBar.getRating();
        rateCount.setText(""+rateValue);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();
                rateCount.setText(""+rateValue);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = Review.getText().toString();
                String count = rateCount.getText().toString();
                if(review.matches("") || count.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Can't leave empty field",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UoloadView(proID,review,count,uid);
                }

            }
        });
    }

    private void UoloadView(String proID, String review, String count, String uid) {
        Map<String,Object> map = new HashMap();
        map.put("review",review);
        map.put("keyid",proID);
        map.put("rate",count);
        map.put("uid",uid);

        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        Call<POST> call = apiInterface.newreview(review, proID, count, uid);

        call.enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {
                if (response.isSuccessful()){
                    Toast.makeText(WriteReviewActivity.this, "Review Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<POST> call, Throwable t) {
                Toast.makeText(WriteReviewActivity.this, "", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}