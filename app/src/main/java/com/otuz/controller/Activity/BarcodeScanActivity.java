package com.otuz.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;
import com.otuz.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Makes barcode scans via device camera.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class BarcodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        scannerView = (ZXingScannerView) findViewById(R.id.scanner_view);
        scannerView.startCamera();
        scannerView.setResultHandler(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Start camera on onResume.
        scannerView.resumeCameraPreview(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on onPause.
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Return the barcode number to caller Activity.
        Intent returnIntent = new Intent();
        returnIntent.putExtra("barcode_number", rawResult.getText());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        // Return RESULT_CANCELED to caller Activity when back button pressed.
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
