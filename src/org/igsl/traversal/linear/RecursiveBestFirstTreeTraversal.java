/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.traversal.linear;

import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Stack;

import org.igsl.cost.Addable;
import org.igsl.functor.CostFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.CostTreeTraversal;

/**
 * Recursive best-first search implementation for a problem graph with edge cost.
 */
public class RecursiveBestFirstTreeTraversal<T,C extends Addable<C> & Comparable<C>>
	implements CostTreeTraversal<T,C>
{
	/**
	 * Constructor based on a start search node, expansion operator and cost function
	 * 
	 * @param value root node value
	 * @param cost root node cost
	 * @param generator node generator function
	 * @param function cost function
	 * @throws NullPointerException thrown if either node generator or cost function is null
	 * @see NodeGenerator
	 * @see CostFunction
	 */
	public RecursiveBestFirstTreeTraversal(T value, C cost, NodeGenerator<T> generator, CostFunction<T,C> function) 
		throws NullPointerException
	{
		if(generator == null || function == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
			this.function = function;
		}
		
		PriorityQueue<TreeNode> startLevel = new PriorityQueue<TreeNode>(11, new Comparator<TreeNode>() {
			public int compare(TreeNode n1, TreeNode n2) {
				C max1 = n1.getBound().compareTo(n1.getCost()) > 0 ? n1.getBound() : n1.getCost();
				C max2 = n2.getBound().compareTo(n2.getCost()) > 0 ? n2.getBound() : n2.getCost();
				
				return max1.compareTo(max2);
			};
		});
		
		startLevel.add(new TreeNode(value, cost));
		nodes.push(startLevel);
	}

	/**
	 * 
	 */
	public void moveForward() {
		if(isEmpty()) return;
	
		TreeNode n = nodes.peek().peek();
		List<T> result = generator.expand(n.getValue());
		
		if(result == null || result.isEmpty()) {
			backtrack(null);
		} else {
			PriorityQueue<TreeNode> q = new PriorityQueue<TreeNode>(11, new Comparator<TreeNode>() {
				public int compare(TreeNode n1, TreeNode n2) {
					C max1 = n1.getBound().compareTo(n1.getCost()) > 0 ? n1.getBound() : n1.getCost();
					C max2 = n2.getBound().compareTo(n2.getCost()) > 0 ? n2.getBound() : n2.getCost();
					return max1.compareTo(max2);
				};
			});
			
			C b = n.getBound();
			C minC = null;
					
			Iterator<T> i = result.iterator();
			while(i.hasNext()) {
				T t = i.next();
				C c = n.getCost().addTo(function.getTransitionCost(n.getValue(), t));
				
				if(b == null || b.compareTo(c) > 0) {
					q.add(new TreeNode(t, c, n, b));
				} else if(minC == null || minC.compareTo(c) > 0) {
					minC = c;
				}
			}
			
			if(q.isEmpty()) {
				backtrack(minC);
			} else {
				nodes.push(q);
			}
		}
	}

	/**
	 * 
	 */	
	public void backtrack() {
		backtrack(null);
	}
	
	private void backtrack(C value) {
		if(isEmpty()) return;
		
		PriorityQueue<TreeNode> level = nodes.peek(); 
		TreeNode n = level.peek();
		
		n.setBound(value);
		n = level.peek();
		TreeNode p = n.getParent();

		while(p != null && n.getBound().compareTo(p.getBound()) > 0) {
			p.setBound(n.getBound()); // set bound for parent
			nodes.pop(); // removes current level
			level = nodes.peek();
			n = level.peek();
			p = n.getParent();
		};
	}
	
	/**
	 * Returns value for cursor node, null - if traversal is empty
	 */
	public T getCursor() { return isEmpty() ? null : nodes.peek().peek().getValue(); }
	
	/**
	 * Returns cost for cursor node, null - if traversal is empty
	 */
	public C getCost() { return isEmpty() ? null : nodes.peek().peek().getCost(); }

	/**
	 * Returns a node generator functor.
	 */
	public NodeGenerator<T> getNodeGenerator() { return generator; }

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
			TreeNode n = nodes.peek().peek();
			
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
			ListIterator<PriorityQueue<TreeNode>> iq = nodes.listIterator(nodes.size());
			while(iq.hasPrevious()) {
				PriorityQueue<TreeNode> q = iq.previous();
				
				Iterator<TreeNode> in = q.iterator();
				
				while(in.hasNext()) {
					TreeNode n = in.next();
					
					if(!parents.contains(n)) {
						leafs.add(n.getValue());
					}
					
					TreeNode parent = n.getParent();
					if((parent != null) && (!parents.contains(parent))) {
						parents.add(parent);
					}
				}
			}
		}
		
		return leafs;
	}

	/**
	 * Depth is a number of edges from a root node to cursor.
	 */
	public int getDepth() {
		return isEmpty() ? -1 : nodes.size();
	}
	
	private Stack<PriorityQueue<TreeNode>> nodes = new Stack<PriorityQueue<TreeNode>>();
	
	private NodeGenerator<T> generator;
	private CostFunction<T,C> function;
	
	class TreeNode {
		T value;
		C cost, bound;
		TreeNode parent;
		
		TreeNode(T value, C cost) {
			this.value = value;
			this.cost = cost;
			this.parent = null;
			this.bound = cost;
		}
		
		TreeNode(T value, C cost, TreeNode parent, C bound) {
			this.value = value;
			this.cost = cost;
			this.parent = parent;
			this.bound = bound;
		}
		
		T getValue() { return value; }
		C getCost() { return cost; }
		
		C getBound() { return bound; }
		void setBound(C bound) { this.bound = bound; }
		
		TreeNode getParent() { return parent; }
	}

}