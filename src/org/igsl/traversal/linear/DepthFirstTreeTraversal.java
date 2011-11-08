/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.traversal.linear;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import org.igsl.functor.NodeGenerator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.TreeTraversal;

/**
 * Depth-first search implementation for a problem graph without edge cost.
 */
public class DepthFirstTreeTraversal<T>
	implements TreeTraversal<T>, Copyable<DepthFirstTreeTraversal<T>>
{
	/**
	 * Constructor based on a start search node and expansion operator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see NodeGenerator
	 */
	public DepthFirstTreeTraversal(T value, NodeGenerator<T> generator) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
		}
		
		nodes.push(new TreeNode(value));
	}
	
	/**
	 * Constructor based on an expansion operator and default root node value
	 * 
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @throws DefaultValuesUnsupportedException thrown if default root node value does not exist
	 * @see NodeGenerator
	 */
	public DepthFirstTreeTraversal(NodeGenerator<T> generator) 
		throws NullPointerException, DefaultValuesUnsupportedException
	{
		this(generator.getDefaultRootNode(), generator);
	}
	
	/**
	 * Expands nodes base on "last found - first expanded" technique.
	 * For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		TreeNode n = nodes.peek();
		List<T> result = generator.expand(n.getValue());
		
		if(result == null || result.isEmpty()) {
			TreeNode parent = null;
			
			do {
				n = nodes.pop();
				parent = n.getParent();
			} while(parent != null && parent == nodes.peek());
		} else {
			ListIterator<T> li = result.listIterator(result.size());
			
			do {
				T p = li.previous();
				nodes.push(new TreeNode(p, n));
			} while(li.hasPrevious());
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */
	public void backtrack() {
		if(isEmpty()) return;
		
		TreeNode parent = null;
		
		do {
			TreeNode n = nodes.pop();
			parent = n.getParent();
		} while(parent != null && parent == nodes.peek());
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
	public boolean isEmpty() { return nodes.empty(); }
	
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
		ArrayList<TreeNode> parents = new ArrayList<TreeNode>();		
		
		if(!nodes.isEmpty()) {
			ListIterator<TreeNode> i = nodes.listIterator(nodes.size());
			
			while(i.hasPrevious()) {
				TreeNode n = i.previous();
				
				if(!parents.contains(n)) {
					leafs.add(n.getValue());
				}
	
				TreeNode parent = n.getParent();
				if((parent != null) && (!parents.contains(parent))) {
					parents.add(parent);
				}
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
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public DepthFirstTreeTraversal<T> getCopyOf() {
		DepthFirstTreeTraversal<T> result =	new DepthFirstTreeTraversal<T>();
		
		result.generator = generator;
		
		Iterator<TreeNode> iterator = nodes.iterator();
		while(iterator.hasNext()) {
			TreeNode node = iterator.next();

			T value = node.getValue();
			TreeNode parent = node.getParent();
			
			if(parent != null) {
				int idx = nodes.indexOf(parent);
				parent = result.nodes.elementAt(idx);
			}
			
			result.nodes.push(new TreeNode(value, parent));
		}
		
		return result;
	}
	
	private DepthFirstTreeTraversal() {}
	
	protected Stack<TreeNode> nodes = new Stack<TreeNode>();
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

}