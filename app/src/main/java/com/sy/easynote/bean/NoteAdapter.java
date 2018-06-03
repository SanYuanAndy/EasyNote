package com.sy.easynote.bean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sy.easynote.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS User on 2018/5/20.
 */
public class NoteAdapter extends BaseAdapter{
    private final static String TAG = "NoteAdapter";
    private Context mContext = null;
    private List<EasyNoteHeader> mData = null;

    public NoteAdapter(Context context, List<EasyNoteHeader> data){
        mContext = context;
        mData = data;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_note_lv_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTitleTV = (TextView)convertView.findViewById(R.id.notes_lv_item_title);
            viewHolder.mEasyContentTV = (TextView)convertView.findViewById(R.id.notes_lv_item_easy_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        EasyNoteHeader easyNoteHeader = mData.get(i);
        viewHolder.mTitleTV.setText(easyNoteHeader.getTitle());
        viewHolder.mEasyContentTV.setText(easyNoteHeader.getEasyContent());

        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return mData == null ? null : mData.get(i);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ViewHolder{
        TextView mTitleTV = null;
        TextView mEasyContentTV = null;
    }


}
