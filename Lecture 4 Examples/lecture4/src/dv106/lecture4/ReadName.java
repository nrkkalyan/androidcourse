/**
 * 
 */
package dv106.lecture4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author jonasl
 *
 */
public class ReadName extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_name);
               
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.done_button);
        button.setOnClickListener(new ButtonClick());
    }
    
    private class ButtonClick implements View.OnClickListener {
    	public void onClick(View v) {
    		EditText reader = (EditText)findViewById(R.id.name_reader);
    		String name = reader.getText().toString();
    		
    		/* Create new result intent */
    		Intent reply = new Intent();
    		reply.putExtra("result", name);
    		setResult(RESULT_OK,reply);
    		System.out.println(name);
    		finish();   // Close this activity
        }
    }
}
