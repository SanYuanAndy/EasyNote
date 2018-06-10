package com.sy.easynote.bean;

import android.text.TextUtils;

import com.sy.easynote.MyApplication;

import java.io.File;

/**
 * Created by ASUS User on 2018/6/3.
 */
public class NotePage{
    private static final int PAGE_SIZE = 100;
    private String mNoteId;
    private int mCurrPage;
    private String mNoteFullText = "";

    public void setNoteId(String strNoteId){
        mNoteId = strNoteId;
        String strPath = MyApplication.getApp().getFilesDir() + File.separator + strNoteId;
        mNoteFullText = FileUtils.readFile(strPath);
        clear();
    }

    private void clear(){
        mCurrPage = 0;
    }

    public void prePage(){
        if (mCurrPage > 0) {
            mCurrPage--;
        }
    }

    public void nextPage(){
        if (mCurrPage*PAGE_SIZE + PAGE_SIZE >= mNoteFullText.length()){
            return;
        }
        mCurrPage++;
    }

    public  String getText(){
        String sText = mNoteFullText.substring(mCurrPage*PAGE_SIZE);
        return sText;
    }

    public  String getNoteId(){
        return mNoteId;
    }

    public boolean isValid(){
        boolean bRet = false;
        do{
            if (TextUtils.isEmpty(mNoteId)){
                break;
            }

            String strPath = MyApplication.getApp().getFilesDir() + File.separator + mNoteId;
            File f = new File(strPath);
            if (!f.exists()){
                break;
            }

            bRet = true;
        }while(false);
        return bRet;
    }

}
