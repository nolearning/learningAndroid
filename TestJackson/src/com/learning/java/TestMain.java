package com.learning.java;

import java.io.File;

import com.fasterxml.jackson.*;
import com.fasterxml.jackson.databind.*;

public class TestMain {
	private static class Package {
		private String name;
		private String id;
		public String publisher;
		public String version;
		public String grade;
		public String subject;
		public int fasciculeOrder;
		public String fasciculeName;
		public int limitedTime;
		public String fullMark;
		public int starred;
		private Package[] childNodes;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public Package[] getChildNodes() {
			return childNodes;
		}
		
		public void setChildNodes(Package[] childNodes) {
			this.childNodes = childNodes;
		}
	}
	
	private class TestJackson {
		private File jsonFile;
		private ObjectMapper mapper;
		public TestJackson(String file) {
			jsonFile = new File(file);
			mapper = new ObjectMapper();
		}
		
		public void readJSONByDataBinding() {
			try {
				Package p1 = mapper.readValue(jsonFile, Package.class);
				mapper.createObjectNode();
				System.out.println(p1.childNodes[1].childNodes[0].name);
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		
		public void readJSONbyTreeModel() {
			try {
				JsonNode rootNode = mapper.readValue(jsonFile, JsonNode.class);
				System.out.println(rootNode.get("childNodes").asText());
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	
	public static void main(String[] args) {
		TestMain tm = new TestMain();
		
		TestJackson tj = tm.new TestJackson("package.json");
		///tj.readJSONByDataBinding();
		tj.readJSONbyTreeModel();
	}

}
