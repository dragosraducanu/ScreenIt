package com.dragos.screenit.app.server;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.dragos.androidfilepicker.library.core.ImageSize;
import com.dragos.screenit.app.activities.SlideshowActivity;
import com.dragos.screenit.app.utils.ImageUtils;
import com.dragos.screenit.app.utils.PreferencesUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 *Created by Raducanu Dragos (raducanu.dragos@gmail.com) on 29.05.2014.
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


    public String uploadImage(String path){
        ImageSize browserWindowSize = Service.getInstance().getBrowserWindowSize();
        //resize image to fit browser window
        String imgPath = path;
        if(PreferencesUtils.getOptimizeImages(mContext)) {
            imgPath = ImageUtils.resizeImageAndSave(path, browserWindowSize, mContext);
        }

        String imgName = getFileName(imgPath);
        PutObjectRequest por = new PutObjectRequest(S3_BUCKET, imgName, new File(imgPath));
        mS3Client.putObject(por);

        return getUploadedFileUrl(imgPath);
    }

    public void startBatchAsyncUpload(ArrayList<String> paths, Context context){
       BatchUploaderTask uploaderTask = new BatchUploaderTask();
        uploaderTask.setContext(context);
        uploaderTask.execute(paths);
    }

    private class BatchUploaderTask extends AsyncTask<ArrayList<String>, String, Void> {
        private Context context;
        public void setContext(Context _context){
            this.context = _context;
        }
        @Override
        protected void onPreExecute(){
           SlideshowActivity.setProgressBarVisibility(View.VISIBLE);
           super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<String>... paths) {
            for (String image : paths[0]) {
                String uploadedURL = uploadImage(image);
                publishProgress(uploadedURL);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... url){
            Service.getInstance().sendImagePathToServer(url[0]);
            SlideshowActivity.incrementProgress();
        }
        @Override
        protected void onPostExecute(Void res){
            super.onPostExecute(res);
            SlideshowActivity.setProgressBarVisibility(View.GONE);
        }

    }

    private String getFileName(String path){
        String filename = "";
        File f = new File(path);
        filename = f.getName();
        return filename;
    }
}
