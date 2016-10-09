package com.gystudio.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TranUtils{
	/** 
	 * 编码 
	 * @param bstr 
	 * @return String 
	 */  
	public static String encode(String str){  

		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return str;
		}

	}  

	public static JsonObject getJsonObject(SoapPrimitive soapObject){
		
		String json=  TranUtils.decode(soapObject.toString());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
		
		return jsonObject;
		
	}
	
	/** 
	 * 解码 
	 * @param str 
	 * @return string 
	 */  
	public static String decode(String str){  
		String rstr="";
		try {  
			rstr=URLDecoder.decode(str, "UTF-8");
		} catch (IOException e) {  
			e.printStackTrace();  

		}  

		return rstr;  
	}  

	/** 
	 * @param args 
	 */  
	public static void main(String[] args) {

		String aa = "[{更多:更:多}]";  
		aa = TranUtils.encode(aa);  
		System.out.println("----aa:"+aa);  

		String str2 = new String(TranUtils.decode(aa));  
		System.out.println("-----str2:"+str2);  
	}  
}
