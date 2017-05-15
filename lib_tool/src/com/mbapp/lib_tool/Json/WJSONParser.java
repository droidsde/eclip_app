package com.mbapp.lib_tool.Json;
 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
 
public class WJSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static HttpURLConnection urlConnection;
    
 
    // constructor
    public WJSONParser() {
 
    }
 
    // function get json from url
    // by making HTTP POST or GET mehtod
    public static  JSONObject makeHttpRequest(String urlIn) {
 

    	StringBuilder result = new StringBuilder();
            	 
            		try {
            	        URL url = new URL(urlIn);
            	        urlConnection = (HttpURLConnection) url.openConnection();
            	        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            	        String line;
            	        while ((line = reader.readLine()) != null) {
            	            result.append(line);
            	        }

            	    }catch( Exception e) {
            	        e.printStackTrace();
            	    }
            	    finally {
            	        urlConnection.disconnect();
            	    }
            		
            		json = result.toString(); 
      
 
   
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
    public class getData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("https://api.github.com/users/dmnugent80/repos");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            //Do something with the JSON string

        }

    }
}