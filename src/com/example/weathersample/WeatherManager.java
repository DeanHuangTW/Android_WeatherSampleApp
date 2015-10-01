package com.example.weathersample;

import java.io.ByteArrayInputStream;
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
	
	public WeatherManager() {
		
	}
	
	/* Input: web address
	 * output: web data
	 * */
	public InputStream getWeathertData(String address) {		
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
	
	public String getCityAddress() {
		// 強制使用MSN的台北天氣資料
		String webAddress = "http://weather.service.msn.com/data.aspx?src=vista&weadegreetype=C&culture=en-US&wealocations=wc:TWXX0021";
		
		return webAddress;
	}
}
