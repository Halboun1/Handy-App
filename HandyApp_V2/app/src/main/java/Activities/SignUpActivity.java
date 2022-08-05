package Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handyapp_v2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    /*-------- Variable Define -----------*/
    LinearLayout sellerRegisterCard,buyerRegisterCard;
    ImageView tickSeller,tickBuyer;
    TextView tvIam1,tvIam2,tvStudent,tvTutor;
    ImageView password_show, password_hide;
    FrameLayout frame_eye;
    TextView login;


    //------- FireBase init --------//
    FirebaseAuth auth;
    DatabaseReference reference;


    EditText email_txt;
    EditText address_txt;
    EditText password_txt;
    EditText userName_txt;

    String userName;
    String userPassword;
    String userEmail;

    Button createAccountBtn;
    private int passwordNotVisible = 1;

    FirebaseFirestore db;
    RelativeLayout loading;
    String UserType = "";

    /////////////////////////////////////////////////////
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    TextView GoogleSignin;
    int RC_SIGN_IN = 1;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);
        GoogleSignin = findViewById(R.id.btn_google_signin);

        loading = findViewById(R.id.loading);


        mAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();


        db = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("153302111375-jrqv1e68fu2q6pjabddgp1vud2lcn9bl.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });




        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        sellerRegisterCard = findViewById(R.id.sellerRegister);
        buyerRegisterCard = findViewById(R.id.buyerRegister);
        tickSeller = findViewById(R.id.imgTickSeller);
        tickBuyer = findViewById(R.id.imgTickBuyer);
        tvIam1 = findViewById(R.id.tvIam1);
        tvIam2 = findViewById(R.id.tvIam2);
        tvStudent = findViewById(R.id.tvStudent);
        tvTutor = findViewById(R.id.tvTutor);
        createAccountBtn = findViewById(R.id.createAccBtn);
        login = findViewById(R.id.loginBtn);
        //liStudent.setOnClickListener((View.OnClickListener) this);


        //--------User Edit Txt Field ID---------//
        password_txt = findViewById(R.id.userPasswordtxt);
        email_txt = findViewById(R.id.userEmailtxt);
        address_txt = findViewById(R.id.userAddresstxt);
        userName_txt = findViewById(R.id.userNametxt);



        //---------Getting ALL USER ENTRY----------//



        frame_eye = findViewById(R.id.frame_eye);
        password_show = findViewById(R.id.password_show);
        password_hide = findViewById(R.id.password_hide);

        /*---------- Password hide and show code here ----------*/
        frame_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    password_show.setVisibility(View.VISIBLE);
                    password_hide.setVisibility(View.GONE);
                    password_txt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    password_show.setVisibility(View.GONE);
                    password_hide.setVisibility(View.VISIBLE);
                    password_txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                password_txt.setSelection(password_txt.length());
            }
        });

        ScrollView scrollview = findViewById(R.id.scrollview1);
        scrollview.fullScroll(View.FOCUS_DOWN);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!UserType.equals("")){
                    Toast.makeText(SignUpActivity.this, "kindly select usertype", Toast.LENGTH_SHORT).show();
                }
                else if (email_txt.getText().toString().equals("")||password_txt.getText().toString().equals("")||
                userName_txt.getText().toString().equals("")||address_txt.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this, "You should set all fields", Toast.LENGTH_SHORT).show();
                }
                else if (password_txt.getText().toString().length()<=5){
                    Toast.makeText(SignUpActivity.this, "Password should be 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    loading.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email_txt.getText().toString(), password_txt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        saveDataOfUser(user);

                                    } else {
                                        loading.setVisibility(View.GONE);
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




    private void saveDataOfUser(FirebaseUser user) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> users = new HashMap<>();
        users.put("email",email_txt.getText().toString());
        users.put("usertype",UserType);
        users.put("Password",password_txt.getText().toString());
        users.put("username",userName_txt.getText().toString());
        users.put("Address",address_txt.getText().toString());
        users.put("uid",uid);
        users.put("imageURL","default");



        db.collection("users")
                .document(uid)
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        String txt_username = userName_txt.getText().toString();
                        String txt_email = email_txt.getText().toString();
                        String txt_password = password_txt.getText().toString();

                        register(txt_username,txt_email,txt_password);


//                        Toast.makeText(SignUpActivity.this, "Profile has been set", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("user", UserType);
                        myEdit.commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Something went Wrong:  "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void register(String username, String email, String password) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userid = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userid);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id",userid);
        hashMap.put("Password",password);
        hashMap.put("username",username);
        hashMap.put("imageURL","default");

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loading.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(SignUpActivity.this, "" + e, Toast.LENGTH_SHORT).show();

                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            uploaddatatofirestore(user);
//                            updateUI(user);
                        } else {
                            Toast.makeText(SignUpActivity.this, "signInWithCredential:faild", Toast.LENGTH_SHORT).show();

                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

    private void uploaddatatofirestore(FirebaseUser user) {
        loading.setVisibility(View.VISIBLE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> users = new HashMap<>();
        users.put("email",user.getEmail());
        users.put("uid",uid);
        users.put("imageURL","default");
        db.collection("users")
                .document(uid)
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loading.setVisibility(View.GONE);
                        Intent intent = new Intent(SignUpActivity.this, SellerDashboardActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Something went Wrong:  "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }









    @Override
    protected void onStart() {
        super.onStart();

    }



}