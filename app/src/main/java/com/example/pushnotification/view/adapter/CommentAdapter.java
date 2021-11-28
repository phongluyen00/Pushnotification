package com.example.pushnotification.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushnotification.R;
import com.example.pushnotification.model.MessageResponse;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context context;
    private List<MessageResponse.Value> messageResponseList;
    private LayoutInflater inflater;

    public void setMessageResponseList(ArrayList<MessageResponse.Value> messageResponseList) {
        this.messageResponseList = messageResponseList;
    }

    public CommentAdapter(Context context, List<MessageResponse.Value> messageResponseList) {
        this.context = context;
        this.messageResponseList = messageResponseList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comment,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        MessageResponse.Value messageResponse = messageResponseList.get(position);
        holder.bind(messageResponse);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return messageResponseList == null ? 0 : messageResponseList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewName, tvTime;
        AppCompatTextView tvComment;
        RatingBar ratingBar;
        View view;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.messageResponse);
            ratingBar =itemView.findViewById(R.id.ratingBar21);
            tvTime = itemView.findViewById(R.id.time);
            view = itemView.findViewById(R.id.enabled);
        }

        public void bind(MessageResponse.Value messageResponse){
            textViewName.setText(messageResponse.getUsername());
            tvComment.setText(messageResponse.getComment());
            ratingBar.setRating(Float.parseFloat(messageResponse.getRate()));
            tvTime.setText(messageResponse.getTime());
        }
    }
}
