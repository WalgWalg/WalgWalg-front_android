package com.example.walgwalg_front_android;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FontAwesomeTextLight extends androidx.appcompat.widget.AppCompatTextView {
    public FontAwesomeTextLight(Context context){
        super(context);
        init();
    }
    public FontAwesomeTextLight(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    public FontAwesomeTextLight(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fa-solid-900.ttf");
        setTypeface(typeface);
    }
}
