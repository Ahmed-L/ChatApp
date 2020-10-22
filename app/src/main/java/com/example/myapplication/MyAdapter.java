package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Messages>messages;

    public MyAdapter(List<Messages> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Messages ld=messages.get(position);
        holder.nametxt.setText(ld.getUsername());
        holder.msgtext.setText(ld.getTextMessage());
        holder.datetxt.setText(ld.getDate());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nametxt,msgtext,datetxt;
        public ViewHolder(View itemView) {
            super(itemView);
            nametxt=(TextView)itemView.findViewById(R.id.Name_des);
            msgtext=(TextView)itemView.findViewById(R.id.message_des);
            datetxt=(TextView)itemView.findViewById(R.id.date_des);
        }
    }
}