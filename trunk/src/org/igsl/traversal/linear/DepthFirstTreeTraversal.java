/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013 
 */

package org.igsl.traversal.linear;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import org.igsl.functor.NodeGenerator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.TreeTraversal;
import org.igsl.functor.BackwardPathIterator;

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
	public boolean moveForward() throws EmptyTraversalException {
		TreeNode n = null;
		
		try {
			n = nodes.peek();
		} catch (EmptyStackException ese) {
			throw new EmptyTraversalException();
		}
		
		List<T> result = generator.expand(getPathIterator());
		
		if(result == null) {
			return false;
		} else {
			if(result.isEmpty()) {
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
			
			return true;
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */
	public void backtrack() throws EmptyTraversalException {
		TreeNode n = null;
		
		try {
			n = nodes.pop();
		} catch(EmptyStackException ese) {
			throw new EmptyTraversalException();
		}

		TreeNode parent = n.getParent();;

		while(parent != null && parent == nodes.peek()) {
			n = nodes.pop();
			parent = n.getParent();
		}
	}
	
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
	public BackwardPathIterator<T> getPathIterator() {
		return pathIterator.reset(nodes.peek());
	}
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public BackwardPathIterator<T> getPath() {
		return new PathIteratorImpl(nodes.peek());
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
	private PathIteratorImpl pathIterator = new PathIteratorImpl(null);
	
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
	
	private class PathIteratorImpl implements BackwardPathIterator<T> {
		
		private TreeNode cursor;

		public PathIteratorImpl(TreeNode node) {
			this.cursor = node;
		}

		public boolean hasPreviousNode() {
			return cursor != null;		
		}

		public T previousNode() {
			T result = cursor.getValue();
			cursor = cursor.getParent();
			return result;
		}
		
		private BackwardPathIterator reset(TreeNode node) {
			this.cursor = node;
			return this;
		}
		
	}

}