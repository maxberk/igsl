/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.traversal.exponential;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import org.igsl.cost.Addable;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.CostFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.traversal.CostTreeTraversal;

/**
 * A* algorithm traversal for a problem graph with edge cost and heuristics.
 */
public class AStarTreeTraversal<T,C extends Addable<C> & Comparable<C>> implements CostTreeTraversal<T,C> {
	
	/**
	 * Constructor based on a start search node, expansion operator and cost function.
	 * 
	 * @param value root node value
	 * @param cost root node cost
	 * @param heuristics heuristic function
	 * @throws NullPointerException thrown if heuristics is null
	 * @see HeuristicFunction
	 */
	public AStarTreeTraversal(T value, C cost, HeuristicFunction<T,C> heuristics) 
		throws NullPointerException
	{
		if(heuristics == null) {
			throw new NullPointerException();
		} else {
			this.heuristics = heuristics;
		}
		
		opened.put(value, new TreeNode(value, cost, heuristics.getEstimatedCost(value)));
		cursor = value;
	}
	
	/**
	 * Constructor based on a root node value and heuristic function along with default root cost value
	 * 
	 * @param value root node value
	 * @param heuristics heuristic function
	 * @throws NullPointerException thrown if heuristics is null
	 * @throws DefaultValuesUnsupportedException thrown if default root cost value does not exist
	 * @see HeuristicFunction
	 */
	public AStarTreeTraversal(T value, HeuristicFunction<T,C> heuristics) 
		throws NullPointerException, DefaultValuesUnsupportedException
	{
		this(value, heuristics.getDefaultRootCost(), heuristics);
	}
	
	/**
	 * Constructor based on a default root node and cost values and heuristic function
	 * 
	 * @param heuristics heuristic function
	 * @throws NullPointerException thrown if heuristics is null
	 * @throws DefaultValuesUnsupportedException thrown if default root node and/or cost value does not exist
	 * @see HeuristicFunction
	 */
	public AStarTreeTraversal(HeuristicFunction<T,C> heuristics) 
		throws NullPointerException, DefaultValuesUnsupportedException
	{
		this(heuristics.getDefaultRootNode(), heuristics.getDefaultRootCost(), heuristics);
	}

