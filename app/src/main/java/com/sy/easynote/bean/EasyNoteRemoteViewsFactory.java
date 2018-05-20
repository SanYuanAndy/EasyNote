package com.sy.easynote.bean;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sy.easynote.R;
import com.sy.easynote.widget.NoteAppWidget;

import java.util.List;

/**
 * Created by ASUS User on 2018/5/13.
 */
public class EasyNoteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = EasyNoteRemoteViewsFactory.class.getSimpleName();
    private final static int VIEW_TYPE_COUNT = 1;
    private Context mContext = null;
    private List<EasyNoteHeader> mData = null;

    public  EasyNoteRemoteViewsFactory(Context context, List<EasyNoteHeader> data){
        mContext = context;
        mData = data;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews view = null;
        do {
            if (mData == null){
                break;
            }
            if (mData.isEmpty()){
                break;
            }
            if (i < 0 || i > mData.size() - 1){
                break;
            }

            EasyNoteHeader header = mData.get(i);
            if (header == null){
                break;
            }

            view = new RemoteViews(mContext.getPackageName(), R.layout.more_note_lv_item);
            view.setTextViewText(R.id.notes_lv_item_title, header.getTitle());
            view.setTextViewText(R.id.notes_lv_item_easy_text, header.getEasyContent());

            Intent itemIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(NoteConstants.EXTRA_NAME_ITEMID, i);
            bundle.putInt(NoteConstants.EXTRA_NAME_NOTEID, header.getNoteId());

            itemIntent.putExtras(bundle);

            view.setOnClickFillInIntent(R.id.note_lv_item, itemIntent);

        }while(false);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
