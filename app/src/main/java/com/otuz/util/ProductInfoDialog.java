package com.otuz.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.otuz.R;
import com.otuz.listener.ProductConfirmListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
@SuppressLint("ValidFragment")
public class ProductInfoDialog extends DialogFragment {

    private ProductConfirmListener productConfirmListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder   = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater       = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view                     = inflater.inflate(getActivity().getResources().getLayout(R.layout.dialog_product_info), null);
        final ImageView productImage        = (ImageView)view.findViewById(R.id.product_image);

        final String photoUrl = getArguments().getString("photo_url");
        Picasso.with(getActivity())
                .load(photoUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(productImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity())
                                .load(photoUrl)
                                .error(R.drawable.gecici_harita)
                                .into(productImage, new Callback() {
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

        builder.setPositiveButton(getResources().getString(R.string.positive_1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productConfirmListener.OnProductConfirmed();
                getDialog().dismiss();
            }
        }).setNegativeButton(getResources().getString(R.string.negative_1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();

    }

    public void setProductConfirmListener(ProductConfirmListener productConfirmListener){
        this.productConfirmListener = productConfirmListener;
    }

}
