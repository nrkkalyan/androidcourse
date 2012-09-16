/**
 * VaxjoWeather.java Created: May 9, 2010 Jonas Lundberg, LnU
 */

package dv106.weather;

import java.io.InputStream;
import java.net.URL;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * This is a first prototype for a weather app. It is currently only downloading
 * weather data for Vдxjц.
 * 
 * This activity downloads weather data and constructs a WeatherReport, a data
 * structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports (forecast.xml)
 * produced by www.yr.no. The handler constructs a WeatherReport containing meta
 * data for a given location (e.g. city, country, last updated, next update) and
 * a sequence of WeatherForecasts. Each WeatherForecast represents a forecast
 * (weather, rain, wind, etc) for a given time period.
 * 
 * The next task is to construct a list based GUI where each row displays the
 * weather data for a single period.
 * 
 * 
 * @author jlnmsi
 * 
 */

public class VaxjoWeather extends ListActivity {
	private InputStream	input;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			URL url = new URL("http://www.yr.no/sted/Sverige/Kronoberg/V%C3%A4xj%C3%B6/forecast.xml");
			WeatherReport report = WeatherHandler.getWeatherReport(url);
			
			/* Print location meta data */
			System.out.println(report);
			
			/* Print forecasts */
			int count = 0;
			for (WeatherForecast forecast : report) {
				count++;
				System.out.println("Forecast " + count);
				System.out.println(forecast.toString());
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		
	}
	
}