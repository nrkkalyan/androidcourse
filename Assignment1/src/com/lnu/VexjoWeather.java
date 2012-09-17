package com.lnu;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnu.weather.WeatherForecast;
import com.lnu.weather.WeatherHandler;
import com.lnu.weather.WeatherReport;

public class VexjoWeather extends ListActivity {
	private DecimalFormat	df	= new DecimalFormat("#.##");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_vexjo_weather);
		
		try {
			URL url = new URL("http://www.yr.no/sted/Sverige/Kronoberg/V%C3%A4xj%C3%B6/forecast.xml");
			WeatherReport report = WeatherHandler.getWeatherReport(url);
			
			setListAdapter(new MobileArrayAdapter(this, report.getForeCasts()));
			setTitle(report.getCity() + "," + report.getCountry() + "\n" + //
					df.format(report.getLatitude()) + "," + df.format(report.getLongitude()));
			
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_vexjo_weather, menu);
		return true;
	}
}

class MobileArrayAdapter extends ArrayAdapter<WeatherForecast> {
	private final Context				context;
	private final List<WeatherForecast>	values;
	
	public MobileArrayAdapter(Context context, List<WeatherForecast> values) {
		super(context, R.layout.activity_vexjo_weather, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.activity_vexjo_weather, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		WeatherForecast weatherForecast = values.get(position);
		textView.setText(weatherForecast.toString());
		
		// Change icon based on name
		int s = weatherForecast.getWeatherCode();
		int imageSource = -1; // Default
		switch (s) {
			case 1: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d1;
				} else {
					imageSource = R.drawable.n1;
				}
				break;
			}
			case 2: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d2;
				} else {
					imageSource = R.drawable.n2;
				}
				break;
			}
			case 3: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d3;
				} else {
					imageSource = R.drawable.n3;
				}
				break;
			}
			case 4: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d4;
				} else {
					imageSource = R.drawable.n4;
				}
				break;
			}
			case 5: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d5;
				} else {
					imageSource = R.drawable.n5;
				}
				break;
			}
			case 6: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d6;
				} else {
					imageSource = R.drawable.n6;
				}
				break;
			}
			case 7: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d7;
				} else {
					imageSource = R.drawable.n7;
				}
				break;
			}
			case 8: {
				if (weatherForecast.isMorning()) {
					imageSource = R.drawable.d8;
				} else {
					imageSource = R.drawable.n8;
				}
				break;
			}
			case 9: {
				imageSource = R.drawable.c9;
				break;
			}
			case 10: {
				imageSource = R.drawable.c10;
				break;
			}
			case 11: {
				imageSource = R.drawable.c11;
				break;
			}
			case 12: {
				imageSource = R.drawable.c12;
				break;
			}
			case 13: {
				imageSource = R.drawable.c13;
				break;
			}
			case 14: {
				imageSource = R.drawable.c14;
				break;
			}
			case 15: {
				imageSource = R.drawable.c15;
				break;
			}
			default: {
				imageSource = R.drawable.ic_launcher;
			}
			
		}
		
		imageView.setImageResource(imageSource);
		
		return rowView;
	}
}