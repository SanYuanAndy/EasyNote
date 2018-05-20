package com.sy.easynote.bean;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sy.easynote.MyApplication;

public class EasyNoteRemoteViewService extends RemoteViewsService {
    private final static String TAG = EasyNoteRemoteViewService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EasyNoteRemoteViewsFactory(this.getApplicationContext(), PageData.getEasyNoteHeades());
    }

}
