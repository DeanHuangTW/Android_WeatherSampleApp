package com.example.weathersample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class WeatherManager {
	private final String TAG = "Dean";
	static final String WEBSERVICE_URL_CITY_CODE = "http://weather.service.msn.com/find.aspx?outputview=search&src=vista&weasearchstr=%s";
	static final String WEBSERVICE_URL_CITY = "http://weather.service.msn.com/data.aspx?src=vista&weadegreetype=C&culture=en-US&wealocations=%s";
	public WeatherManager() {
		
	}
	
	/* Input: web address
	 * return: web data
	 * */
	public InputStream getWebData(String address) throws IOException {
		InputStream stream = null;
		String str = null;		

		HttpClient getClient = new DefaultHttpClient();  
        HttpGet get = new HttpGet(address);  
        try {  
            HttpResponse response = getClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	str = EntityUtils.toString(response.getEntity());
            } else {
            	Log.e(TAG, "Get data fail");
            }
        } catch (Exception e) {
            e.printStackTrace();  
        }

        // 因為XML parse需要InputStream格式,所以這裡先做轉換
        stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        return stream;

	}	
	
	/* Input: cityCode
	 * return: this city's weather information
	 */
	public InputStream getWeatherXml(String cityCode) {
		String url = String.format(WEBSERVICE_URL_CITY, cityCode);	
		InputStream is = null;
		try {
			is = getWebData(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	/* Input: City English name. eq: "taipei"
	 * Return: CityCode web data
	 */
	public InputStream getCityCodeXml(String city) {
		String url = String.format(WEBSERVICE_URL_CITY_CODE, city);	
		InputStream is = null;
		try {
			is = getWebData(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
}
