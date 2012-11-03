/**
 * 
 */
package dv106.lecture4;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author jlnmsi
 *
 */
public class NotificationsDemo extends Activity {
	public static final int NOTIFICATION_ID = 1543;        // Unique ID 
	private Activity main_activity;
	private NotificationManager notifManager;    // Accessed in both listeners
	private int count = 1;                       // Notification count
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifdemo);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.notify_button);
        button.setOnClickListener(new ButtonClick()); 
        
        /* Assign listener to button */
        button = (Button)findViewById(R.id.notify_clear_button);
        button.setOnClickListener(new ClearClick()); 
    }
    
    private class ButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* 1. Setup Notification Builder */			
    		Notification.Builder builder = new Notification.Builder(main_activity); 
    		
    		/* 2. Configure Notification Alarm */
    		builder.setSmallIcon(R.drawable.ic_action_done)
    			.setWhen(System.currentTimeMillis())
    			.setTicker("Notification no. "+(count++))
				.setAutoCancel(true);
    			
    			
    		/* 3. Configure Drop-down Action */
    		builder.setContentTitle("More information")
    				.setContentText("Click to continue.")
    				.setContentInfo("Click!");
    		Intent intent = new Intent(main_activity, NotificationDisplay.class);   // Notification intent
    		PendingIntent notifIntent = PendingIntent.getActivity(main_activity, 0, intent, 0);
    		builder.setContentIntent(notifIntent);
    		
    		/* 4. Create Notification and use Manager to launch it */
    		Notification notification = builder.build();	
    		String ns = Context.NOTIFICATION_SERVICE;
    		notifManager = (NotificationManager) getSystemService(ns);
    		notifManager.notify(NOTIFICATION_ID, notification);
        }
    }
    
    private class ClearClick implements View.OnClickListener {
    	public void onClick(View v) {
    		notifManager.cancel(NOTIFICATION_ID);
        }
    }
}
