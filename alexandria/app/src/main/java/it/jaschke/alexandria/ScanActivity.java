package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Felipe Berbert on 09/12/2015.
 */
public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler  {
    private static final String TAG = "SCAN_ACTIVITY";
    public static final String PARAM_BARCODE_CONTENT = "BARCODE_CONTENT";




    private ZXingScannerView scannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(PARAM_BARCODE_CONTENT, result.getText());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
