package utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	/*
	 * Helper method to convert a JSON request body to a Java Map
	 */
	public static Map<String, Object> getRequestJsonBodyAsMap(HttpServletRequest req) throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> result = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, Object>>(){});
		return result;
	}
}
