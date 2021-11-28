package com.example.pushnotification.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushnotification.R;
import com.example.pushnotification.model.Product;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CommentHolder> {

    private Context context;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    private itemListener listener;

    public HistoryAdapter(Context context, ArrayList<Product> productArrayList, itemListener listener) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.inflater  = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.bind(product);
        holder.itemView.setOnClickListener(v -> {
            listener.onClickItem(product);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList == null ? 0 : productArrayList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewName, tvTime;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_id);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        public void bind(Product product){
            textViewName.setText(product.getProductId() + "");
            tvTime.setText(product.getTimeSave());
        }
    }

    public interface itemListener{
        void onClickItem(Product product);
    }
}
