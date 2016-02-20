package com.otuz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.otuz.R;
import com.otuz.model.ProductModel;
import java.util.List;

/**
 * Adapter for RecyclerView which holds user's shopping cart products.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    // List of products.
    private List<ProductModel> products;
    // Identifier of a layout which will be inflated.
    private int itemRow;

    /**
     * Constructor of adapter.
     * @param products Shopping cart product which will be listed.
     * @param itemRow Identifier for infilating row layout for every product.
     */
    public ProductRecyclerViewAdapter(List<ProductModel> products, int itemRow) {
        this.products   = products;
        this.itemRow    = itemRow;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemRow, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductModel productItem = products.get(position);
        holder.productName.setText(productItem.getName());
        holder.productCount.setText(productItem.getQuantity());
        //holder.image.setImageBitmap(null);
        //Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        //Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
        holder.itemView.setTag(productItem);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView productName, productCount;
        public Button quantityIncreaseButton, quantityDecreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage            = (ImageView) itemView.findViewById(R.id.product_image);
            productName             = (TextView) itemView.findViewById(R.id.product_name);
            productCount            = (TextView) itemView.findViewById(R.id.product_count);
            quantityIncreaseButton  = (Button) itemView.findViewById(R.id.quantity_increase_button);
            quantityDecreaseButton  = (Button) itemView.findViewById(R.id.quantity_decrease_button);
        }

    }

}