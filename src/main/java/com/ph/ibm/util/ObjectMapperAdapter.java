package com.ph.ibm.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperAdapter {
	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
	}
	
	/**
	 * Marshal a Java object to a JSON string 
	 * 
	 * @param entity type to unmarshal to JSON
	 * @return 
	 */
	public static <T> String marshal(T entity) {
		String jsonResult = "";
		try {
			jsonResult = mapper.writeValueAsString(entity);
		} catch (IOException e) {
			 System.out.println("Exception Ocurred while converting" + e.getMessage());
			e.printStackTrace();
		}

		return jsonResult;
	}
	
	/**
	 * Unmarshal a JSON string to its corresponding Java object
	 * 
	 * @param aJsonString to marshal
	 * @param entityClass the entityClass
	 * @return
	 */
	public static <T> T unmarshal(String aJsonString, Class<T> entityClass) {
		T result = null;
		try {
			result = mapper.readValue(aJsonString, entityClass);
		} catch (IOException e) {
			System.out.println("Exception Ocurred while converting" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
