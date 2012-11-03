/**
 * 
 */
package dv106.lecture4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jlnmsi
 *
 */
public class Threading extends Activity {
	private Activity main_activity;
	private Handler handler = new Handler();
	private TextView count_view;
	private ProgressBar progress;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threading);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Raising Toasts */
        Button button = (Button)findViewById(R.id.start_toast);
        button.setOnClickListener(new StartToast());         
        button = (Button)findViewById(R.id.stop_toast);
        button.setOnClickListener(new StopToast());  
        
        /* Counting Numbers */
        button = (Button)findViewById(R.id.start_count);
        button.setOnClickListener(new StartCount());         
        button = (Button)findViewById(R.id.stop_count);
        button.setOnClickListener(new StopCount());  
        count_view = (TextView)findViewById(R.id.count_display);
        
        /* Making Progress */
        button = (Button)findViewById(R.id.start_progress);
        button.setOnClickListener(new StartProgress());         
        button = (Button)findViewById(R.id.stop_progress);
        button.setOnClickListener(new StopProgress());  
        progress = (ProgressBar)findViewById(R.id.progress_bar);
    }
    
    /* Stop all threads */
    @Override
    public void onStop() {
    	super.onStop();
        keepToasting = false; // Stop Toasting
        keepCounting = false; // Stop Counting
        if (task != null && !task.isCancelled())
        	task.cancel(true);
    }
    
    /*
     * Eaxmple: Raising Toasts
     */
    private boolean keepToasting;  // Used to interrupt thread
    private class StartToast implements View.OnClickListener {   	
    	public void onClick(View v) {
    		// Start up the heavy work thread. 
    		keepToasting = true;
            Thread thr = new Thread(null, work, "Raising Toasts");
            thr.start();
    	}
    }
  
	
    Runnable work = new Runnable() {
        public void run() {
             int count = 0;
        	 while (count<20 && keepToasting) {  // Maximum of 20 toasts
             	count++; 
            	handler.post(update);    // Handler to interact with GUI thread
             	SystemClock.sleep(3000);   // Sleep 3 seconds
            }
        }
    };
    
    Runnable update = new Runnable() {  // Simple GUI interaction
        public void run() {
            Toast.makeText(main_activity,"Hello",Toast.LENGTH_SHORT).show();             
        }
    };
    
    private class StopToast implements View.OnClickListener {   	
    	public void onClick(View v) {
    		// Force work thread to stop working
    		keepToasting = false;    		
    	}
    }
    
    /*
     * Example: Counting Numbers. 
     * Exactly the same as Raising Toasts above apart from that we now 
     * are updating the TextView display.
     */
    private boolean keepCounting;
    private int number_count = 0;
    private class StartCount implements View.OnClickListener {   	
    	public void onClick(View v) {
    		// Start up the work thread updating numbers. 
    		keepCounting = true;
            Thread thr = new Thread(null, count_work, "Counting Numbers");
            thr.start();
    	}
    }
  	
    Runnable count_work = new Runnable() {
        public void run() {
        	 while (number_count<50 && keepCounting) {  // 50 is upper limit
        		number_count++;
            	handler.post(count_update);  // Post message to GUI thread queue
             	SystemClock.sleep(1000);   // Sleep 1 seconds
            }
        }
    };
    
    /* A "Message" to be sent to the GUI thread.
     * The Handler takes care of the thread interaction. */
    Runnable count_update = new Runnable() {
        public void run() {  
            String number = new Integer(number_count).toString();
            count_view.setTextSize(32);
            count_view.setText(number);
        }
    };
    
    private class StopCount implements View.OnClickListener {   	
    	public void onClick(View v) {
    		keepCounting = false;        // Force thread to stop working
            count_view.setTextSize(24);	 // Reset display
    		count_view.setText("Done Counting");
    		number_count = 0;            // Prepare next count
    	}
    }
    
    
    /*
     * Example Making progress. 
     * We are here using AsyncTask to interact with the GUI thread.
     */  
    private AsyncTask<Integer, Integer, String> task;
    private class StartProgress implements View.OnClickListener {   	
    	public void onClick(View v) {
    		// Start up AsyncTask to update progress bar. 
    		System.out.println("StartProgress");
    		task = new ProgressTask();
    		task.execute(5);   // Calls doInBackground( [5] )
    		
    	}
    }
   
    private class StopProgress implements View.OnClickListener {   	
    	public void onClick(View v) {
    		// Force task to stop working
    		task.cancel(true);
    		progress.setProgress(0);  
    	}
    }
    
    /* Help class to help interacting with GUI thread  */
    private class ProgressTask extends AsyncTask<Integer, Integer, String> {
    	@Override  // Heavy work, executed in separate thread
        protected String doInBackground(Integer... steps) {
       	 	int step_size = steps[0];
       	 	int step_count = 0;
       	 	while (step_count<100 && !task.isCancelled()) {  
       	 		publishProgress(step_size); // Call onProgressUpdate
       	 		SystemClock.sleep(500);     // Sleep 1/2 second
       	 		step_count += step_size;
       	 	}
       	 	return "End of Progress";  // Message sent to onProgressUpdate 
        }
        @Override  // Update GUI, executed in GUI thread
        protected void onProgressUpdate(Integer... update) {
        	progress.incrementProgressBy(update[0]);
        }
        @Override // called when doInBackground completed, executed in GUI thread
        protected void onPostExecute(String result) {
        	Toast.makeText(main_activity,result,Toast.LENGTH_SHORT).show(); 
        }
    }
    

}
