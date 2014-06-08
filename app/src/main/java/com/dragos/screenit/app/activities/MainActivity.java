package com.dragos.screenit.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.dragos.androidfilepicker.library.Constants;
import com.dragos.androidfilepicker.library.ImagePickerActivity;
import com.dragos.screenit.app.R;
import com.dragos.screenit.app.server.Service;
import com.dragos.screenit.app.utils.AlertUtils;
import com.dragos.screenit.app.utils.SharedPreferencesUtils;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements IScanResultHandler{



    private BarcodeFragment mBarcodeScannerFragment;
    private static Handler mConnectionHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        if(SharedPreferencesUtils.isFirstLaunch(this)) {
            Intent tutorialIntent = new Intent(this, TutorialActivity.class);
            startActivity(tutorialIntent);
            return;
        }

        mBarcodeScannerFragment = (BarcodeFragment)getSupportFragmentManager().findFragmentById(R.id.scanFragment);
        mBarcodeScannerFragment.setScanResultHandler(this);
      //  mBarcodeScannerFragment.setDecodeFor(EnumSet.of(BarcodeFormat.QR_CODE));
        mBarcodeScannerFragment.setUserVisibleHint(true);

        mConnectionHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Service.CONNECTION_ACCEPTED:
                        startFilePicker();
                        setProgressBarIndeterminate(false);
                        break;
                    case Service.BAD_ID_ERROR:
                        AlertUtils.showErrorDialog(MainActivity.this, MainActivity.this.getString(R.string.bad_id_error));
                        break;
                    case Service.SERVER_UNREACHABLE_ERROR:
                        AlertUtils.showErrorDialog(MainActivity.this, MainActivity.this.getString(R.string.cannot_find_server));
                        break;
                    case Service.UNDEFINED_ERROR:
                        AlertUtils.showErrorDialog(MainActivity.this, MainActivity.this.getString(R.string.undefined_error));
                        break;
                }
            }
        };

    }



    public static Handler getHandler(){
        return mConnectionHandler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_help) {
            Intent tutorialIntent = new Intent(this, TutorialActivity.class);
            startActivity(tutorialIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void scanResult(ScanResult scanResult) {
        Log.w("DBG", "onScanResult");
        Log.w("DBG", "code scanned: " + scanResult.getRawResult().getText());
        setProgressBarIndeterminate(true);
        String browserId = scanResult.getRawResult().getText();
        Service.getInstance().connect(browserId);
        mBarcodeScannerFragment.restart();
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
                Intent slideshowIntent = new Intent(getBaseContext(), SlideshowActivity.class);
                slideshowIntent.putStringArrayListExtra("paths", paths);
                startActivity(slideshowIntent);

            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
