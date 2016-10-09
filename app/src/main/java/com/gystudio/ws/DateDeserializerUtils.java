package com.gystudio.ws;

import java.lang.reflect.Type;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
 
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeserializerUtils implements JsonDeserializer<Date>{
	String[] pattern = new String[]{"yyyy-MM","yyyyMM","yyyy/MM",   
            "yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd",   
            "yyyyMMddHHmmss",   
                        "yyyy-MM-dd HH:mm:ss",   
                        "yyyy/MM/dd HH:mm:ss"};   
	@Override
	public Date deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		String dateString=StringUtils.substringBefore(json.getAsString(), "T");  
		try{
			Date rt=DateUtils.parseDate(dateString,pattern);
			return rt;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
