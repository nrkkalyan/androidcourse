package com.lnu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ColorDisplay extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_color_display, menu);
        return true;
    }
}
