package com.example.umfs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umfs.Model.Post;
import com.example.umfs.databinding.PostSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    Context context;
    ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post post = list.get(position);
        Picasso.get()
                .load(post.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postImage);
        Log.d("PostImage", "onBindViewHolder: " + post.getPostImage());

        String description = post.getPostDescription();
        if (description.equals("")){
            holder.binding.description.setVisibility(View.GONE);
        }else {
            holder.binding.description.setText(post.getPostDescription());
        }

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(post.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getCoverPhoto())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profileImage);
                        holder.binding.name.setText(user.getName());
                        holder.binding.profession.setText(user.getProfession());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        PostSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostSampleBinding.bind(itemView);
        }
    }
}
