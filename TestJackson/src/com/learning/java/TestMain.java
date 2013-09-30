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
		
		class  IncmentalPrint {
			private String text;
			private int incs;
			public IncmentalPrint(String text, int incs) {
				this.text = text;
				this.incs = incs;
				for(int i = 0; i < incs; i++)
					System.out.print(">>");
				System.out.println(text);
			}
		}
		
		public void readJSONByStreamAPI() {
			try {
				JsonParser jp = new JsonFactory().createParser(jsonFile);
				JsonToken token = null;
				Stack stack = new Stack();
				TopNode topNode = null;
				Object curNode = null;
				
				for(;;) {
					token = jp.nextToken();
					if(token == null) break;
					if(token == JsonToken.START_OBJECT) {
						System.out.println("Token start object");
						if(stack.size() == 0) {
							curNode = topNode = new TopNode();
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
						curNode = ((Node)curNode).childNodes; 
						System.out.println("Token start array");
						continue;
					} else if (token == JsonToken.END_ARRAY) {
						curNode = stack.pop();
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
				displayNodeByRecursive(topNode, 0);
//				displayNodeWithoutRecusive(topNode);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		
		public void displayNodeByRecursive(Node node, int level) {
			new IncmentalPrint(node.name, level);
			if(node.childNodes == null) return;
			for(Node child : node.childNodes) {
				displayNodeByRecursive(child, level + 1);
			}
		}
		
		public void displayNodeWithoutRecusive(Node node) {
			class PairNode{
				public int index;
				public Node node;
				public PairNode(Node node, int index){
					this.index = index;
					this.node = node;
				}
			}
			
			ArrayList<PairNode> iStack = new ArrayList<PairNode>();
			iStack.add(new PairNode(node,0));
			System.out.println(iStack.get(0).node.name);
			for(;;){
				PairNode pairNode = iStack.get(iStack.size() - 1);
				if(pairNode.node.childNodes != null && pairNode.index < pairNode.node.childNodes.size() ) {
					iStack.add(new PairNode(pairNode.node.childNodes.get(pairNode.index), 0));
					pairNode.index++;
					new IncmentalPrint(iStack.get(iStack.size() - 1).node.name, iStack.size() - 1);
//					if(pairNode.index == 2) {
//						new IncmentalPrint(pairNode.node.name, iStack.size() - 1).print();
//					}
				} else {
//					if(iStack.get(iStack.size() - 1).node.childNodes == null) {
//						new IncmentalPrint(pairNode.node.name, iStack.size() - 1).print();
//					}
					iStack.remove(iStack.size() - 1);
				}
				if(iStack.size() == 0) break;

//				if(node.childNodes != nuh`ll && index == 0) {
//					iStack.add(new PairNode(index, node));
//					nodes = node.childNodes;
//					node = nodes.get(index);
//				} else if(++index < nodes.size()) {
//					node = iStack.get(iStack.size() - 1).node.childNodes.get(index);
//				} else {
//					do {
//						iStack.remove(iStack.size() - 1);
//						if(iStack.size() == 0) return;
//						PairNode pn  = iStack.get(iStack.size() -1);
//						index = ++pn.index;
//						node = pn.node;
//					} while(index >= node.childNodes.size());
//					nodes = node.childNodes;
//					node = nodes.get(index);
//					index = 0;
//				}
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
