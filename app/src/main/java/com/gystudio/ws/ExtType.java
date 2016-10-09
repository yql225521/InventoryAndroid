package com.gystudio.ws;

import java.util.HashMap;
import java.util.Map;

public class ExtType {
	
	private static Map<String,ExtType> extTypes;
	
	private Map<String,ExtTypeProp> extTypeProps;
	
	private String typeName;
	
	public Map<String,Object> buildMapObject(){
		Map<String,Object> rmap=new HashMap<String,Object>();
		for (Map.Entry<String,ExtTypeProp> entry : this.extTypeProps.entrySet()) {
            if(entry.getValue().getPropType().equals("Double")){
            	rmap.put(entry.getKey(), 0.0);
			}else if(entry.getValue().getPropType().equals("Integer")){
				rmap.put(entry.getKey(), 0);
			}else{
				rmap.put(entry.getKey(), "");
			}
			
		}
		return rmap;
	}
	
	public static void setExtTypes(Map<String,ExtType> extTypes) {
		ExtType.extTypes = extTypes;
	}

	public static Map<String,ExtType> getExtTypes() {
		return extTypes;
	}

	public void setExtTypeProps(Map<String,ExtTypeProp> extTypeProps) {
		this.extTypeProps = extTypeProps;
	}

	public Map<String,ExtTypeProp> getExtTypeProps() {
		return extTypeProps;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}


}
