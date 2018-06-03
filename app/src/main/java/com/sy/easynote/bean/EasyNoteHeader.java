package com.sy.easynote.bean;

/**
 * Created by ASUS User on 2018/5/13.
 */
public class EasyNoteHeader {
    private String mTitle = null;
    private String mEasyContent = null;
    private String mId = "0";

    public  EasyNoteHeader(String strNoteId){
        mId = strNoteId;
    }

    public EasyNoteHeader setTitle(String strTitle){
        mTitle = strTitle;
        return this;
    }

    public EasyNoteHeader setEasyContent(String strText){
        mEasyContent = strText;
        return this;
    }

    public String getTitle(){
        return mTitle;
    }

    public  String getEasyContent(){
        return mEasyContent;
    }

    public String getNoteId(){
        return mId;
    }
}
