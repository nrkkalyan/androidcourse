/**
 * 
 */
package dv106.lecture4;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author jlnmsi
 *
 */
public class ServiceDemo extends Activity {
	private Activity main_activity;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.start_service);
        button.setOnClickListener(new StartClick());  
        
        button = (Button)findViewById(R.id.stop_service);
        button.setOnClickListener(new StopClick());  
    }
    
    private class StartClick implements View.OnClickListener {   	
    	public void onClick(View v) {
    		Intent intent = new Intent(main_activity,SlowCountService.class);
    		main_activity.startService(intent);
    	}
    }
    private class StopClick implements View.OnClickListener {   	
    	public void onClick(View v) {
    		Intent intent = new Intent(main_activity,SlowCountService.class);
    		main_activity.stopService(intent);
    	}
    }
}
