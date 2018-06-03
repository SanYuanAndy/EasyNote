package com.sy.easynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sy.easynote.MyApplication;
import com.sy.easynote.R;
import com.sy.easynote.bean.EasyNoteHeader;
import com.sy.easynote.bean.NoteConstants;
import com.sy.easynote.bean.PageData;

/**
 * Created by ASUS User on 2018/5/20.
 */
public class EditActivity extends Activity{
    private final static String TAG = EditActivity.class.getSimpleName();
    private EditText mTitleETV = null;
    private EditText mContentETV = null;
    private Button mBackBtn = null;
    private EasyNoteHeader mHeader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        parseIntentParams();
        init();
    }

    private void parseIntentParams(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String noteId = bundle.getString(NoteConstants.EXTRA_NAME_NOTEID);
        Log.d(TAG, NoteConstants.EXTRA_NAME_NOTEID + ":" + noteId);

        if (TextUtils.isEmpty(noteId)){
            mHeader = PageData.genEasyNoteHeader();
        }else{
            mHeader = PageData.getEasyNoteHeader(noteId);
        }
    }

    private void init(){
        mTitleETV = (EditText)findViewById(R.id.edit_title_etv);
        if (!TextUtils.isEmpty(mHeader.getTitle())){
            mTitleETV.setText(mHeader.getTitle());
        }
        String strContent = PageData.readContent(mHeader);
        mContentETV = (EditText)findViewById(R.id.edit_content_etv);
        mContentETV.setText(strContent);
        mBackBtn = (Button)findViewById(R.id.edit_back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
        Log.d(TAG, "onStop");
    }

    private void save(){
        String strTitle = mTitleETV.getText().toString();
        String strContent = mContentETV.getText().toString();
        if (TextUtils.isEmpty(strTitle) && TextUtils.isEmpty(strContent)){
            Log.d(TAG, "内容不能为空");
            return;
        }

        mHeader.setTitle(strTitle);
        PageData.save(mHeader, strContent);
    }

}
