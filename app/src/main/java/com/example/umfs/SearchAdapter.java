package com.example.umfs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    public ArrayList<Post> list;

    public SearchAdapter(ArrayList<Post> list) {
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

        holder.LLSearchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: when the search card is click, then navigate to that specific post
                Toast.makeText(v.getContext(),"The search card is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.TVSearchCardUserID.setText(list.get(position).getPostBy());
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
