package com.learning.java;

import java.util.Random;
import java.util.Stack;

class Node {
	String value;
	Node leftNode;
	Node rightNode;
	public Node(String value) {
		this.value = value;
	}
}

class Pair<P1, P2> {
	public P1 first;
	public P2 second;
	public Pair(P1 first, P2 second) {
		this.first = first;
		this.second = second;
	}
}


class Tree {
	Node rootNode;
	
	public Tree() {
		rootNode = new Node("Root Node");
	}
	
	public void randomCreateChildNodes(Node node, int level) {
		Random random = new Random();
		if(random.nextBoolean() || level % 2 == 0) {
			node.leftNode = new Node("left node at level:" + level);
		}
		if(random.nextBoolean() || level % 2 == 1) {
			node.rightNode = new Node("right node at level:" + level);
		}
	}

	public void mockConstruct() {
		// TODO Auto-generated method stub
		int i = 0;
		
		Stack<Pair<Integer, Node>> stack = new Stack<Pair<Integer, Node>>();
		Node node = rootNode;
		while(node != null) {
			if( ++i < 5)
				randomCreateChildNodes(node, i);
			if(node.leftNode != null) {
				if(node.rightNode != null)
					stack.push(new Pair<Integer, Node>(i, node.rightNode));
				node = node.leftNode;
			} else if(node.rightNode != null ) {
				node = node.rightNode;
			} else {
				if(stack.empty()) break;
				Pair<Integer, Node> pn = stack.pop();
				node = pn.second;
				i = pn.first;
			}
		}
	}
	
	public void visit(Node node) {
		System.out.println("node is:" + node.value);
	}
	public void traversePreorder(Node node) {
		if(node == null) return;
		visit(node);
		traversePreorder(node.leftNode);
		traversePreorder(node.rightNode);
	}
	
	public void traversePreorderIterative(Node node) {
		Stack<Node> stack = new Stack<Node>();
		
		while(node != null || !stack.empty()) {
			if(node != null) {
				visit(node);
				if(node.rightNode != null) 
					stack.push(node.rightNode);
				node = node.leftNode;
			} else {
				node = stack.pop();
			}
		}
	}
	
	public void traverseInorder(Node node) {
		if(node == null) return;
		traverseInorder(node.leftNode);
		visit(node);
		traverseInorder(node.rightNode);
	}
	
	public void traverseInorderIterative(Node node) {
		Stack<Node> stack = new Stack<Node>();
		while(node != null || !stack.empty()) {
			if(node != null) {
				stack.push(node);
				node = node.leftNode;
			} else {
				node = stack.pop();
				visit(node);
				node = node.rightNode;
			}
		}
	}
	
	public void traversePostorder(Node node) {
		if(node == null) return;
		traversePostorder(node.leftNode);
		traversePostorder(node.rightNode);
		visit(node);
		
	}
	
	public void traversePostorderIterative(Node node) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(node);
		Node prevNode = null;
		while(!stack.empty()) {
			node = stack.peek();
			if(prevNode == null || node == prevNode.leftNode || node == prevNode.rightNode) {
				if(node.leftNode != null) {
					stack.push(node.leftNode);
				} else if (node.rightNode != null) {
					stack.push(node.rightNode);
				}
			} else if( node.leftNode == prevNode) {
				if(node.rightNode != null)
					stack.push(node.rightNode);
			} else {
				stack.pop();
				visit(node);
			}
			
			prevNode = node;
		}
	}
}
public class TreeVisitorTest {
	public static void start() {
		Tree tree = new Tree();
		tree.mockConstruct();
		
//		tree.traversePreorder(tree.rootNode);
//		tree.traversePreorderIterative(tree.rootNode);
		
		tree.traverseInorder(tree.rootNode);
		tree.traverseInorderIterative(tree.rootNode);
		
//		tree.traversePostorder(tree.rootNode);
//		tree.traversePostorderIterative(tree.rootNode);
		
		
		
	}
}
