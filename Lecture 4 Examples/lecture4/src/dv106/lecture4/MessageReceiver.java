

package dv106.lecture4;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		String msg = intent.getStringExtra("message");
		Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
	}
}

