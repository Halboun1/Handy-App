package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyapp_v2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activities.MessagesActivity;
import Activities.models.Chat;
import Activities.models.usermodel.Model;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Model> mUsers;
    private String thelastMessage;

    boolean ischat = false;

    public UserAdapter(Context context, List<Model> mUsers, boolean ischat) {
        this.context = context;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model user = mUsers.get(position);
        holder.username.setText(user.getUsername());

        try {
            String url = user.getImageURL();

            if (url.equals("")||url.equals("default")) {
                Picasso.get().load(R.drawable.ic_profile).into(holder.profile_image);

            } else {
                Picasso.get().load(url).into(holder.profile_image);
            }

        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_profile).into(holder.profile_image);

        }

//        if (ischat){
        lastMessage(user.getId(), holder.last_msg);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView last_msg;
        public RoundedImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            last_msg = itemView.findViewById(R.id.last_msg);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    private void lastMessage(String userid, TextView last_msg) {
        thelastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                        thelastMessage = chat.getMessage();
                    }
                }
                switch (thelastMessage) {
                    case "default":
                        last_msg.setText("Open Chat");
                        break;
                    default:
                        last_msg.setText(thelastMessage);
                        break;
                }
                thelastMessage = "default";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
