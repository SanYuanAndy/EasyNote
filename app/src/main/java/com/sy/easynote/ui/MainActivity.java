package com.sy.easynote.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sy.easynote.MyApplication;
import com.sy.easynote.R;
import com.sy.easynote.bean.EasyNoteHeader;
import com.sy.easynote.bean.NoteAdapter;
import com.sy.easynote.bean.NoteConstants;
import com.sy.easynote.bean.PageData;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private ListView mNoteListLV = null;
    private NoteAdapter mNoteAdapter = null;
    private Button mAddBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mNoteListLV = (ListView)findViewById(R.id.note_list_lv);
        mNoteAdapter = new NoteAdapter(this, PageData.getEasyNoteHeades());
        mNoteListLV.setAdapter(mNoteAdapter);
        mNoteListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goEdit(i);
            }
        });

        mNoteListLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteItem(i);
                return true;
            }
        });

        mAddBtn = (Button)findViewById(R.id.add_note_btn);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditActivity("");
            }
        });

        PageData.addDataChangeListener(new PageData.DataChangeListener() {
            @Override
            public void onChange() {
                MyApplication.getApp().runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoteAdapter.notifyDataSetChanged();
                    }
                }, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    public void startEditActivity(String strNoteId){
        Intent intent = new Intent(this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NoteConstants.EXTRA_NAME_NOTEID, strNoteId);
        intent.putExtra("data", bundle);
        try {
            startActivity(intent);
        }catch (Exception e){

        }
    }

    public  void goEdit(int index){
        EasyNoteHeader easyNoteHeader = PageData.getEasyNoteHeades().get(index);
        startEditActivity(easyNoteHeader.getNoteId());
    }

    public  void deleteItem(int index){
        final EasyNoteHeader easyNoteHeader = PageData.getEasyNoteHeades().get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要删除" + easyNoteHeader.getTitle() + "?");
        builder.setMessage(easyNoteHeader.getEasyContent());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PageData.deleteNote(easyNoteHeader);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
