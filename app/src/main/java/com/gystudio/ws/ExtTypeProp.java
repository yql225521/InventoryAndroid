package com.gystudio.ws;

import java.util.Map;

public class ExtTypeProp {
	
	private static Map<String,ExtTypeProp> extTypes;
	
	private String propType;

	private String propName;
	
	private String descr;

	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public static void setExtTypes(Map<String,ExtTypeProp> extTypes) {
		ExtTypeProp.extTypes = extTypes;
	}

	public static Map<String,ExtTypeProp> getExtTypes() {
		return extTypes;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}


}
