package com.ufam.hospitalapp.services;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonObjectRequest extends Request<JSONObject> {
	private Listener<JSONObject> response;
	private Map<String, String> params;
	
	
	public CustomJsonObjectRequest(int method, String url, Map<String, String> params, Listener<JSONObject> response, ErrorListener listener) {
		super(method, url, listener);
		this.params = params;
		this.response = response;
		// TODO Auto-generated constructor stub
	}
	public CustomJsonObjectRequest(String url, Map<String, String> params, Listener<JSONObject> response, ErrorListener listener) {
		super(Method.GET, url, listener);
		this.params = params;
		this.response = response;
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, String> getParams() throws AuthFailureError {
		return params;
	}
	
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("apiKey", "Essa e minha API KEY: json object");
		//header.put("Content-Type", "application/json; charset=utf-8");
		
		return(header);
	}
	
	public Priority getPriority(){
		return(Priority.NORMAL);
	}

	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			//response.headers.put(HTTP.CONTENT_TYPE,
			//		response.headers.get("Content-Type"));
			//String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			String js =  new String (response.data,"UTF-8");
			//String str =  new String (response.data,"UTF-8");
			//Log.i("NEW STR", str);
			Log.i("HEADERS", response.headers.toString());
			Log.i("APP", js);
			return(Response.success(new JSONObject(js), HttpHeaderParser.parseCacheHeaders(response)));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.i("jsonObjectResponse",response.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
			Log.i("jsonObjectResponse",response.toString());
		}
		return null;
	}

	
	@Override
	protected void deliverResponse(JSONObject response) {
		this.response.onResponse(response);
	}

}
