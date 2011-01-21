/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.traversal.linear;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import org.igsl.cost.Addable;
import org.igsl.functor.CostFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.CostTreeTraversal;

/**
 * Depth-first search implementation for a problem graph with edge cost.
 */
public class DepthFirstCostTreeTraversal<T,C extends Addable<C> & Comparable<C>>
	implements CostTreeTraversal<T,C>
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
	 * Expands nodes base on "last found - first expanded" technique.
	 * For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		TreeNode n = nodes.peek();
		List<T> result = function.expand(n.getValue());
		
		if(result == null || result.isEmpty()) {
			TreeNode parent = null;
			
			do {
				n = nodes.pop();
				parent = n.getParent();
			} while(parent != null && parent == nodes.peek());
		} else {
			ListIterator<T> li = result.listIterator(result.size());
			
			do {
				T t = li.previous();
				
				nodes.push(new TreeNode(
					t, n.getCost().addTo(function.getTransitionCost(n.getValue(),t)), n
				));
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
	 * Returns cost for cursor node, null - if traversal is empty
	 */
	public C getCost() { return isEmpty() ? null : nodes.peek().getCost(); }

	/**
	 * Returns a node generator functor.
	 */
	public NodeGenerator<T> getNodeGenerator() { return function; }

	/**
	 * Returns a cost function functor.
	 */
	public CostFunction<T,C> getCostFunction() { return function; }

	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() { return nodes.empty(); }
	
	/**
	 * Returns a list of node values from a root node to cursor including both
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

	protected DepthFirstCostTreeTraversal() {}
	
	protected Stack<TreeNode> nodes = new Stack<TreeNode>();
	protected CostFunction<T,C> function;
	
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

}