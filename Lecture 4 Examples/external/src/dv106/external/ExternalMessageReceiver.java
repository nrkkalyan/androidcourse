/**
 * 
 */
package dv106.external;

import dv106.external.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



/**
 * @author jlnmsi
 *
 */
public class ExternalMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		/* Extract received message */
		String msg = intent.getStringExtra("message");
		
		/* 1. Setup Notification Builder */			
		Notification.Builder builder = new Notification.Builder(ctx); 
		
		/* 2. Configure Notification */
		builder.setSmallIcon(R.drawable.ic_action_mail)
			.setWhen(System.currentTimeMillis())
			.setTicker(msg)
			.setContentTitle("Message Notification")
			.setContentText(msg + " received")
			.setAutoCancel(true);
		
		/* 3. Create Notification and use Manager to launch it */
		Notification notification = builder.build();
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(ns);	
		final int ID = 478;
		
		mNotificationManager.notify(ID, notification);

	}

}
