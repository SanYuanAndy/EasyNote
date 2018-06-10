package com.sy.easynote.bean;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;

/**
 * Created by ASUS User on 2018/6/3.
 */
public class FileUtils {
    private final static String TAG = FileUtils.class.getSimpleName();

    public static void saveFile(String strPath, String strContent){
        File f = new File(strPath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f));
            writer.write(strContent);
            writer.close();
        } catch (Exception e) {

        }
    }

    public static String readFile(String strPath){
        File f = new File(strPath);
        if (!f.exists()) {
            Log.d(TAG, f.getPath() + " no exist");
            return "";
        }
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(f));
            while (true) {
                String s = reader.readLine();
                Log.d(TAG, "line:" + s);
                if (s == null) {
                    break;
                }
                sb.append(s);
                sb.append("\n");
            }
        } catch (Exception e) {
            return "";
        }

        if (reader != null){
            try {
                reader.close();
            }catch (Exception e){

            }
        }

        return sb.toString();
    }


    public static String stringToMD5(String string) {
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes());
        } catch (Exception e) {

        }


        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
