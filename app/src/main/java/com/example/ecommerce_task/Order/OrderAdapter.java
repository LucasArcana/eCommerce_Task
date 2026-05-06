package com.example.ecommerce_task.Order;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_task.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final List<OrderItem> orders;

    public OrderAdapter(List<OrderItem> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem order = orders.get(position);
        holder.tvName.setText(order.getProductName());
        holder.tvPrice.setText(String.format("$%.2f", order.getPrice()));
        holder.tvDate.setText(order.getDate());
        holder.tvStatus.setText(order.getStatus());
        Glide.with(holder.itemView.getContext())
                .load(order.getProductImage())
                .placeholder(new ColorDrawable(0xFF70D8F0))
                .centerCrop()
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() { return orders.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvDate, tvStatus;
        ImageView ivImage;
        ViewHolder(View view) {
            super(view);
            tvName   = view.findViewById(R.id.tv_order_name);
            tvPrice  = view.findViewById(R.id.tv_order_price);
            tvDate   = view.findViewById(R.id.tv_order_date);
            tvStatus = view.findViewById(R.id.tv_order_status);
            ivImage = view.findViewById(R.id.iv_order_image);
        }
    }
}