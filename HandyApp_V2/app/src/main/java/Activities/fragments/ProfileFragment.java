package Activities.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Activities.BuyerDashboardActivity;
import Activities.EditProfileActivity;
import Activities.LoginActivity;
import Activities.SellerDashboardActivity;
import Activities.SignUpActivity;


public class ProfileFragment extends Fragment {

    TextView nametxt,usertype,changeUser;
    TextView name, email, description,address;
    FirebaseAuth firebaseAuth;
    RoundedImageView profileImageView;
    ImageView editImage,editprofile;
    private Uri resultUri;
    private String url = "";
    private DatabaseReference Refdatabase;
    private FirebaseFirestore firestore;
    private StorageReference storage;
    RelativeLayout loading;
    Button logout;
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences("usertype", Context.MODE_PRIVATE);
        nametxt = view.findViewById(R.id.nametxt);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        loading = view.findViewById(R.id.loading);
        usertype = view.findViewById(R.id.usertype);
        changeUser = view.findViewById(R.id.change_user_typr);
        address = view.findViewById(R.id.address);
        logout = view.findViewById(R.id.logout);

        profileImageView = view.findViewById(R.id.imgUser);
        editImage = view.findViewById(R.id.edit_image);
        editprofile = view.findViewById(R.id.edit_profile);

        Refdatabase = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        selectImage();

                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
            }
        });
        getProfileData();
    }

    private void getProfileData() {
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
                                String username = dataSnapshot.get("username").toString();
                                String myemail = dataSnapshot.get("email").toString();
                                String userType = dataSnapshot.get("usertype").toString();
                                String userAddress = dataSnapshot.get("Address").toString();
                                try {
                                    String imageurl = dataSnapshot.get("imageURL").toString();
                                    Picasso.get().load(imageurl).into(profileImageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                                }
                                if (userType.equals("Seller")){
                                    changeUser.setText("Switch to Buyer");
                                }else {
                                    changeUser.setText("Switch to Seller");
                                }
                                changeUserType(userType);

                                address.setText(userAddress);
                                usertype.setText(userType);
                                nametxt.setText(username);
                                name.setText(username);
                                email.setText(myemail);

                            } else {
                                Toast.makeText(getActivity(), "Error While getting data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void changeUserType(String userType) {
        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equals("Seller")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure want to switch to Buyer");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore firestore;
                            firestore = FirebaseFirestore.getInstance();
                            HashMap<String, Object> map = new HashMap<>();

                            map.put("usertype", "Buyer");

                            firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getActivity(), BuyerDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure want to switch to Seller?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            FirebaseFirestore firestore;
                            firestore = FirebaseFirestore.getInstance();
                            HashMap<String,Object> map = new HashMap<>();

                            map.put("usertype","Seller");

                            firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(getActivity(), SellerDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });
    }

    private void uploadimage() {
        if (resultUri != null) {
            loading.setVisibility(View.VISIBLE);


            final StorageReference ref = storage.child("profiles/" + firebaseAuth.getCurrentUser().getUid());
            UploadTask uploadTask = ref.putFile(resultUri);


            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        String error = task.getException().getMessage();
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);

                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {


                        uploadImage();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);


                    }
                }
            });
        } 

    }

    private void uploadImage() {

        Map<String, Object> map = new HashMap<>();
        map.put("imageURL", url);

        String currentUserrID = firebaseAuth.getCurrentUser().getUid();

        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("imageURL", url);



        Refdatabase.child("Users").child(currentUserrID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Your profile has been updated...", Toast.LENGTH_SHORT).show();
                } else {
                    String errormessage = task.getException().toString();

                }
            }
        });


        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Your profile has been updated...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.purple_200))
                .setActivityTitle("Profile Photo")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                resultUri = result.getUri();
                Glide
                        .with(getContext())
                        .load(resultUri)
                        .centerCrop()

                        .into(profileImageView);
                uploadimage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}