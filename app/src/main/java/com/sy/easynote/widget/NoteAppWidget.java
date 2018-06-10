package com.sy.easynote.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.sy.easynote.R;
import com.sy.easynote.bean.EasyNoteRemoteViewService;
import com.sy.easynote.bean.NoteConstants;
import com.sy.easynote.bean.PageData;
import com.sy.easynote.ui.EditActivity;

import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class NoteAppWidget extends AppWidgetProvider {
    private final static String TAG = NoteAppWidget.class.getSimpleName();

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(TAG, "updateAppWidget : " + appWidgetId);
        Bundle widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
        //printWidgetOptions(widgetOptions);

        CharSequence widgetText = PageData.getWigdetText();
        CharSequence widgetTitle = PageData.getWigdetTitle();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_app_widget);

        // 把这个Widget绑定到RemoteViewsService
        Intent intent = new Intent(context, EasyNoteRemoteViewService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // 设置适配器
        views.setRemoteAdapter(R.id.more_notes_lv, intent);

        views.setTextViewText(R.id.page_content_tv, widgetText);
        views.setTextViewText(R.id.page_title_tv, widgetTitle);

        int visibleValue = PageData.isMorePage() ? View.VISIBLE : View.INVISIBLE;
        views.setInt(R.id.more_notes_layer, "setVisibility", visibleValue);

        Intent preIntent = new Intent(context, NoteAppWidget.class);
        preIntent.setAction(NoteConstants.ACTION_PRE_PAGE_BTN_ONCLICK);
        PendingIntent prePendingIntent = PendingIntent.getBroadcast(context, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.pre_page_btn, prePendingIntent);

        Intent nextIntent = new Intent(context, NoteAppWidget.class);
        nextIntent.setAction(NoteConstants.ACTION_NEXT_PAGE_BTN_ONCLICK);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.next_page_btn, nextPendingIntent);

        Intent moreIntent = new Intent(context, NoteAppWidget.class);
        moreIntent.setAction(NoteConstants.ACTION_MORE_PAGE_BTN_ONCLICK);
        PendingIntent morePendingIntent = PendingIntent.getBroadcast(context, 0, moreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.more_page_btn, morePendingIntent);

        {
            Intent editIntent = new Intent(context, NoteAppWidget.class);
            editIntent.setAction(NoteConstants.ACTION_EDIT_PAGE_BTN_ONCLICK);
            PendingIntent editPendingIntent = PendingIntent.getBroadcast(context, 0, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.edit_page_btn, editPendingIntent);
        }

        Intent itemIntent = new Intent(context, NoteAppWidget.class);
        itemIntent.setAction(NoteConstants.ACTION_ITEM_LV_ONCLICK);
        itemIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));//添加这个，才能携带参数
        PendingIntent itemPendingIntent = PendingIntent.getBroadcast(context, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.more_notes_lv, itemPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "onUpdate : " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(final Context context) {
        Log.d(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
        PageData.addDataChangeListener(new PageData.DataChangeListener() {
            @Override
            public void onChange() {
                notifyDataChanged(context);
                PageData.checkNotePage();
                update(context);
            }
        });
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive :  " + intent.getAction());
        super.onReceive(context, intent);
        if (TextUtils.equals(NoteConstants.ACTION_PRE_PAGE_BTN_ONCLICK, intent.getAction())){
            changePage(context, NoteConstants.PRE_PAGE_BTN_CMD);
        }else if (TextUtils.equals(NoteConstants.ACTION_NEXT_PAGE_BTN_ONCLICK, intent.getAction())){
            changePage(context, NoteConstants.NEXT_PAGE_BTN_CMD);
        }else if (TextUtils.equals(NoteConstants.ACTION_MORE_PAGE_BTN_ONCLICK, intent.getAction())){
                changePage(context, NoteConstants.MORE_PAGE_BTN_CMD);
        }else if (TextUtils.equals(NoteConstants.ACTION_EDIT_PAGE_BTN_ONCLICK, intent.getAction())){
            changePage(context, NoteConstants.EDIT_PAGE_BTN_CMD);
        }else if (TextUtils.equals(NoteConstants.ACTION_ITEM_LV_ONCLICK, intent.getAction())){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int itemid = bundle.getInt(NoteConstants.EXTRA_NAME_ITEMID, -1);
                String noteid = bundle.getString(NoteConstants.EXTRA_NAME_NOTEID, "0");
                Log.d(TAG, "onReceive itemId :  " + itemid);
                Log.d(TAG, "onReceive noteId :  " + noteid);
                if (itemid > -1 && !TextUtils.isEmpty(noteid)){
                    selectNote(context, itemid, noteid);
                }
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(TAG, "onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    private void printWidgetOptions(Bundle bundle){
        Set<String> keyset = bundle.keySet();
        for (String key : keyset){
            Log.d(TAG, "onAppWidgetOptionsChanged " + key + " : " + bundle.get(key));
        }
    }

    private void changePage(Context context, int cmd){
        switch (cmd){
            case NoteConstants.PRE_PAGE_BTN_CMD:
                PageData.prePage();
                break;
            case NoteConstants.NEXT_PAGE_BTN_CMD:
                PageData.nextPage();
                break;
            case NoteConstants.MORE_PAGE_BTN_CMD:
                PageData.morePage();
                break;
            case NoteConstants.EDIT_PAGE_BTN_CMD:
                goEdit(context, PageData.getCurrNoteId());
                break;
            default:
                return;
        }

        update(context);

    }

    private void selectNote(Context context, int itemid, String noteid){
        PageData.setCurrNoteId(noteid);
        PageData.morePage();
        update(context);
    }

    private void update(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, NoteAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "onUpdate : " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void notifyDataChanged(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, NoteAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.more_notes_lv);
    }


    private void goEdit(Context context, String strNoteId){
        Intent intent = new Intent(context, EditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.EXTRA_NAME_NOTEID, strNoteId);
        intent.putExtra("data", bundle);
        try {
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

