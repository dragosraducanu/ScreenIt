package com.dragos.screenit.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.dragos.androidfilepicker.library.core.ImageSize;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dragos on 29.05.2014.
 */
public class ImageUtils {
    public static final String IMAGES_FOLDER_NAME = "ScreenIt";

    public static String resizeImageAndSave(String path, ImageSize newSize, Context context){
        Bitmap newFileBitmap = null;
        try {
            newFileBitmap = Picasso.with(context).load(new File(path)).resize(newSize.getWidth(), newSize.getHeight()).centerInside().get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String destPath = getImagesLocation(context) + (new File(path).getName());
        new File(getImagesLocation(context)).mkdirs();
        writeBitmapToFile(destPath, newFileBitmap, 90);
        return destPath;
    }

    public static void writeBitmapToFile(String destination, Bitmap sourceBitmap, int compression){
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(destination);
            sourceBitmap.compress(Bitmap.CompressFormat.PNG, compression, out);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static String getImagesLocation(Context context){
        return context.getFilesDir() + "/" + IMAGES_FOLDER_NAME +"/";
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, result);

        intrinsicBlur.setRadius(25.f);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(result);
        rs.destroy();
        return result;
    }
}
