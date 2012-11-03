package dv106.lecture4;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowTime extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        
        /* Find text display */
        TextView display = (TextView) findViewById(R.id.display_text);
        Date date = new Date(System.currentTimeMillis());
        //String str = DateFormat.getDateInstance().format(date);
        String str = DateFormat.getTimeInstance().format(date);
        display.setText(str);
    }
}