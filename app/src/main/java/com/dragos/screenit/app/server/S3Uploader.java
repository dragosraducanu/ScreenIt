package com.dragos.screenit.app.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.dragos.androidfilepicker.library.core.ImageSize;
import com.dragos.screenit.app.utils.ImageUtils;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Created by dragos on 29.05.2014.
 */
public class S3Uploader {
    public static String S3_BUCKET = "screenit-eu-ireland-1";
    private AmazonS3Client mS3Client;
    private Context mContext;

    public S3Uploader(Context context, String access_key, String secret_key){
        this.mContext = context;
        initAmazonS3Client(access_key, secret_key);
    }

    private void initAmazonS3Client(String access_key, String secret_key){
        mS3Client = new AmazonS3Client(new BasicAWSCredentials(access_key, secret_key));
    }

    private String getUploadedFileUrl(String imgPath){

        String imgName = getFileName(imgPath);

        ResponseHeaderOverrides override = new ResponseHeaderOverrides();
        override.setContentType( "image/jpeg" );

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( S3_BUCKET, imgName );
        urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
        urlRequest.setResponseHeaders( override );

        URL url = mS3Client.generatePresignedUrl( urlRequest );
        return url.toString();
    }


    public void startAsyncUpload(String imgPath){
        new UploaderTask().execute(imgPath);
    }

    private class UploaderTask extends AsyncTask<String, Void, String> {
        private String mUploadedImageUrl = "";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... paths) {
            ImageSize browserWindowSize = Service.getInstance().getBrowserWindowSize();
            //resize image to fit browser window
            String imgPath = ImageUtils.resizeImageAndSave(paths[0], browserWindowSize, mContext);

            String imgName = getFileName(imgPath);
            PutObjectRequest por = new PutObjectRequest(S3_BUCKET, imgName, new File(imgPath));
            mS3Client.putObject(por);

            return getUploadedFileUrl(imgPath);
        }
        @Override
        protected void onPostExecute(String res){
            super.onPostExecute(res);
            Service.getInstance().sendImagePathToServer(res);
            Log.w("service", "image uploaded to: " + res);
            Log.w("service", "image should be on page!!");
        }

    }

    private String getFileName(String path){
        String filename = "";
        File f = new File(path);
        filename = f.getName();
        return filename;
    }
}
