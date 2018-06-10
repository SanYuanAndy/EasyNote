package com.sy.easynote.bean;

import android.text.TextUtils;
import android.util.Log;
import com.sy.easynote.MyApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
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
    private static boolean isMorePaged = false;
    private static long note_id_constants = 0;
    private static NotePage sNotePage = null;

    private static List<EasyNoteHeader> sEasyNoteHeaders = new LinkedList<EasyNoteHeader>();


    private static String genNoteId(){
        String strNoteId = "";
        strNoteId = System.currentTimeMillis() + "" + note_id_constants++;
        return FileUtils.stringToMD5(strNoteId);
    }

    public static void init(){
        sEasyNoteHeaders.clear();
        loadConfig();
        Log.d(TAG, "size : " + sEasyNoteHeaders.size());
        sNotePage = new NotePage();
        checkNotePage();
    }

    public static void checkNotePage(){
        if (sNotePage.isValid()){
            return;
        }
        if (!sEasyNoteHeaders.isEmpty()){
            EasyNoteHeader header = sEasyNoteHeaders.get(0);
            if (header != null){
                sNotePage.setNoteId(header.getNoteId());
            }
        }
    }

    public  static List<EasyNoteHeader> getEasyNoteHeades(){
        return sEasyNoteHeaders;
    }

    public static String getCurrNoteId(){
        return sNotePage.getNoteId();
    }

    public static String getWigdetText(){
        return sNotePage.getText();
    }

    public static String getWigdetTitle(){
        String strTitle = "";
        EasyNoteHeader header = getEasyNoteHeader(sNotePage.getNoteId());
        if (header != null){
            strTitle = header.getTitle();
        }
        return strTitle;
    }

    public  static void setCurrNoteId(String strNoteId){
        sNotePage.setNoteId(strNoteId);
    }

    public static void nextPage(){
        sNotePage.nextPage();
    }

    public static void prePage(){
        sNotePage.prePage();
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
        FileUtils.saveFile(MyApplication.getApp().getFilesDir() + File.separator + header.getNoteId(), strContent);

        if (bUpdated){
            notifyDataChanged();
        }
    }

    public static String readContent(EasyNoteHeader header){
        String strPath = MyApplication.getApp().getFilesDir() + File.separator + header.getNoteId();
        return FileUtils.readFile(strPath);
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
            FileUtils.saveFile(CONFIG_PATH, strContent);
        }
    }

    private static void loadConfig() {
        Log.d(TAG, "loadConfig");
        String strText = FileUtils.readFile(CONFIG_PATH);
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
}
