package com.alfonsochap.bqdropboxapp.app.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Alfonso on 15/12/2015.
 */
public class Util {

    public static String createFileTmp(Context context, Uri uri, String fileName){
        String path = context.getFilesDir().getAbsolutePath() + "/tmp/";
        InputStream is = null;
        OutputStream os = null;
        try{
            // Creating tmp dir if not exists
            File f = new File(path);
            f.mkdirs();

            // Writing uri data in tmp file
            f = new File(path, fileName);
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        }catch(Exception e){
            Log.v("tag", "Error: " + e.getMessage());
        }
        finally{
            if(is != null){
                try{
                    is.close();
                }catch(Exception e){}
            }
            if(os != null){
                try{
                    os.close();
                }catch(Exception e){}
            }
        }
        return path;
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
