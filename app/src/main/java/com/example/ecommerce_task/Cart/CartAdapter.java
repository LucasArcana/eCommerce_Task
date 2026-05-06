package com.example.ecommerce_task.Cart;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_task.Product.Product;
import com.example.ecommerce_task.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private final List<CartItem> items;
    private final OnCartChangeListener listener;

    public CartAdapter(List<CartItem> items, OnCartChangeListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item    = items.get(position);
        Product product = item.getProduct();

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getFormattedPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        String imageUrl = product.getFirstImage();
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(new ColorDrawable(0xFF70D8F0))
                    .centerCrop()
                    .into(holder.ivProduct);
        }

        holder.btnMinus.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Cartmanager.getInstance(holder.itemView.getContext()).updateQuantity(pos, item.getQuantity() - 1);
            if (item.getQuantity() <= 0) {
                notifyItemRemoved(pos);
            } else {
                notifyItemChanged(pos);
            }
            listener.onCartChanged();
        });

        holder.btnPlus.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Cartmanager.getInstance(holder.itemView.getContext()).updateQuantity(pos, item.getQuantity() + 1);
            notifyItemChanged(pos);
            listener.onCartChanged();
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Cartmanager.getInstance(holder.itemView.getContext()).removeItem(pos);
            notifyItemRemoved(pos);
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView      ivProduct;
        TextView       tvName, tvPrice, tvQuantity;
        MaterialButton btnMinus, btnPlus;
        ImageButton    btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ivProduct  = itemView.findViewById(R.id.iv_product);
            tvName     = itemView.findViewById(R.id.tv_name);
            tvPrice    = itemView.findViewById(R.id.tv_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnMinus   = itemView.findViewById(R.id.btn_minus);
            btnPlus    = itemView.findViewById(R.id.btn_plus);
            btnDelete  = itemView.findViewById(R.id.btn_delete);
        }
    }
}