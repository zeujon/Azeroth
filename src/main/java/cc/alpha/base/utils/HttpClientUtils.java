package cc.alpha.base.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpClientUtils {

	private static HttpClient hc = new DefaultHttpClient(new ThreadSafeClientConnManager());

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException, UnsupportedEncodingException, IOException, URISyntaxException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", "18602129581");
		map.put("content", "Hello World！");
		String url = "http://139.196.241.163:8686/send-sms?";

		String body = post(url, map);
		System.out.println(body);
	}

	/**
	 * Map<String, String> 转 List<NameValuePair>
	 * @param params
	 * @return
	 */
	public static List<NameValuePair> convertList(Map<String, String> map) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(map != null){
			for (Entry<String, String> entry : map.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return params;
	}
	
	/**
	 * Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 * @throws URISyntaxException 
	 */
	public static String get(String url, Map<String, String> paramsMap) throws ParseException, UnsupportedEncodingException, IOException, URISyntaxException {
		String body = null;
		List<NameValuePair> params = convertList(paramsMap);
		// Get请求
		HttpGet httpget = new HttpGet(url);
		// 设置参数
		String str = EntityUtils.toString(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
		// 发送请求
		HttpResponse httpresponse = hc.execute(httpget);
		// 获取返回数据
		HttpEntity entity = httpresponse.getEntity();
		body = EntityUtils.toString(entity);
		if (entity != null) {
			entity.consumeContent();
		}
		httpget.abort();
		return body;
	}
	
	/**
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ClientProtocolException 
	 */
	public static String get(String url) throws URISyntaxException, ClientProtocolException, IOException {
		String body = null;
		// Get请求
		HttpGet httpget = new HttpGet(url);
		// 设置参数
		httpget.setURI(new URI(httpget.getURI().toString()));
		// 发送请求
		HttpResponse httpresponse = hc.execute(httpget);
		// 获取返回数据
		HttpEntity entity = httpresponse.getEntity();
		body = EntityUtils.toString(entity);
		if (entity != null) {
			entity.consumeContent();
		}
		httpget.abort();
		return body; 
	}

	/**
	 * // Post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String post(String url, Map<String, String> paramsMap) throws ClientProtocolException, IOException {
		String body = null;
		List<NameValuePair> params = convertList(paramsMap);
		// Post请求
		HttpPost httppost = new HttpPost(url);
		// 设置参数
		httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		// 发送请求
		HttpResponse httpresponse = hc.execute(httppost);
		// 获取返回数据
		HttpEntity entity = httpresponse.getEntity();
		body = EntityUtils.toString(entity);
		if (entity != null) {
			entity.consumeContent();
		}
		httppost.abort();
		return body;
	}

}
