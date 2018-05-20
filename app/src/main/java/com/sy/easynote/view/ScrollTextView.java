package com.sy.easynote.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by ASUS User on 2018/5/6.
 */
@RemoteViews.RemoteView
public class ScrollTextView extends TextView{
    public ScrollTextView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setMovementMethod(new ScrollingMovementMethod());
    }

}
