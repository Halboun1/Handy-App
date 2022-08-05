package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class SellerAddDetailsActivity extends AppCompatActivity {

    EditText price,skills,description;
    String Price, uid;
    String category;
    Button upload;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String currentDate,currentTime;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_details);

        //find all elements by ID
        price = findViewById(R.id.price);
        skills = findViewById(R.id.skills);
        description = findViewById(R.id.description);
        Price = price.getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        upload = findViewById(R.id.update);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseFirestore firebaseFirestore;
                firebaseFirestore = FirebaseFirestore.getInstance();

                String firebaseUser = firebaseAuth.getCurrentUser().getUid();
//        Toast.makeText(getActivity(), firebaseUser, Toast.LENGTH_SHORT).show();

                firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot dataSnapshot = task.getResult();

                                    String strSkills = skills.getText().toString();
                                    String strDescription = description.getText().toString();
                                    String strPrice = price.getText().toString();

                                    if(strSkills.matches("") || strDescription.matches("") || strPrice.matches(""))
                                    {
                                        Toast.makeText(getApplicationContext(),"Can't upload work with empty fields",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        if (dataSnapshot.exists()) {
                                            String username = dataSnapshot.get("username").toString();

                                            uploadDate(username);

//                                       Toast.makeText(getActivity(), "getprofile data", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SellerAddDetailsActivity.this, "Error While getting data", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    /*if (dataSnapshot.exists()) {
                                        String username = dataSnapshot.get("username").toString();

                                        uploadDate(username);

//                                Toast.makeText(getActivity(), "getprofile data", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SellerAddDetailsActivity.this, "Error While getting data", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                            }
                        });

            }
        });
    }

    private void uploadDate(String userName) {
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String instantkey = currentDate+" "+currentTime;
        Map<String,Object> map = new HashMap();
        map.put("category",category);
        map.put("price",price.getText().toString());
        map.put("skills",skills.getText().toString());
        map.put("description",description.getText().toString());
        map.put("uid",uid);


        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
        Call<POST> call = apiInterface.newData(category, price.getText().toString(), skills.getText().toString(),userName, description.getText().toString(), uid);

        call.enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {
                Toast.makeText(SellerAddDetailsActivity.this, "Job has been done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SellerAddDetailsActivity.this, SellerDashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<POST> call, Throwable t) {
                //Toast.makeText(SellerAddDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
}