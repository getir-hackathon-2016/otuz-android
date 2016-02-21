package com.otuz.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.otuz.R;
import com.otuz.controller.BaseApplication;
import com.otuz.dao.IUserDAO;
import com.otuz.dao.UserDAOImpl;
import com.otuz.listener.UserProductQuantityChangeListener;
import com.otuz.model.DAOResponse;
import com.otuz.model.ProductModel;
import com.otuz.model.UserModel;
import com.otuz.util.APIErrorCodeHandler;
import com.otuz.util.HttpFailStatusCodeHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
    private Context context;
    private UserProductQuantityChangeListener userProductQuantityChangeListener;

    /**
     * Constructor of adapter.
     * @param products Shopping cart product which will be listed.
     * @param itemRow Identifier for infilating row layout for every product.
     */
    public ProductRecyclerViewAdapter(List<ProductModel> products, int itemRow, Context context, UserProductQuantityChangeListener userProductQuantityChangeListener) {
        this.products   = products;
        this.itemRow    = itemRow;
        this.context    = context;
        this.userProductQuantityChangeListener = userProductQuantityChangeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemRow, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductModel productItem = products.get(position);
        holder.productName.setText(productItem.getName());
        holder.productPrice.setText(context.getResources().getString(R.string.product_price, productItem.getPrice()));
        holder.productCount.setText(productItem.getQuantity());
        final String photoUrl = productItem.getPhotoUrl();
        final ImageView productPhotoImageView = holder.productImage;
        Picasso.with(context)
                .load(photoUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(productPhotoImageView, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(photoUrl)
                                .error(R.drawable.error_placeholder)
                                .placeholder(R.drawable.placeholder)
                                .into(productPhotoImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.e("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
        holder.quantityIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.valueOf(products.get(position).getQuantity());
                currentQuantity++;
                products.get(position).setQuantity(currentQuantity + "");
                updateProductQuantity(products.get(position));
            }
        });
        holder.quantityDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.valueOf(products.get(position).getQuantity());
                // Preventing minus quantities.
                if(currentQuantity > 0) {
                    currentQuantity--;
                    products.get(position).setQuantity(currentQuantity + "");
                    updateProductQuantity(products.get(position));
                }
            }
        });
        holder.itemView.setTag(productItem);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView productName, productCount, productPrice;
        public Button quantityIncreaseButton, quantityDecreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage            = (ImageView) itemView.findViewById(R.id.product_image);
            productName             = (TextView) itemView.findViewById(R.id.product_name);
            productPrice            = (TextView) itemView.findViewById(R.id.product_price);
            productCount            = (TextView) itemView.findViewById(R.id.product_count);
            quantityIncreaseButton  = (Button) itemView.findViewById(R.id.quantity_increase_button);
            quantityDecreaseButton  = (Button) itemView.findViewById(R.id.quantity_decrease_button);
        }

    }

    /**
     * Updated product count quantity.
     * @param productModel
     */
    private void updateProductQuantity(final ProductModel productModel){

        final Handler serverRequestHandler = new Handler();
        Thread updateUserProductQuantityThread = new Thread(new Runnable() {
            @Override
            public void run() {

                IUserDAO userDAO = new UserDAOImpl();
                final DAOResponse daoResponse = userDAO.updateUserProductQuantity(
                        productModel.getProductId(),
                        BaseApplication.getUserModel().getFacebookUserId(),
                        productModel.getQuantity()
                );

                // Handling server response.
                serverRequestHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (daoResponse.getError().getErrorCode() == 0) {
                            // Success.
                            UserModel userModel = (UserModel) daoResponse.getObject();

                            // Store User data locally but temporary.
                            BaseApplication.setUserModel(userModel);

                            userProductQuantityChangeListener.onChangeOccurs();

                        } else {
                            // Check if the error code is a Http status code.
                            HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(context);
                            if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(context);
                                apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                            }

                        }

                    }
                });
            }
        });
        updateUserProductQuantityThread.start();

    }

}