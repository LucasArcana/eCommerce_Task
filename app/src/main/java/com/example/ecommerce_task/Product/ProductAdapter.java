package com.example.ecommerce_task.Product;

import android.graphics.Color;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = products.get(position);

        holder.tvName.setText(p.getName());

        holder.tvSeries.setText(p.getType());

        holder.tvPrice.setText(p.getFormattedPrice());

        String imageUrl = p.getFirstImage();
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(new ColorDrawable(Color.WHITE))
                    .error(new ColorDrawable(Color.GRAY))
                    .centerCrop()
                    .into(holder.ivProduct);
        }
        String badge = p.getBadge();
        if (badge != null) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText(badge);
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        int offsetPx = position % 2 == 1
                ? (int) (32 * holder.itemView.getContext().getResources().getDisplayMetrics().density)
                : 0;
        params.topMargin = offsetPx;
        holder.itemView.setLayoutParams(params);


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onProductClick(p);
        });
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private OnProductClickListener listener;

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvSeries, tvPrice, tvBadge;

        ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvName    = itemView.findViewById(R.id.tv_name);
            tvSeries  = itemView.findViewById(R.id.tv_series);
            tvPrice   = itemView.findViewById(R.id.tv_price);
            tvBadge   = itemView.findViewById(R.id.tv_badge);
        }
    }
}