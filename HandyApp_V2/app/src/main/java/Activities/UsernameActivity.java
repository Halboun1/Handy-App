package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsernameActivity extends AppCompatActivity {

    LinearLayout sellerRegisterCard, buyerRegisterCard;
    ImageView tickSeller, tickBuyer;
    TextView tvIam1, tvIam2, tvStudent, tvTutor;
    ImageView password_show, password_hide;
    EditText address_txt;
    EditText userName_txt;
    FirebaseFirestore db;
    FirebaseAuth auth;
    DatabaseReference reference;
    RelativeLayout loading;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference Refdatabase;
    String UserType = "";
    Button upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        loading = findViewById(R.id.loading);
        auth = FirebaseAuth.getInstance();
        upload = findViewById(R.id.uploadbtn);
        userName_txt = findViewById(R.id.userNametxt);
        address_txt = findViewById(R.id.addresstxt);
        Refdatabase= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        sellerRegisterCard = findViewById(R.id.sellerRegister);
        buyerRegisterCard = findViewById(R.id.buyerRegister);
        tickSeller = findViewById(R.id.imgTickSeller);
        tickBuyer = findViewById(R.id.imgTickBuyer);
        tvIam1 = findViewById(R.id.tvIam1);
        tvIam2 = findViewById(R.id.tvIam2);
        tvStudent = findViewById(R.id.tvStudent);
        tvTutor = findViewById(R.id.tvTutor);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                uploadUsername();
            }
        });

    }

    private void uploadUsername() {

        Map<String, Object> map = new HashMap<>();
        map.put("username", userName_txt.getText().toString());
        map.put("Address", address_txt.getText().toString());
        map.put("usertype",UserType);



        String  currentUserrID=firebaseAuth.getCurrentUser().getUid();

        HashMap<String,Object> profileMap= new HashMap<>();
        profileMap.put("id",currentUserrID);
        profileMap.put("username",userName_txt.getText().toString());
        profileMap.put("imageURL","default");



        Refdatabase.child("Users").child(currentUserrID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(UsernameActivity.this,"Your profile has been updated...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String errormessage=task.getException().toString();
//                    Toast.makeText(UsernameActivity.this,"Error :"+errormessage,Toast.LENGTH_SHORT).show();
                }
            }
        });


        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.GONE);
                            Intent mainintent = new Intent(UsernameActivity.this, SplashActivity.class);
                            startActivity(mainintent);
                            finish();
                        } else {

                        }
                    }
                });
    }


    public void ChangeCards(View v) {
        switch (v.getId()) {
            case R.id.sellerRegister:
                changeCardsSelection(1);
                UserType="Seller";
                break;
            case R.id.buyerRegister:
                changeCardsSelection(2);
                UserType="Buyer";
                break;
        }

    }
    public void changeCardsSelection(int flag) {
        if (flag == 1) {
            sellerRegisterCard.setBackgroundResource(R.drawable.rectangle_navy_blue);
            buyerRegisterCard.setBackgroundResource(R.drawable.rectangle_silver);
            tickSeller.setImageResource(R.drawable.ic_circle_tick);
            tickBuyer.setImageResource(R.drawable.circle_black_border);
            tvIam1.setTextColor(getResources().getColor(R.color.white));
            tvIam2.setTextColor(getResources().getColor(R.color.black));
            tvStudent.setTextColor(getResources().getColor(R.color.white));
            tvTutor.setTextColor(getResources().getColor(R.color.black));
        }
        if (flag == 2) {
            sellerRegisterCard.setBackgroundResource(R.drawable.rectangle_silver);
            buyerRegisterCard.setBackgroundResource(R.drawable.rectangle_navy_blue);
            tickSeller.setImageResource(R.drawable.circle_black_border);
            tickBuyer.setImageResource(R.drawable.ic_circle_tick);
            tvIam1.setTextColor(getResources().getColor(R.color.black));
            tvIam2.setTextColor(getResources().getColor(R.color.white));
            tvStudent.setTextColor(getResources().getColor(R.color.black));
            tvTutor.setTextColor(getResources().getColor(R.color.white));
        }
    }

}