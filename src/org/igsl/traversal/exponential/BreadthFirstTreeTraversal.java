/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.traversal.exponential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.TreeTraversal;

/**
 * Breadth-first search implementation for a problem graph without edge cost.
 */
public class BreadthFirstTreeTraversal<T> implements TreeTraversal<T> {
	
	/**
	 * Constructor based on a start search node and expansion operator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see NodeGenerator
	 */
	public BreadthFirstTreeTraversal(T value, NodeGenerator<T> generator) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
		}
		
		nodes.enqueue(new TreeNode(value));
	}

	/**
	 * Expands nodes base on "first found - first expanded" techniques.
	 * For an empty traversal returns without any action.
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		TreeNode n = nodes.dequeue();
		List<T> result = generator.expand(n.getValue());
		
		if(result != null && !result.isEmpty()) {
			parents.add(n);
			
			Iterator<T> i = result.iterator();
			while(i.hasNext()) {
				nodes.enqueue(new TreeNode(i.next(), n));
			}
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */
	public void backtrack() {
		if(isEmpty()) return;
		nodes.dequeue();
	}
	
	/**
	 * Returns value for cursor node, null - if traversal is empty
	 */
	public T getCursor() { return isEmpty() ? null : nodes.peek().getValue(); }
	
	/**
	 * Returns a node generator functor.
	 */
	public NodeGenerator<T> getNodeGenerator() { return generator; }
	
	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() { return nodes.isEmpty(); }
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public Enumeration<T> getPath() {
		Stack<T> result = new Stack<T>();
		
		if(!nodes.isEmpty()) {
			TreeNode n = nodes.peek();
			
			while(n != null) {
				result.push(n.getValue());
				n = n.getParent();
			}
		}
		
		return result.elements();
	}
	
	/**
	 * Returns a list of nodes to be expanded.
	 * Nodes are ordered by expansion priority in the tree:
	 * from a cursor to a least "perspective" node
	 */
	public Collection<T> getLeafs() {
		ArrayList<T> leafs = new ArrayList<T>();		
		
		if(!nodes.isEmpty()) {
			Enumeration<TreeNode> e = nodes.asEnumeration();
			while(e.hasMoreElements()) {
				leafs.add(e.nextElement().getValue());
			}
		}
		
		return leafs;
	}
	
	/**
	 * Depth is a number of edges from a root node to cursor.
	 */
	public int getDepth() {
		if(isEmpty()) {
			return -1;
		}
		
		int result = 0;
		
		TreeNode n = nodes.peek();
		while(n.getParent() != null) {
			++result;
			n = n.getParent();
		}
		
		return result;
	}
	
	protected BreadthFirstTreeTraversal() {}
	
	protected Deque<TreeNode> nodes = new Deque<TreeNode>();
	protected ArrayList<TreeNode> parents = new ArrayList<TreeNode>();
	
	protected NodeGenerator<T> generator;
	
	protected class TreeNode {
		T value;
		TreeNode parent;
		
		TreeNode(T value) {
			this.value = value;
			this.parent = null;
		}
		
		TreeNode(T value, TreeNode parent) {
			this(value);
			this.parent = parent;
		}
		
		T getValue() { return value; }
		TreeNode getParent() { return parent; }
	}
	
	class Deque<N> {
		boolean processed;
		Stack<N> a1;
		Stack<N> a2;
		
		Deque() {
			processed = false;
			a1 = new Stack<N>();
			a2 = new Stack<N>();
		}
		
		void enqueue(N n) {
			a1.push(n);
			processed = false;
		}
		
		N dequeue() {
			process();
			return a2.pop();
		}
		
		N peek() {
			process();
			return a2.peek();
		}
		
		boolean isEmpty() {
			process();
			return a2.isEmpty();
		}
		
		private void process() {
			if(!processed) {
				if(a2.isEmpty()) {
					if(a1.isEmpty()) {
						throw new EmptyStackException();
					} else {
						while(!a1.isEmpty()) {
							a2.push(a1.pop());
						}
					}
				}
				processed = true;
			}
		}
		
		Enumeration<N> asEnumeration() {
			process();
			return a2.elements();
		}
		
	}

}
