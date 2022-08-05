package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Activities.SellerDetailsActivity;
import Activities.models.ListModel;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListModel> list;

    FirebaseFirestore firebaseFirestore;
    String userid;

    public ListAdapter(Context context, ArrayList<ListModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_seller_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ListModel listModel = list.get(position);
        holder.title.setText(listModel.getCategory());
        holder.price.setText("$ " + listModel.getPrice() + "/hr");
        //holder.categoryTitle.setText(listModel.getCategory());

        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("users").document(listModel.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()) {
                                String username = dataSnapshot.get("username").toString();

                                try {
                                    String imageurl = dataSnapshot.get("imageURL").toString();
                                    Picasso.get().load(imageurl).into(holder.profile);

                                } catch (Exception e) {
                                    e.printStackTrace();


                                }

                                     holder.name.setText(username);

                            } else {

                            }
                        }
                    }
                });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellerDetailsActivity.class);
                intent.putExtra("pid", listModel.getId());
                context.startActivity(intent);
            }
        });

        try {


        } catch (Exception e) {

            e.printStackTrace();
        }




    }



    @Override
    public int getItemCount() {
        return  list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, post, price,categoryTitle;
        RoundedImageView  profile;
        private FirebaseFirestore profileref;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvUser);
            title = itemView.findViewById(R.id.tvTitle);
            price = itemView.findViewById(R.id.tvPrice);
            profile = itemView.findViewById(R.id.imgBg);
           // categoryTitle = itemView.findViewById(R.id.cat_title);


            profileref = FirebaseFirestore.getInstance();


        }
    }
}
