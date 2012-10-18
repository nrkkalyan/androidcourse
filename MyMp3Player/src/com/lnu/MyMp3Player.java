package com.lnu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyMp3Player extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mp3player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_mp3player, menu);
        return true;
    }
}
