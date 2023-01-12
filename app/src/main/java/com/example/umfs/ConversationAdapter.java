package com.example.umfs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

    private Context context;
    private List<MessageModel>messageModelList;

    public ConversationAdapter(Context context) {
        this.context = context;
        messageModelList=new ArrayList<>();
    }

    public void add(MessageModel messageModel){
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    public void clear(){
        messageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.msg.setText(messageModel.getMessage());
        if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.light_blue_1));
        }
        else{
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.light_grey_1));
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.message);
            main=itemView.findViewById(R.id.mainMessageLayout);
        }
    }

}
