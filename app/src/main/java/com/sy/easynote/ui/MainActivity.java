package com.sy.easynote.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.sy.easynote.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
     TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.main_content_tv);
//        textView.setVisibility(View.VISIBLE);
//        textView.setVisibility(View.INVISIBLE);
        //textView.setMovementMethod(new ScrollingMovementMethod());

    }
}
