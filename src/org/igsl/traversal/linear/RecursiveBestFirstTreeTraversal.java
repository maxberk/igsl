/**
 * Implicit Graph Search Library(C), 2009, 2010 
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
		
		PriorityQueue<TreeNode> startLevel = new PriorityQueue<TreeNode>();
		
		startLevel.add(new TreeNode(value, cost));
		nodes.push(startLevel);
	}

	/**
	 * 
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		PriorityQueue<TreeNode> level = nodes.peek(); // extract last level
		TreeNode n = level.peek(); // extract best node
		
		//System.out.println(n.getValue() + "(" + n.getCost() + "-" + n.getEstimate() + ")");
		
		List<T> result = generator.expand(n.getValue()); // expand the node
		
		if(result == null || result.isEmpty()) {
			System.out.println(" backtracking null from " + n.getValue());
			backtrack(null);
		} else { // create collection for next level
			C b = n.getBound(); // cost limit
			// calculate estimate as a minimum of parent value and next minimum value
			if(level.size() > 1) { // level has more than one node
				level.poll();
				TreeNode n2 = level.peek();
				
				C b2 = n2.getBound();
				if(b.compareTo(b2) > 0) {
					b = b2;
				}
				
				level.add(n);
			}
			
			//System.out.println("Bound for " + n.getValue() + " = " + b);

			PriorityQueue<TreeNode> q = new PriorityQueue<TreeNode>();
			C minC = null;
			
			Iterator<T> i = result.iterator();
			while(i.hasNext()) {
				T t = i.next();
				C c = n.getCost().addTo(function.getTransitionCost(n.getValue(), t));
				//System.out.println(" Testing " + t + " with cost " + c);
				
				q.add(new TreeNode(t, c, n, b));
				
				if(minC == null || minC.compareTo(c) > 0) {
					minC = c;
				}
			}
			
			if(b.compareTo(minC) < 0) {
				//System.out.println(" Backtracking " + n.getValue() + " with value " + minC);
				backtrack(minC);
			} else {
				nodes.push(q);
			}
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */	
	public void backtrack() {
		if(isEmpty()) return;
		backtrack(null);
	}

	private void backtrack(C value) {
		PriorityQueue<TreeNode> level = nodes.peek();
		TreeNode n = level.poll(); // extract best node
		
		System.out.println("node " + n.getValue() + " with cost " + n.getCost() + " with est " + n.getEstimate());
		
		if(value != null) {
			n.setEstimate(value);
			level.add(n); // put it back
		}
		
		if(level.size() > 0) {
			n = level.peek(); // read best node
			//System.out.println("Level size is " + level.size()  + "."
					//+ " Next best node is " + n.getValue()
					//+ " with cost " + n.getCost()
					//+ " and estimate " + n.getEstimate());
			TreeNode p = n.getParent();
			
			if(p != null && n.compareTo(p) > 0) {
				nodes.pop();
				System.out.println("1: backtrack level = " + nodes.size() + " value = " + n.getEstimate());
				backtrack(n.getEstimate());
			}
		} else {
			nodes.pop();
			System.out.println("2: backtrack level = " + nodes.size() + " value = " + value);
			backtrack(value);
		}
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
	
	class TreeNode implements Comparable<TreeNode> {
		T value;
		C cost, estimate;
		TreeNode parent;
		
		TreeNode(T value, C cost) {
			this.value = value;
			this.cost = cost;
			this.estimate = null;
			this.parent = null;
		}
		
		TreeNode(T value, C cost, TreeNode parent, C estimate) {
			this(value, cost);
			this.parent = parent;
			this.estimate = estimate;
		}
		
		T getValue() { return value; }
		C getCost() { return cost; }
		
		C getEstimate() { return estimate; }
		void setEstimate(C value) { this.estimate = value; }
		
		C getBound() { return (estimate == null) ? cost : estimate; } 
		
		TreeNode getParent() { return parent; }
		
		public int compareTo(TreeNode other) {
			return getBound().compareTo(other.getBound());
		}
	}

}