	/**
	 * Expands nodes based on a minimal value for a sum of node cost and
	 * cost estimation from a node to a goal. For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 * For each new node checks if a duplicated node already exits and replaces it if the cost
	 * is less.
	 */
	public void moveForward() {
		if(isEmpty()) return;
		
		List<T> result = heuristics.expand(cursor);
		TreeNode n = opened.remove(cursor);
		
		if(result == null || result.isEmpty()) {
			pruneBranch(n, cursor);
		} else {
			int added = 0;
			Iterator<T> i = result.iterator();
			
			while(i.hasNext()) {
				T t = i.next();
				
				C c = heuristics.getTransitionCost(cursor, t).addTo(n.getCost());
				C e = heuristics.getEstimatedCost(t);
				C tc = c.addTo(e);

				TreeNode o = opened.get(t);
				if(o != null) {
					if(o.getTotalCost().compareTo(tc) > 0) {
						opened.remove(t);
						pruneBranch(o, t);
						opened.put(t, new TreeNode(t, c, e, n));
						++added;
					}
				} else {
					o = closed.get(t);
					
					if(o != null) {
						if(o.getTotalCost().compareTo(tc) > 0) {
							if(o.isLeaf() == false) {
								pruneChilds(o);
							}
							closed.remove(t);
							pruneBranch(o, t);
							opened.put(t, new TreeNode(t, c, e, n));
							++added;
						}
					} else {
						opened.put(t, new TreeNode(t, c, e, n));
						++added;
					}
				}
			}
			
			if(added > 0) {
				closed.put(cursor, n);
			} else {
				pruneBranch(n, cursor);
			}
		}
		
		resetCursor();
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */	
	public void backtrack() {
		if(isEmpty()) return;
		pruneBranch(opened.remove(cursor), cursor);
		resetCursor();
	}

	/**
	 * Returns value for cursor node, null - if traversal is empty
	 */
	public T getCursor() {
		return cursor;
	}
	
	/**
	 * Returns cost for cursor node, null - if traversal is empty
	 */
	public C getCost() {
		return (cursor == null) ? null : opened.get(cursor).getCost();
	}

	/**
	 * Returns a node generator functor.
	 */
	public NodeGenerator<T> getNodeGenerator() {
		return heuristics;
	}
	
	/**
	 * Returns a cost function functor.
	 */
	public CostFunction<T,C> getCostFunction() { 
		return heuristics; 
	}

	/**
	 * Returns a list of node values from a root node to cursor including both
	 */	
	public PathIterator<T> getPath() {
		TreeNode node = null;
		
		Iterator<Entry<T,TreeNode>> i = opened.entrySet().iterator();
		
		if(i.hasNext()) {
			Entry<T,TreeNode> e = i.next();
			
			node = e.getValue();
			C minC = node.getCost();
			
			while(i.hasNext()) {
				e = i.next();
				
				C currC = e.getValue().getCost();
				if(currC.compareTo(minC) == -1) {
					node = e.getValue();
					minC = currC;
				}
			}
		}		
		
		return new PathIteratorImpl(node);	
	}

	/**
	 * Returns a list of nodes to be expanded.
	 * Nodes are ordered by cost value in the tree:
	 * from a cursor to a least "perspective" node
	 */
	public Collection<T> getLeafs() {
		return opened.keySet();
	}

	/**
	 * Depth is a number of edges from a root node to cursor.
	 */
	public int getDepth() {
		if(cursor == null) return 0;

		int result = 1;
		TreeNode n = opened.get(cursor);
		
		while(n.getParent() != null) {
			++result;
			n = closed.get(n.getParent());
		}
		
		return result;
	}

	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() {
		return opened.isEmpty();
	}
	

	private void pruneBranch(TreeNode n, T value) {
		if(n != null) {
			T t = n.getParent();
			
			if(t != null) {
				TreeNode pn = closed.get(t);
				pn.removeChild(value);
	
				while(pn.isLeaf()) {
					closed.remove(t);
					t = pn.getParent();
					if(t == null) break;
					pn = closed.get(t);
					pn.removeChild(t);
				}
			}
		}
	}
	
	private void pruneChilds(TreeNode node) {
		Iterator<T> i = node.getChilds();
		
		while(i.hasNext()) {
			T child = i.next();
			
			TreeNode n = closed.remove(child);
			if(n == null) {
				opened.remove(child);
			} else {
				pruneChilds(n);
			}
		}
		
		node.removeChilds();
	}
	
	private void resetCursor() {
		Iterator<Entry<T,TreeNode>> i = opened.entrySet().iterator();
		
		if(i.hasNext()) {
			Entry<T,TreeNode> e = i.next();
			cursor = e.getKey();
			C minC = e.getValue().getTotalCost();
			
			while(i.hasNext()) {
				e = i.next();
				
				C currC = e.getValue().getTotalCost();
				if(currC.compareTo(minC) == -1) {
					cursor = e.getKey();
					minC = currC;
				}
			}
		} else {
			cursor = null;
		}
	}
	
	private HashMap<T,TreeNode> opened = new HashMap<T,TreeNode>();
	private HashMap<T,TreeNode> closed = new HashMap<T,TreeNode>();
	private T cursor = null;
	
	private HeuristicFunction<T,C> heuristics;
	
	class TreeNode {
		T value;
		C cost, estimation;
		TreeNode parent;
		Vector<T> childs = new Vector<T>();
		
		TreeNode(T value, C cost, C estimation) {
			this.value = value;
			this.cost = cost;
			this.estimation = estimation;
			this.parent = null;
		}
		
		TreeNode(T value, C cost, C estimation, TreeNode parent) {
			this(value, cost, estimation);
			this.parent = parent;
			if(this.parent != null) {
				this.parent.addChild(this.value);
			}
		}

		T getValue() { return value; }
		C getCost() { return cost; }
		C getEstimation() { return estimation; }
		C getTotalCost() { return cost.addTo(estimation); }

		Iterator<T> getChilds() { return childs.iterator(); }
		void addChild(T child) { childs.add(child); }
		void removeChild(T child) { childs.remove(child); }
		void removeChilds() { childs.clear(); }
		
		T getParent() { return (parent == null) ? null : parent.value; }
		TreeNode getPrevious()  { return (parent == null) ? null : parent; }
		boolean isLeaf() { return childs.isEmpty(); }
	}
	
	private class PathIteratorImpl implements PathIterator<T> {
		
		private TreeNode node;

		public PathIteratorImpl(TreeNode node) {
			this.node = node;
		}

		public boolean hasPreviousNode() {
			return node != null;
		}

		public T previousNode() {
			T result = node.getValue();
			node = node.getPrevious();
			return result;
		}
		
	}	
	
}