package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore firebaseFirestore;
                    firebaseFirestore = FirebaseFirestore.getInstance();

                    String firebaseUser = firebaseAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot dataSnapshot = task.getResult();
                                        if (dataSnapshot.exists()) {
                                            String usertypr = dataSnapshot.get("usertype").toString();

                                            if (usertypr.equals("Seller")){
                                                Intent intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Intent intent = new Intent(SplashActivity.this, BuyerDashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                }
                            });
                }

                else {
                    Intent Loginintent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(Loginintent);
                    finish();
                }

            }
        },SPLASH_SCREEN);
    }
}