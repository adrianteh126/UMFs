package com.example.umfs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    public ArrayList<Post> list;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public SearchAdapter(Context context,ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = list.get(position);
        holder.LLSearchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: when the search card is click, then navigate to that specific post
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postedBy", post.getPostBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
                Toast.makeText(context,"The search card is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.TVSearchCardUserID.setText(list.get(position).getPostUserName());
        holder.TVSearchCardCategory.setText(list.get(position).getPostCategory());
        holder.TVSearchCardTitle.setText(list.get(position).getPostTitle());
        holder.TVSearchCardDesc.setText(list.get(position).getPostDescription());

        //if the description character > 300, then slice the string become <= 300
//        holder.TVSearchCardDesc.setText(
//                list.get(position).getPostDescription().length() > 301 ?
//                        list.get(position).getPostDescription() :
//                        list.get(position).getPostDescription().substring(0,301));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Modify ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout LLSearchCard;
        TextView TVSearchCardUserID;
        TextView TVSearchCardCategory;
        TextView TVSearchCardTitle;
        TextView TVSearchCardDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            LLSearchCard = itemView.findViewById(R.id.LLSearchCard);
            TVSearchCardUserID = itemView.findViewById(R.id.TVSearchCardUserID);
            TVSearchCardCategory = itemView.findViewById(R.id.TVSearchCardCategory);
            TVSearchCardTitle = itemView.findViewById(R.id.TVSearchCardTitle);
            TVSearchCardDesc = itemView.findViewById(R.id.TVSearchCardDesc);
        }
    }

}
