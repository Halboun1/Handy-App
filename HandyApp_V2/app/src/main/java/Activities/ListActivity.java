package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Activities.models.ListModel;
import Adapters.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter myAdapter ;
    ListModel listModel;
    ArrayList<ListModel> list= new ArrayList<ListModel>();;
    FirebaseFirestore firestore;
    String category;
    TextView cat_Title;
    ImageView cat_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recycleView);
        cat_Title = findViewById(R.id.cat_title);
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        cat_Title.setText(category);

        Bundle b = getIntent().getExtras();
        int img = b.getInt("pic");
        cat_Title.setBackgroundResource(img);


        listModel = new ListModel();
        RetreaveFirestore();
    }
    private void RetreaveFirestore() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ListModel>();
        myAdapter = new ListAdapter(this, list);

        recyclerView.setAdapter(myAdapter);

        EventChildListner();
    }

    private void EventChildListner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://handymendapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<ListModel>> call = apiInterface.getCategory(category.toString());

        call.enqueue(new Callback<List<ListModel>>() {
            @Override
            public void onResponse(Call<List<ListModel>> call, Response<List<ListModel>> response) {
                if(response.isSuccessful()) {
                    List<ListModel> posts = response.body();
                    for(ListModel post: posts) {
                        list.add((ListModel) post);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ListModel>> call, Throwable t) {

            }
        });
    }
}