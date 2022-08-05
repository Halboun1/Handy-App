package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Activities.models.ListModel;
import Activities.models.Rate;
import Activities.models.Review;
import Adapters.ReviewAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellerDetailsActivity extends AppCompatActivity {

    TextView name, skills, price, description, category, address, rating;
    FloatingActionButton floatingActionButton;
    private String proID = "";
    String sname;
    String suid;
    Button giveReview, makePayment;
    RelativeLayout loading;
    RecyclerView recyclerView;
    ReviewAdapter myAdapter ;
    Review review;
    ArrayList<Review> list= new ArrayList<Review>();
    ApiInterface apiInterface;
    CircleImageView user_image;

    FirebaseFirestore firestore;
    String sprice;
    private int id ;

    String Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proID = getIntent().getStringExtra("pid");
        id = Integer.parseInt(proID);
        setContentView(R.layout.activity_seller_details);


        name = findViewById(R.id.seller_name);
        skills = findViewById(R.id.seller_skills);
        price = findViewById(R.id.sellerprice);
        description = findViewById(R.id.seller_description);
       // category = findViewById(R.id.seller_category);
        //address = findViewById(R.id.seller_address);
        user_image = findViewById(R.id.userImage);
        loading = findViewById(R.id.loading);
        makePayment = findViewById(R.id.make_payment);
        loading.setVisibility(View.VISIBLE);
        giveReview = findViewById(R.id.give_review);
        recyclerView = findViewById(R.id.sellerRecyclerList);
        rating = findViewById(R.id.rate);
        firestore = FirebaseFirestore.getInstance();
        review = new Review();

        floatingActionButton = findViewById(R.id.floatingActionButton);

        giveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerDetailsActivity.this, WriteReviewActivity.class);
                intent.putExtra("proid",proID);
                startActivity(intent);

            }
        });
        getrating("135");
        getdetailswithApis(id);
        getReviews();


    }

    private void getrating(String s) {
        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        Call<List<Rate>> call = apiInterface.getrate("135");
        call.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                if (response.isSuccessful()){
                    for (Rate rate: response.body()){
                        rating.append(""+rate.getRating());
                        Toast.makeText(SellerDetailsActivity.this,""+rate.getRating() , Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {

            }

        });

    }

    private void getdetailswithApis(int iid) {
        apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

        Call<List<ListModel>> call = apiInterface.getdetails(iid);
        call.enqueue(new Callback<List<ListModel>>() {
            @Override
            public void onResponse(Call<List<ListModel>> call, Response<List<ListModel>> response) {
                if (response.isSuccessful()){
                    for (ListModel listModel: response.body()){
                        skills.append("Skills: "+listModel.getSkills());
                        //category.append(""+listModel.getCategory());
                        name.append(listModel.getUid());
                        description.append("Description: "+listModel.getDescription());
                        price.setText("$ " + listModel.getPrice() + "/hr");
                        Price = listModel.getPrice();
                        loading.setVisibility(View.GONE);
                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SellerDetailsActivity.this, MessagesActivity.class);
                                intent.putExtra("userid", listModel.getUid());
                                startActivity(intent);
                            }
                        });



                        getuserdata(listModel.getUid());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<ListModel>> call, Throwable t) {

            }
        });

    }

    private void getuserdata(String uid) {
        FirebaseFirestore userref = FirebaseFirestore.getInstance();

        userref.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {

                        try {
                            String imageurl = dataSnapshot.get("imageURL").toString();
                            Picasso.get().load(imageurl).into(user_image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sname = dataSnapshot.get("username").toString();
                        String receiveremail = dataSnapshot.get("email").toString();
                        makePayment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SellerDetailsActivity.this, PaymentActivity.class);
                                intent.putExtra("email", receiveremail);
                                intent.putExtra("name", sname);
                                intent.putExtra("price", Price);
                                startActivity(intent);
                            }
                        });

                        final String sAddress = dataSnapshot.get("Address").toString();
                        loading.setVisibility(View.GONE);
                        name.setText(sname);
                       // address.setText(sAddress);

                    }

                }
            }
        });


    }





    private void getReviews() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Review>();
        myAdapter = new ReviewAdapter(this, list);

        recyclerView.setAdapter(myAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://handymendapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Review>> call = apiInterface.getReviews(proID);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.isSuccessful()) {
                    List<Review> posts = response.body();
                    for(Review post: posts) {
                        list.add((Review) post);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });



    }

}