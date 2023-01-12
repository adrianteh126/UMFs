package com.example.umfs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private Context context;
    private List<User>userList;
    private List<String>userID;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    public ChatAdapter(Context context){
        this.context=context;
        userList=new ArrayList<>();
    }

    public void add(User user){
        userList.add(user);
        notifyDataSetChanged();
    }

    public void clear(){
        userList.clear();
        notifyDataSetChanged();
    }

    public void addID(String id){
        userID.add(id);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getUsername());
        holder.email.setText(user.getSiswamail());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture/" + user.getProfilePicture());
        try {
            File localfile = File.createTempFile("Temp File", ".jpg");
            storageReference.getFile(localfile)
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            //holder.profile_pic.setImageBitmap(bitmap);
                            Picasso.get().load(user.getProfilePicture()).into(holder.profile_pic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent=new Intent(context, ConversationActivity.class);
                intent.putExtra("id", user.getUserUID());
                intent.putExtra("name",user.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email;
        private ImageView profile_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.chat_name);
            email=itemView.findViewById(R.id.email);
            profile_pic=itemView.findViewById(R.id.chat_profile);
        }
    }
}
