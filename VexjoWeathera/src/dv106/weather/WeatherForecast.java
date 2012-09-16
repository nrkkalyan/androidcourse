/**
 * WeatherForecast.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */
package dv106.weather;

import android.text.format.Time;

/**
 * Each WeatherForecast represents a forecast (weather, rain, wind, temp, etc)
 * for a given time period.
 * 
 *  ToDO: Translate weather and wind names to English!
 *  
 * @author jonasl
 *
 */
public class WeatherForecast {
	private Time startTime, endTime;
	private int period_code, weather_code;
	private String weather_name;  // Name in Norwegian!
	private String wind_direction, direction_name; // Name in Norwegian!
	private double rain, wind_speed; 
	private String speed_name;
	private int temp;
	
	/*
	 * Access methods
	 */
	
	/* Time period */
	public String getStartYYMMDD() {return TimeUtils.getYYMMDD(startTime);}
	public String getStartHHMM() {return TimeUtils.getHHMM(startTime);}
	public String getEndYYMMDD() {return TimeUtils.getYYMMDD(endTime);}
	public String getEndHHMM() {return TimeUtils.getHHMM(endTime);}
	public int getPeriodCode() {return period_code;}
	/* Weather */
	public String getWeatherName() {return weather_name;}
	public int getWeatherCode() {return weather_code;}
	/* Rain (mm/h), Temp (Celsius)*/
	public double getRain() {return rain;}
	public int getTemp() {return temp;}
	/* Wind */
	public String getWindDirection() {return wind_direction;}
	public String getWindDirectionName() {return direction_name;}
	public double getWindSpeed() {return wind_speed;}
	public String getWindSpeedName() {return speed_name;}
	
	
	/* 
	 * Methods used by WeatherHandler to build forecast 
	 * */	
	void setPeriod(String from, String to, String period) {
		//System.out.println(from+"\t"+to+"\t"+period);
		startTime = TimeUtils.getTime(from);
		endTime = TimeUtils.getTime(to);
		startTime.month++; endTime.month++;
		period_code = Integer.parseInt(period);
	}
	
	void setWeather(String num, String name) {
		//System.out.println(num+"\t"+name);
		weather_code = Integer.parseInt(num);
		weather_name = name;
	}
	
	void setWindDirection(String dir, String name) {
		//System.out.println(dir+"\t"+name);
		wind_direction = dir;
		direction_name = name;
	}
	
	void setWindSpeed(String num, String name) {
		//System.out.println(num+"\t"+name);
		wind_speed = Double.parseDouble(num);
		speed_name = name;
	}
	
	void setRain(String num) {  // Precipitation, nederbörd
		//System.out.println(num);
		rain = Double.parseDouble(num);
	}
	
	void setTemp(String num) {
		//System.out.println(num);
		temp = Integer.parseInt(num);
	}
	
	/*
	 * Diagnostics
	 */
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Date: "+ TimeUtils.getYYMMDD(startTime));
		buf.append("\nFrom:" +TimeUtils.getHHMM(startTime)+", To: "+TimeUtils.getHHMM(endTime)
				   +", Period: "+period_code);
		buf.append("\nWeather: "+weather_name+", Code: "+weather_code);
		buf.append("\nWind: "+wind_direction+", Speed: "+wind_speed+"m/s");
		buf.append("\nTemp: "+temp+", Rain: "+rain+"mm/h");
		return buf.toString();
	}
	
	


}
