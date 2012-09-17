package com.lnu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Random extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        
       final TextView newRandomResultText = (TextView)findViewById(R.id.newRandomResultText);
       newRandomResultText.setText("100");
       Button newRandomButton = (Button)findViewById(R.id.newRandomButton);
       newRandomButton.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			java.util.Random random = new java.util.Random();
			int nextInt = random.nextInt(100)+1;
			newRandomResultText.setText(nextInt+"");
		}
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_random, menu);
        return true;
    }
}
