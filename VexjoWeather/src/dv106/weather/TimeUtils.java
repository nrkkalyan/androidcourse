/**
 * TimeUtils.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */
package dv106.weather;

import android.text.format.Time;

/**
 * @author jlnmsi
 *
 */
public class TimeUtils {

	public static Time getTime(String date_string) {
		Time time = new Time();
		try {		
			time.parse3339(date_string);
			//System.out.println(date_string);
			//System.out.println(time);
			//System.out.println(getYYMMDD(time)+" "+getHHMM(time));
		}
		catch (Exception e) {
			System.out.println("Time string: "+date_string);
			e.printStackTrace();
		}
		return time;
	}
	
	public static String getYYMMDD(Time time) {
		return time.year+"-"+addZero(time.month)+"-"+addZero(time.monthDay);
	}
	
	public static String getHHMM(Time time) {
		return addZero(time.hour)+":"+addZero(time.minute);
	}
	
	private static String addZero(int n) {
		if (n<10)
			return "0"+n;
		else
			return Integer.toString(n);
	}
}
