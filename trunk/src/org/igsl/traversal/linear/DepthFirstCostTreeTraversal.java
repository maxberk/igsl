/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013
 */

package org.igsl.traversal.linear;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import org.igsl.cost.Addable;
import org.igsl.functor.CostFunction;
import org.igsl.functor.PathIterator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.CostTreeTraversal;
import org.igsl.traversal.Splitable;

/**
 * Depth-first search implementation for a problem graph with edge cost.
 */
public class DepthFirstCostTreeTraversal<T,C extends Addable<C> & Comparable<C>>
	implements CostTreeTraversal<T,C>, Copyable<DepthFirstCostTreeTraversal<T,C>>,
		Splitable<DepthFirstCostTreeTraversal<T,C>>
{
	/**
	 * Constructor based on a start search node and cost function interface
	 * 
	 * @param value root node value
	 * @param cost root node cost
	 * @param function cost function
	 * @throws NullPointerException thrown if cost function is null
	 * @see CostFunction
	 */
	public DepthFirstCostTreeTraversal(T value, C cost, CostFunction<T,C> function) 
		throws NullPointerException
	{
		if(function == null) {
			throw new NullPointerException();
		} else {
			this.function = function;
		}
		
		nodes.push(new TreeNode(value, cost));
	}
	
	/**
	 * Constructor based on a start search node with cost function and default root cost value
	 * 
	 * @param value root node value
	 * @param function cost function
	 * @throws NullPointerException thrown if cost function is null
	 * @throws DefaultValuesUnsupportedException thrown if default root cost value does not exist
	 * @see CostFunction
	 */
	public DepthFirstCostTreeTraversal(T value, CostFunction<T,C> function) 
		throws NullPointerException, DefaultValuesUnsupportedException
	{
		this(value, function.getDefaultRootCost(), function);
	}
	
	/**
	 * Constructor based on a cost function interface and default root node and cost values
	 * 
	 * @param function cost function
	 * @throws NullPointerException thrown if cost function is null
	 * @throws DefaultValuesUnsupportedException thrown if default root node and/or cost value does not exist
	 * @see CostFunction
	 */
	public DepthFirstCostTreeTraversal(CostFunction<T,C> function) 
		throws NullPointerException, DefaultValuesUnsupportedException
	{
		this(function.getDefaultRootNode(), function.getDefaultRootCost(), function);
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
		
		List<T> result = function.expand(getPathIterator());
		
		if(result == null) {
			return false;
		} else {
			if(result.isEmpty()) {
				prune();
			} else {
				ListIterator<T> li = result.listIterator(result.size());
			
				do {
					T t = li.previous();
				
					nodes.push(new TreeNode(
							t, n.getCost().addTo(function.getTransitionCost(n.getValue(),t)), n
							));
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
		try {
			prune();
		} catch(EmptyStackException ese) {
			throw new EmptyTraversalException();
		}
	}
	
	/**
	 * Returns cost for cursor node, null - if traversal is empty
	 */
	public C getCost() { return isEmpty() ? null : nodes.peek().getCost(); }

	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() { return nodes.empty(); }
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public PathIterator<T> getPathIterator() {
		return pathIterator.reset(nodes.peek());
	}
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public PathIterator<T> getPath() {
		return new NodeIteratorImpl(nodes.peek());
	}

	/**
	 * Implementation details of Copyable interface.
	 * Returns a DepthFirstCostTreeTraversal with a copy of a cursor node
	 */
	public DepthFirstCostTreeTraversal<T,C> getCopyOf() {
		DepthFirstCostTreeTraversal<T,C> result = new DepthFirstCostTreeTraversal<T,C>();
		
		result.function = function;
		
		Iterator<TreeNode> iterator = nodes.iterator();
		while(iterator.hasNext()) {
			TreeNode node = iterator.next();

			T value = node.getValue();
			C cost = node.getCost();
			TreeNode parent = node.getParent();
			
			if(parent != null) {
				int idx = nodes.indexOf(parent);
				parent = result.nodes.elementAt(idx);
			}
			
			result.nodes.push(new TreeNode(value, cost, parent));
		}
		
		return result;
	}
	
	/**
	 * Implementation details of Splittable interface.
	 * Returns a DepthFirstCostTreeTraversal with a copy of a cursor node
	 */
	public DepthFirstCostTreeTraversal<T,C> split() throws EmptyTraversalException {
		try {
			DepthFirstCostTreeTraversal<T,C> result = 
				new DepthFirstCostTreeTraversal<T,C>(nodes.peek().getValue(), getCost(), function);
			
			backtrack();
			return result;
		} catch(EmptyStackException ese) {
			throw new EmptyTraversalException();
		}
	}
	
	private DepthFirstCostTreeTraversal() {}
	
	private void prune() {
		TreeNode parent = null;
		
		do {
			TreeNode n = nodes.pop();
			parent = n.getParent();
		} while(parent != null && parent == nodes.peek());
	}
	
	protected Stack<TreeNode> nodes = new Stack<TreeNode>();
	protected CostFunction<T,C> function;
	private NodeIteratorImpl pathIterator = new NodeIteratorImpl(null);
	
	class TreeNode {
		T value;
		C cost;
		TreeNode parent;
		
		TreeNode(T value, C cost) {
			this.value = value;
			this.cost = cost;
			this.parent = null;
		}
		
		TreeNode(T value, C cost, TreeNode parent) {
			this(value, cost);
			this.parent = parent;
		}
		
		T getValue() { return value; }
		C getCost() { return cost; }
		TreeNode getParent() { return parent; }
	}
	
	private class NodeIteratorImpl implements PathIterator<T> {
		
		private TreeNode cursor;

		public NodeIteratorImpl(TreeNode node) {
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
		
		private PathIterator reset(TreeNode node) {
			this.cursor = node;
			return this;
		}

	}

}