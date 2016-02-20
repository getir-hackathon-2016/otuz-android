package com.otuz.controller.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.otuz.R;
import com.otuz.adapter.ProductRecyclerViewAdapter;
import com.otuz.model.ProductModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Launcher Activity.
 * Displays user's shopping cart.
 */
public class ShoppingCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.user_cart_title));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<ProductModel> mockList = new ArrayList<ProductModel>();
        for(int i=0; i<25; i++){

            ProductModel productModel = new ProductModel();
            productModel.setBarcodeNumber("525252");
            productModel.setName("deterjan");
            productModel.setPhotoUrl("fbvwejbvwl");
            productModel.setPrice("80");
            productModel.setQuantity("2");
            mockList.add(productModel);

        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ProductRecyclerViewAdapter(mockList, R.layout.row_cart_product));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

}