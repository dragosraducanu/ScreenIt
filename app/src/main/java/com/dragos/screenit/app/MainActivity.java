package com.dragos.screenit.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.dragos.androidfilepicker.library.Constants;
import com.dragos.androidfilepicker.library.ImagePickerActivity;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.EnumSet;


public class MainActivity extends FragmentActivity implements IScanResultHandler{
    BarcodeFragment mBarcodeScannerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Intent tutorialIntent = new Intent(this, TutorialActivity.class);
        startActivity(tutorialIntent);*/
        mBarcodeScannerFragment = (BarcodeFragment)getSupportFragmentManager().findFragmentById(R.id.scanFragment);
        mBarcodeScannerFragment.setScanResultHandler(this);
        mBarcodeScannerFragment.setDecodeFor(EnumSet.of(BarcodeFormat.QR_CODE));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void scanResult(ScanResult scanResult) {
        Toast.makeText(this, scanResult.getRawResult().getText(), Toast.LENGTH_LONG).show();
        startFilePicker();
    }

    private void startFilePicker(){
        Intent filePickerIntent = new Intent(getBaseContext(), ImagePickerActivity.class);
        startActivityForResult(filePickerIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                ArrayList<String> paths = data.getStringArrayListExtra(Constants.IMAGE_PICKER_PATHS_EXTRA_KEY);
                for(String p : paths) {
                    Log.w("paths", p);
                }
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
