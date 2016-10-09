package com.gystudio.ws;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializerUtils implements JsonSerializer<Date>{
	@Override
	public JsonElement serialize(Date date, Type type, 
			JsonSerializationContext content) {
		return new JsonPrimitive(date.getTime());
	}

}
