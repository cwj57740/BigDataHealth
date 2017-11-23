package com.diagnosis;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.sf.json.JSONObject;

public class MapDistance {
	
	private static double EARTH_RADIUS = 6378.137;
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	//两点间的距离
	public static double getDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   return s;
	}
	 //获得一定范围（正方形范围）的最小、最大经纬度,raidusMile单位为米
	public  Map getAround(double latitude, double longitude, double raidusMile) {
        Map map = new HashMap();
  
        double degree = (24901 * 1609) / 360.0; // 获取纬度的每度表示的距离，约为111km，此数值单位为m
          
        double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));//经度每一度的距离
        double dpmLng = 1 / mpdLng;
        double radiusLng = dpmLng * raidusMile;
        //获取最小经度
        double minLng = longitude - radiusLng;
        // 获取最大经度
        double maxLng = longitude + radiusLng;
          
        double dpmLat = 1 / degree;
        double radiusLat = dpmLat * raidusMile;
        // 获取最小纬度
        double minLat = latitude - radiusLat;
        // 获取最大纬度
        double maxLat = latitude + radiusLat;
          
        map.put("minLat", minLat);
        map.put("maxLat", maxLat);
        map.put("minLng", minLng);
        map.put("maxLng", maxLng);
          
        return map;
    }
	//由地址得到经纬度
	public static Map<String,Double> getLngAndLat(String address){
		Map<String,Double> map=new HashMap<String, Double>();
		 String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=1qHKvOwOc4iwMAe9haLV6teGC7ZQ6j0z";
		// BufferedReader in = new BufferedReader(new InputStreamReader(url));
		 //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));    
		 String json = loadJSON(url);
		 
	        //System.out.println(new String(json.getBytes("UTF-8")));
		 int index=json.indexOf("status");
		 String statusString="";
		 statusString=json.substring(index+8,index+9);
		 if(!statusString.equals("0")){
			 System.out.println("您输入的地址不正确，请重新输入！");
			 return map;
		 }
		 
	        JSONObject obj = JSONObject.fromObject(json);
	       
	        if(obj.get("status").toString().equals("0")){
	        	double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
	        	double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
	        	map.put("lng", lng);
	        	map.put("lat", lat);
	        	
	        }else{
	        	System.out.println("您输入的地址不正确，请重新输入！");
	        }
		return map;
	}
	//解析
	 public static String loadJSON (String url) {
	        StringBuilder json = new StringBuilder();
	        try {
	            URL oracle = new URL(url);
	            URLConnection yc = oracle.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                                        yc.getInputStream()));
	            String inputLine = null;
	            while ( (inputLine = in.readLine()) != null) {
	                json.append(inputLine);
	            }
	            in.close();
	        } catch (MalformedURLException e) {
	        } catch (IOException e) {
	        }
	        return json.toString();
	    }
}
