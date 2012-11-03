package dv106.lecture4;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MoreIntents extends Activity {
	private Activity main_activity;
	private TextView name_display;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intents);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Assign listeners to buttons */
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new ButtonClick());      
        
        Button name_button = (Button)findViewById(R.id.name_button);
        name_button.setOnClickListener(new NameButtonClick());
                
        Button browser_button = (Button)findViewById(R.id.browser_button);
        browser_button.setOnClickListener(new BrowserButtonClick());

        
        Button implicit_button = (Button)findViewById(R.id.my_implicit_button);
        implicit_button.setOnClickListener(new InternalButtonClick());
        
        Button external_button = (Button)findViewById(R.id.external_implicit_button);
        external_button.setOnClickListener(new ExternalButtonClick());
        
        
        /* Find name display */
        name_display = (TextView) findViewById(R.id.name_display);
    }
    
    private class ButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* Start new Activity */
    		Intent intent = new Intent(main_activity,Hello.class);
    		main_activity.startActivity(intent);
        }
    }
    
    private class NameButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* Start new Activity that returns a result */
    		Intent intent = new Intent(main_activity,ReadName.class);
    		main_activity.startActivityForResult(intent,0);
        }
    }
    
    private class BrowserButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* Start implicit Activity to browse a web site */
    		Uri uri = Uri.parse("http://www.google.se");
    		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    		main_activity.startActivity(intent);
        }
    }
    
  
    private String ACTION = "dv106.lecture4.VIEW";
    private class InternalButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* Start implicitly my lecture4.ShowTime Activity */
    		Intent intent = new Intent(ACTION);
    		main_activity.startActivity(intent);
        }
    }
    
    private static final String ACTION2 = "dv106.external.VIEW";
    private class ExternalButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		/* Start implicitly my external.ShowDate Activity */
    		Intent intent = new Intent(ACTION2);
    		main_activity.startActivity(intent);
        }
    }
    
    /** Called when the activity receives a results. */
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent result) {
    	if (resultCode == RESULT_OK) {
    		String name = result.getStringExtra("result");
    		name_display.setText(name);
    	}
    }
}