package com.learning.java;

import java.io.File;
import java.util.ArrayList;


import com.fasterxml.jackson.*;
import com.fasterxml.jackson.core.*;
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
				System.out.println(p1.childNodes[1].childNodes[0].name);
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		
		public void readJSONByTreeModel() {
			try {
				//JsonNode rootNode = mapper.readValue(jsonFile, JsonNode.class);
				JsonNode rootNode = mapper.readTree(jsonFile);
				System.out.println(rootNode.size());
				System.out.println(rootNode.get("childNodes").size());
				System.out.println(rootNode.get("childNodes").get(0).get("name"));
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		
		private class Stack {
			private final ArrayList<Object> objects;
			
			public Stack() {
				objects = new ArrayList<Object>();
			}
			
			public int size() {
				return objects.size();
			}
			public boolean push(Object o) {
				return objects.add(o);
			}
			
			public Object pop() {
				if(size() == 0) 
					return null;
				int lastIndex = size() - 1;
				Object o = objects.get(lastIndex);
				objects.remove(lastIndex);
				return o;
			}
			
			public Object top() {
				if(size() == 0) 
					return null;
				int lastIndex = size() - 1;
				Object o = objects.get(lastIndex);
				return o;
			}
		}
		
		class Node {
			public String name;
			public String id;
			public ArrayList<Node> childNodes;
		}
		
		class TopNode extends Node {
			public String publisher;
			public String version;
			public String grade;
			public String subject;
			public int fasiculeOrder;
			public String fasciculeName;
		}
		
		public void readJSONByStreamAPI() {
			try {
				JsonParser jp = new JsonFactory().createParser(jsonFile);
				JsonToken token = null;
				Stack stack = new Stack();
				Object curNode = null;
				
				for(;;) {
					token = jp.nextToken();
					if(token == null) return;
					if(token == JsonToken.START_OBJECT) {
						System.out.println("Token start object");
						if(stack.size() == 0) {
							curNode = new TopNode();
						} else {
							stack.push(curNode);
							curNode = new Node();
							if(stack.top() instanceof ArrayList<?>) {
								((ArrayList<Node>)stack.top()).add((Node)curNode);
							}
						}
						continue;
					} else if (token == JsonToken.END_OBJECT) {
						System.out.println("Token end object");
						curNode = stack.pop();
						continue;
					} else if (token == JsonToken.START_ARRAY) {
						((Node)curNode).childNodes = new ArrayList<Node>();
						stack.push(curNode);
						curNode 
						System.out.println("Token start array");
						continue;
					} else if (token == JsonToken.END_ARRAY) {
						System.out.println("Token end array");
						continue;
					} else if(jp.getCurrentName().equals("childNodes")){
						continue;
					}
					
					
					String fieldName = jp.getCurrentName();
					jp.nextToken();
					if("name".equals(fieldName)) {
						((Node)curNode).name = jp.getText();
					} else if("id".equals(fieldName)){
						((Node)curNode).id = jp.getText();
					}
				}
				
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	
	public static void main(String[] args) {
		TestMain tm = new TestMain();
		
		TestJackson tj = tm.new TestJackson("package.json");
		///tj.readJSONByDataBinding();
		//tj.readJSONByTreeModel();
		tj.readJSONByStreamAPI();
	}

}
