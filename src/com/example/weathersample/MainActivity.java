package com.example.weathersample;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String TAG = "Dean";
	
	private String data;
	private TextView mWeatherData;
	private Button mShow;
	private EditText mEditTextCityName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mWeatherData = (TextView) findViewById(R.id.textView1);
		mEditTextCityName = (EditText) findViewById(R.id.edit_city_name);
		
		mShow = (Button) findViewById(R.id.button_show_data);
		mShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Thread myThread = new Thread(runnable);
				myThread.start();
				try {
					//使用join,等待資料處理完成再setText()
					myThread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
				
				mWeatherData.setText(data);
			}
			
		});		
		
	}
	
	final Runnable runnable = new Runnable() {
    	InputStream xmlData;
        public void run() {        	
        	WeatherManager WM = new WeatherManager();
        	
        	// 取得City name
        	String cityName = mEditTextCityName.getText().toString();
        	// 根據city name取得city code (XML format)
        	xmlData = WM.getCityCodeXml(cityName);        	
        	// parse XML
        	String cityCode = parseCityCodeXml(xmlData);
        	Log.v(TAG, cityName + ". city code is:" + cityCode);
        	
        	//根據city code取得weather XML data
			xmlData =WM.getWeatherXml(cityCode);
        	// parse XML
        	data = parseWeatherXml(xmlData);
        }
    };
    
    
/* MSN weather XML format example:
<?xml version="1.0"?>
<weatherdata xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <weather encodedlocationname="Taipei" entityid="25135" alert="" timezone="8" long="121.509" lat="25.039" attribution2="Foreca" attribution="http://www.foreca.com/" provider="Foreca" degreetype="C" imagerelativeurl="http://blob.weather.microsoft.com/static/weather4/en-us/" url="http://a.msn.com/54/en-US/ct25.039,121.509?ctsrc=vista" weatherlocationname="Taipei, Asia" weatherlocationcode="wc:TWXX0021">
	<current windspeed="11 km/h" shortday="Thu" day="Thursday" winddisplay="11 km/h West" humidity="66" feelslike="34" observationpoint="Taipei" observationtime="09:30:00" date="2015-10-01" skytext="Mostly Cloudy" skycode="28" temperature="30"/>
	<forecast shortday="Thu" day="Thursday" date="2015-10-01" precip="" skytextday="T-Storms" skycodeday="4" high="34" low="24"/>
	<forecast shortday="Fri" day="Friday" date="2015-10-02" precip="" skytextday="Rain Showers" skycodeday="11" high="28" low="23"/>
	<forecast shortday="Sat" day="Saturday" date="2015-10-03" precip="" skytextday="Mostly Sunny" skycodeday="34" high="33" low="24"/>
	<forecast shortday="Sun" day="Sunday" date="2015-10-04" precip="" skytextday="Partly Sunny" skycodeday="30" high="33" low="24"/>
	<forecast shortday="Mon" day="Monday" date="2015-10-05" precip="" skytextday="Rain Showers" skycodeday="11" high="30" low="25"/>
	<toolbar minversion="1.0.1965.0" timewindow="60"/>
  </weather>
</weatherdata> 
 
 */
	private String parseWeatherXml(InputStream xmlData) {
		String result = null;
		try {
			//設定XML parse
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlparser = factory.newPullParser();
			//把XML放到parse
			xmlparser.setInput(xmlData,"utf-8");
			//開始翻譯
			String tagName;
			int eventType = xmlparser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
			    switch (eventType) {			              
			    	case XmlPullParser.START_TAG:
			    		//取得tag,根據tag做不同的事
			    		tagName = xmlparser.getName(); 
			    		if (tagName.equals("weather")) {			    			
			    			result = "location: " + xmlparser.getAttributeValue(null, "weatherlocationname") + "\n";
			    		} else if (tagName.equals("current")) {
			    			result += "temperature: " + xmlparser.getAttributeValue(null, "temperature") + "\n";
			    			result += "sky: " + xmlparser.getAttributeValue(null, "skytext") + "\n";			    			
			    		}
			    		break;
			    	default:
			    		break;
			    }
			    eventType = xmlparser. next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return result;
	}

	private String parseCityCodeXml(InputStream xmlData) {
		String cityCode = null;
		try {
			//設定XML parse
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlparser = factory.newPullParser();
			//把XML放到parse
			xmlparser.setInput(xmlData,"utf-8");
			//開始翻譯
			String tagName;
			int eventType = xmlparser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
			    switch (eventType) {			              
			    	case XmlPullParser.START_TAG:
			    		//取得tag,根據tag做不同的事
			    		tagName = xmlparser.getName(); 
			    		if (tagName.equals("weather")) {			    			
			    			cityCode = xmlparser.getAttributeValue(null, "weatherlocationcode");
			    		}
			    		break;
			    	default:
			    		break;
			    }
			    eventType = xmlparser. next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return cityCode;
	}
}
