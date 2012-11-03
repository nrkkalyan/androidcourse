/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dv106.lecture4;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity used by NotificationDemo to handle the notification.
 */
public class NotificationDisplay extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.notifdisplay);
        
        String msg = "This is an activity (NotificationDisplay) that might contain " +
		"more information about the current notification. Or alternatively, be an " +
		"activity that can handle the reason for this notification. For example, it " +
		"might be a mail program handling an incoming mail notification." +
		"\nPushing the button below " +
		"will terminate and remove this notification. It will also take us back to " +
		"the Activity (NotificationDemo) that raised this notification.";
        TextView display   = (TextView)findViewById(R.id.notify_handling);
        display.setText(msg);
        
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.clear_notify_button);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        // The user has confirmed this notification, so remove it.
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .cancel(NotificationsDemo.NOTIFICATION_ID);
        
        // We're done.
        finish();
    }
}
