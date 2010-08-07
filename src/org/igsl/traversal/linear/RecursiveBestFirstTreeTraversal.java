/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */
package org.igsl.traversal.linear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Stack;

import org.igsl.cost.Addable;
import org.igsl.functor.CostFunction;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.CostTreeTraversal;

/**
 * Recursive best-first search implementation for a problem graph with an edge cost function.
 */
public class RecursiveBestFirstTreeTraversal<T,C extends Addable<C> & Comparable<C>>
	implements CostTreeTraversal<T,C>
{
	/**
	 * Constructor based on a start search node, expansion operator and heuristic function
	 * 
	 * @param value root node value
	 * @param cost root node cost
	 * @param function cost function
	 * @throws NullPointerException thrown if heuristic function is null
	 * @see HeuristicFunction
	 */
	public RecursiveBestFirstTreeTraversal(T value, C cost, HeuristicFunction<T,C> heuristics) 
		throws NullPointerException
	{
		if(heuristics == null) {
			throw new NullPointerException();
		} else {
			this.heuristics = heuristics;
		}
		
		TreeNode root = new TreeNode(value, cost, this.heuristics.getEstimatedCost(value)); 
		
		levels.push(new Level(root, root.getCost()));
	}

	/**
	 * On each level expand a node with a minimal cost and for a subtree with this node as a root set
	 * a bound value as a minimum of a current bound and a second minimal cost among brothers. If all
	 * nodes' costs exceeds the level bound or the result of node expansion is empty performs backtracking.
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		Level level = levels.peek();
		TreeNode n = level.peek(); // extract best node
		C b = level.getBound();
		
		List<T> result = heuristics.expand(n.getValue()); // expand the node
		
		if(result == null || result.isEmpty()) {
			backtrack(null);
		} else { // create collection for next level
			Iterator<T> i = result.iterator();
			Level newLevel = new Level();
			boolean addOk = false;
			C minC = null;
			
			while(i.hasNext()) {
				T t = i.next();
				C gcost = n.getGCost().addTo(heuristics.getTransitionCost(n.getValue(), t));
				C hcost = heuristics.getEstimatedCost(t);
				
				TreeNode ancestor = new TreeNode(t, gcost, hcost, n);
				C fcost = ancestor.getCost();
				newLevel.add(ancestor);
				
				if(fcost.compareTo(b) <= 0) {
					addOk = true;
				} else if(minC == null || minC.compareTo(fcost) > 0) {
					minC = fcost;
				}
			}
			
			if(addOk == false) {
				backtrack(minC);
			} else {
				if(level.size() > 1) {
					level.poll();

					C newB = level.peek().getBound();
					if(newB.compareTo(b) < 0) {
						b = newB;
					}
					
					level.add(n);
				}
				
				newLevel.setBound(b);
				levels.push(newLevel);
			}
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */	
	public void backtrack() {
		backtrack(null);
	}

	private void backtrack(C value) {
		if(isEmpty()) return;
		
		Level level = levels.peek();
		TreeNode n = level.poll(); // extract best node
		
		if(value != null) { // return back if value is not null
			n.setEstimate(value);
			level.add(n);
		} else if(level.size() == 0) {
			levels.pop();
			backtrack(null);
			return;
		}
		
		n = level.peek(); // re-read best node
		TreeNode p = n.getParent();
		
		if(p == null) {
			level.setBound(value);
		} else if(p.getBound().compareTo(n.getBound()) < 0) {
			levels.pop();
			backtrack(n.getBound());
		}
	}
	
	/**
	 * Returns value for cursor node, null - if traversal is empty
	 */
	public T getCursor() { return isEmpty() ? null : levels.peek().peek().getValue(); }
	
	/**
	 * Returns cost for cursor node, null - if traversal is empty
	 */
	public C getCost() { return isEmpty() ? null : levels.peek().peek().getCost(); }

	/**
	 * Returns a node generator functor.
	 */
	public NodeGenerator<T> getNodeGenerator() { return heuristics; }

	/**
	 * Returns a cost function functor.
	 */
	public CostFunction<T,C> getCostFunction() { return heuristics; }

	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() { return levels.empty(); }
	
	/**
	 * Returns a list of node values from a root node to cursor including both
	 */	
	public Enumeration<T> getPath() {
		Stack<T> result = new Stack<T>();
		
		if(!levels.isEmpty()) {
			TreeNode n = levels.peek().peek();
			
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
		
		if(!levels.isEmpty()) {
			ListIterator<Level> li = levels.listIterator(levels.size());
			
			while(li.hasPrevious()) {
				PriorityQueue<TreeNode> q = li.previous();
				
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
		return isEmpty() ? -1 : levels.size();
	}
	
	private Stack<Level> levels = new Stack<Level>();
	
	private HeuristicFunction<T,C> heuristics;
	
	class TreeNode implements Comparable<TreeNode> {
		T value;
		C gcost, hcost, estimate;
		TreeNode parent;
		
		TreeNode(T value, C gcost, C hcost) {
			this.value = value;
			this.gcost = gcost;
			this.hcost = hcost;
			this.estimate = null;
			this.parent = null;
		}
		
		TreeNode(T value, C gcost, C hcost, TreeNode parent) {
			this(value, gcost, hcost);
			this.parent = parent;
		}
		
		T getValue() { return value; }
		
		C getGCost() { return gcost; }
		C getHCost() { return hcost; }
		C getCost() { return gcost.addTo(hcost); }
		
		C getEstimate() { return estimate; }
		void setEstimate(C value) {
			this.estimate = value;
		}
		
		C getBound() { return (estimate == null) ? getCost() : estimate; } 
		
		TreeNode getParent() { return parent; }
		
		public int compareTo(TreeNode other) {
			return getBound().compareTo(other.getBound());
		}
	}
	
	class Level extends PriorityQueue<TreeNode> {
		C bound;
		
		public Level() {
			super();
			this.bound = null;
		}
		
		public Level(TreeNode n, C bound) {
			this();
			add(n);
			this.bound = bound;
		}
		
		public C getBound() {
			return bound;
		}
		
		public void setBound(C value) {
			this.bound = value;
		}
		
	}

}