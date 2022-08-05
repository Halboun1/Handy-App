package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import Activities.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Review> list;

    FirebaseFirestore firebaseFirestore;
    FirebaseFirestore firestoreaddreview;
    String userid;
    int sum = 0;
    int tsum = 0;

    public ReviewAdapter(Context context, ArrayList<Review> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.seller_review_list, parent, false);
        return new ReviewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {

        Review review = list.get(position);
        holder.name.setText(review.getUid());
        holder.review.setText(review.getReview());

        String rate = review.getRate();
        int ratenum = (int)Double.parseDouble(review.getRate());
        holder.ratingBar.setRating(ratenum);

        final String keyid = review.getKeyid();
        final String uid = review.getUid();

        int reviewsum = (int)Double.parseDouble(review.getRate());
        int count = getItemCount();

        for (int i = 0;i<count;i++){
            tsum = 0;

            tsum = tsum+reviewsum;
        }




        firebaseFirestore = FirebaseFirestore.getInstance();
        firestoreaddreview = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("users").document(uid).get()
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
                                    Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                                }

                                holder.name.setText(username);

                            } else {

                            }
                        }
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
        TextView name, review;
        RoundedImageView profile;
        RatingBar ratingBar;
        private FirebaseFirestore profileref;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.review_name);
            review = itemView.findViewById(R.id.review_text);
            profile = itemView.findViewById(R.id.image1);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            profileref = FirebaseFirestore.getInstance();


        }
    }
}
