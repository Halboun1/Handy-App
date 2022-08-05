package Activities.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import Activities.ApiInterface;
import Activities.models.ListModel;
import Adapters.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    ListAdapter myAdapter;
    ListModel listModel;
    ArrayList<ListModel> list = new ArrayList<ListModel>();
    ;
    FirebaseFirestore firestore;
    String category;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recycleView);
        search = view.findViewById(R.id.search);
        firestore = FirebaseFirestore.getInstance();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()){
                    RetreaveFirestore("");

                    recyclerView.setVisibility(View.GONE);
                }

                else  {
                    RetreaveFirestore(s.toString());
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

        });
        listModel = new ListModel();


    }

    private void RetreaveFirestore(String start) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<ListModel>();
        myAdapter = new ListAdapter(getContext(), list);

        recyclerView.setAdapter(myAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://handymendapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<ListModel>> call = apiInterface.search(start);

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


        Query query = firestore.collection("data")
                .orderBy("category").startAt(start).endAt(start+"\uf888");


        String Search = "";
        Search = search.getText().toString();
    }






}