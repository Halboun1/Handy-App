package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;

import java.util.List;

import Data.ReviewData;

public class ReviewDataAdapter extends RecyclerView.Adapter<ReviewDataAdapter.ViewHolder> {
    private List<ReviewData> userList;

    public ReviewDataAdapter(List<ReviewData> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ReviewDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_review_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewDataAdapter.ViewHolder holder, int position) {
        int resource = userList.get(position).getImage1();
        String review_name = userList.get(position).getReviewname();
        String review_txt = userList.get(position).getReview_text();
        String dvder = userList.get(position).getDivider();
        holder.setDate(resource, review_name, review_txt, dvder);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView profile;
        private TextView reviewname;
        private TextView review_text;
        private TextView divider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            profile = itemView.findViewById(R.id.image1);
            reviewname = itemView.findViewById(R.id.review_name);
            review_text = itemView.findViewById(R.id.review_text);

        }

        public void setDate(int resource, String review_name, String review_txt, String dvder) {
            profile.setImageResource(resource);
            reviewname.setText(review_name);
            review_text.setText(review_txt);
            divider.setText(dvder);
        }
    }
}
