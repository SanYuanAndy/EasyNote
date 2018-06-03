package com.sy.easynote.bean;

import android.text.TextUtils;
import android.util.Log;

import com.sy.easynote.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by ASUS User on 2018/5/12.
 */
public class PageData {
    private  final static String TAG = PageData.class.getSimpleName();
    private static int currPage = 0;
    private static final int PAGE_SIZE = 100;
    private static boolean isMorePaged = false;
    private static long note_id_constants = 0;

    private static List<EasyNoteHeader> sEasyNoteHeaders = new LinkedList<EasyNoteHeader>();

    private static String genNoteId(){
        String strNoteId = "";
        strNoteId = System.currentTimeMillis() + "" + note_id_constants++;
        return stringToMD5(strNoteId);
    }

    public static void init(){
        sEasyNoteHeaders.clear();
        loadConfig();
        Log.d(TAG, "size : " + sEasyNoteHeaders.size());
    }

    public  static List<EasyNoteHeader> getEasyNoteHeades(){
        return sEasyNoteHeaders;
    }

    private static String sCurrNoteId = null;

    public  static void setCurrNoteId(String strNoteId){
        if (TextUtils.equals(strNoteId, sCurrNoteId)){
            return;
        }

        currPage = 0;
        sCurrNoteId = strNoteId;
    }

    public static String getWidgetText(String strNoteId){
        String strPath = MyApplication.getApp().getFilesDir() + File.separator + strNoteId;
        String strWigdetText = readFile(strPath);
        return  strWigdetText;
    }

    public static String getCurrPage(){
        String currPageText = getWidgetText(sCurrNoteId).substring(currPage*PAGE_SIZE);
        return currPageText;
    }

    public static void nextPage(){
        String strCurrWidgetText = getWidgetText(sCurrNoteId);
        if (currPage*PAGE_SIZE + PAGE_SIZE >= strCurrWidgetText.length()){
            return;
        }
        currPage++;
    }

    public static void prePage(){
        if (currPage > 0) {
            currPage--;
        }
    }

    public static void morePage(){
        isMorePaged = !isMorePaged;
    }

    public static boolean isMorePage(){
        return  isMorePaged;
    }


    public  static interface DataChangeListener{
        public  void onChange();
    }

    private static Set<DataChangeListener> sDataChangeListeners = new HashSet<DataChangeListener>();

    public  static void addDataChangeListener(DataChangeListener listener){
        sDataChangeListeners.add(listener);
    }

    public  static void removeDataChangeListener(DataChangeListener listener){
        sDataChangeListeners.remove(listener);
    }

    private static void notifyAllListener(){
        Iterator<DataChangeListener> it = sDataChangeListeners.iterator();
        while(it.hasNext()){
            DataChangeListener listener = it.next();
            listener.onChange();
        }
    }

    private static void notifyDataChanged(){
        MyApplication.getApp().runWorkerThread(new Runnable() {
            @Override
            public void run() {
                notifyAllListener();
            }
        }, 0);
    }
    public static void deleteNote(EasyNoteHeader header){
        boolean bUpdated = true;
        EasyNoteHeader oldHeader = getEasyNoteHeader(header.getNoteId());
        if (oldHeader != null) {
            sEasyNoteHeaders.remove(oldHeader);
            saveConfig();
            File f = new File(MyApplication.getApp().getFilesDir() + File.separator + header.getNoteId());
            if (f.exists()){
                f.delete();
            }
            notifyDataChanged();
        }
    }

    public static void save(EasyNoteHeader header, String strContent){
        boolean bUpdated = true;
        EasyNoteHeader oldHeader = getEasyNoteHeader(header.getNoteId());
        if (oldHeader == null) {
            sEasyNoteHeaders.add(header);
        }

        String newEasyContent = "";
        do{
            if(TextUtils.isEmpty(strContent)){
                break;
            }

            if (strContent.length() < NoteConstants.EASY_CONTENT_MAX_LEN){
                newEasyContent = strContent;
            }else{
                newEasyContent = strContent.substring(0, NoteConstants.EASY_CONTENT_MAX_LEN - 1);
            }
        }while(false);

        header.setEasyContent(newEasyContent.replace("\n", " "));

        saveConfig();
        saveFile(MyApplication.getApp().getFilesDir() + File.separator + header.getNoteId(), strContent);

        if (bUpdated){
            notifyDataChanged();
        }
    }

    public static String readContent(EasyNoteHeader header){
        String strPath = MyApplication.getApp().getFilesDir() + File.separator + header.getNoteId();
        return readFile(strPath);
    }

    public static EasyNoteHeader getEasyNoteHeader(String strNoteId) {
        for (int i = 0; i < sEasyNoteHeaders.size(); ++i) {
            EasyNoteHeader oldHeader = sEasyNoteHeaders.get(i);
            if (TextUtils.equals(strNoteId, oldHeader.getNoteId())) {
                return oldHeader;
            }
        }
        return null;
    }


    public static EasyNoteHeader genEasyNoteHeader(){
        return new EasyNoteHeader(genNoteId());
    }



    private final static String CONFIG_PATH = MyApplication.getApp().getFilesDir() + File.separator + "config";
    public static void saveConfig() {
        JSONArray json = new JSONArray();
        for (EasyNoteHeader header : sEasyNoteHeaders) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(NoteConstants.EXTRA_NAME_NOTEID, header.getNoteId());
                obj.put(NoteConstants.EXTRA_NAME_TITLE, header.getTitle());
                obj.put(NoteConstants.EXTRA_NAME_EASYCONTENT, header.getEasyContent());
                json.put(obj);
            } catch (Exception e) {

            }
        }
        String strContent = json.toString();
        if (!TextUtils.isEmpty(strContent)){
            saveFile(CONFIG_PATH, strContent);
        }
    }

    private static void loadConfig() {
        Log.d(TAG, "loadConfig");
        String strText = readFile(CONFIG_PATH);
        JSONArray json = null;
        try {
            json = new JSONArray(strText);
            for (int i = 0; i < json.length(); ++i) {

                JSONObject obj = json.getJSONObject(i);
                if (obj == null) {
                    continue;
                }

                String strNoteId = null;
                try {
                    strNoteId = obj.getString(NoteConstants.EXTRA_NAME_NOTEID);
                } catch (Exception e) {

                }

                if (TextUtils.isEmpty(strNoteId)){
                    continue;
                }
                EasyNoteHeader header = new EasyNoteHeader(strNoteId);
                try {
                    header.setTitle(obj.getString(NoteConstants.EXTRA_NAME_TITLE));
                } catch (Exception e) {

                }

                try {
                    header.setEasyContent(obj.getString(NoteConstants.EXTRA_NAME_EASYCONTENT));
                } catch (Exception e) {

                }

                sEasyNoteHeaders.add(header);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




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
