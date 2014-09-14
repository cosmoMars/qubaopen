package com.qubaopen.backend;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParseTest {
	
	public static void main(String[] args) {
		
		final String json = "{\"questions\": [{\"questionNo\": \"1\", \"questionContent\": \"qqwe qe c懂得\"}]}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode jsonNode = objectMapper.readTree(json);
			ArrayNode questions = (ArrayNode) jsonNode.path("questions");
			for (JsonNode question : questions) {
				System.out.println(question.get("questionNo"));
				System.out.println(question.get("questionContent"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
