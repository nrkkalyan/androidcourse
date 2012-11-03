/**
 * 
 */
package dv106.lecture4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



/**
 * @author jlnmsi
 *
 */
public class InternalMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		String msg = intent.getStringExtra("message");
		Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
	}

}
