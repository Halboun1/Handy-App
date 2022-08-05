package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    EditText address, password,userName,email;
    Button update;
    String Password;
    String Address;
    String UserName;
    String Email;
    String oldpass ="";
    String em;
    String olduserAddress;
    String oldUserName;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.name);
        email = findViewById(R.id.email);
        update = findViewById(R.id.uploadbtn);

        getOldPassword();

    }

    private void UpdatePassword(String password, String address, String oldpass,String username) {
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();


        final String email = user.getEmail();

        AuthCredential credential = EmailAuthProvider.getCredential(email, oldpass);
        em = email;

        if(oldpass != Password) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(EditProfileActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                } else {

                                    UpdateAddress();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void UpdateAddress() {
        FirebaseFirestore firestore;
        FirebaseUser user;
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> map = new HashMap<>();


        if(em != Email)
        {
            user.updateEmail(Email);
            map.put("email",Email);
            flag = true;
        }
        if (oldpass != Password)
        {
            map.put("Password", password.getText().toString());
            flag = true;
        }
        if (oldUserName != UserName)
        {
            map.put("username", userName.getText().toString());
            flag = true;
        }
        if (olduserAddress != Address)
        {
            map.put("Address", address.getText().toString());
            flag = true;
        }

        if(flag == true) {
            firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile has been updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }

    }

    private void getOldPassword() {
        FirebaseAuth firebaseAuth;
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
                                oldpass = dataSnapshot.get("Password").toString();
                                olduserAddress = dataSnapshot.get("Address").toString();
                                oldUserName = dataSnapshot.get("username").toString();
                                String oldEmail = dataSnapshot.get("email").toString();

                                password.setText(oldpass);
                                address.setText(olduserAddress);
                                userName.setText(oldUserName);
                                email.setText(oldEmail);

                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Password = password.getText().toString();
                                        Address = address.getText().toString();
                                        UserName = userName.getText().toString();
                                        Email = email.getText().toString();

                                        if (Password.length() <= 5) {
                                            Toast.makeText(EditProfileActivity.this, "password should be minimum 6 characters long", Toast.LENGTH_SHORT).show();
                                        } else if (Password.equals("")) {
                                            Toast.makeText(EditProfileActivity.this, "Please provide valid password", Toast.LENGTH_SHORT).show();
                                        } else if (Address.equals("")) {
                                            Toast.makeText(EditProfileActivity.this, "Please provide valid address", Toast.LENGTH_SHORT).show();
                                        } else if (UserName.equals("")) {
                                            Toast.makeText(EditProfileActivity.this, "Please provide valid username", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (Email.equals("")) {
                                            Toast.makeText(EditProfileActivity.this, "Please provide valid E-mail", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            UpdatePassword(Password, Address, oldpass,UserName);
                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error While getting data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}