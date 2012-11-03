package dv106.lecture4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BroadcastIntents extends Activity {
	private Activity main_activity;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcasting);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.broadcast_button);
        button.setOnClickListener(new ButtonClick());  
    }
    
    private static final String ACTION = "dv106.lecture4.BROADCAST";
    private class ButtonClick implements View.OnClickListener {
    	int count = 1;
    	public void onClick(View v) {
    		String msg = "Message no. "+(count++);
    		
    		/* Broadcast intent */
    		Intent intent = new Intent(ACTION);
    		intent.putExtra("message", msg);
    		main_activity.sendBroadcast(intent);
        }
    }
    
}