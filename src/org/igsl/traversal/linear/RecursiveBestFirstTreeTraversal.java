/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.traversal.linear;

import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import org.igsl.cost.Addable;
import org.igsl.functor.NodeGenerator;
import org.igsl.functor.CostFunction;
import org.igsl.traversal.CostTreeTraversal;

public class RecursiveBestFirstTreeTraversal<T,C extends Addable<C> & Comparable<C>> implements CostTreeTraversal<T,C> {
	
	public RecursiveBestFirstTreeTraversal(T value, C cost, NodeGenerator<T> generator, CostFunction<T,C> function) 
		throws NullPointerException
	{
		if(generator == null || function == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
			this.function = function;
		}
		
		TreeSet<TreeNode> rootLevel = new TreeSet<TreeNode>();
		rootLevel.add(new TreeNode(value, cost));
		levels.push(rootLevel);
	}
	
	public void moveForward() {
		if(levels.isEmpty()) return;

		C bound = null;
		if(bounds.isEmpty() == false) {
			ListIterator<C> ci = bounds.listIterator(bounds.size());
			while(ci.hasPrevious()) {
				bound = ci.previous();
				if(bound != null) break;
			}
		}
		System.out.println("Bound is " + bound);

		TreeSet<TreeNode> level = levels.peek();
		TreeSet<TreeNode> nextLevel = new TreeSet<TreeNode>(new Comparator<TreeNode>() {
			public int compare(TreeNode n1, TreeNode n2) {
				return n1.getCost().compareTo(n2.getCost());
			}
		});
		
		boolean expanded = false;
		Iterator<TreeNode> li = level.iterator();
		while(li.hasNext()) {
			TreeNode n = li.next();
			
			System.out.println("Expanding " + n.getValue() + ". Results are: ");
			
			C minC = null;
			
			List<T> result = generator.expand(n.value);
			
			Iterator<T> i = result.iterator();
			while(i.hasNext()) {
				T t = i.next();
				C c = function.getTransitionCost(n.getValue(), t).addTo(n.getCost());
				
				System.out.println("Node " + t + " with cost " + c);
				
				if(bound == null || c.compareTo(bound) == -1) {
					nextLevel.add(new TreeNode(t, c, n));
				} else if(nextLevel.isEmpty()) {
					if(minC == null || c.compareTo(minC) == -1){
						minC = c;
					}
				}
			}
			
			if(nextLevel.isEmpty()) {
				n.setMinCost(minC);
				System.out.println("For node " + n.getValue() + " " + minC + " stored as min value.");
			} else {
				levels.push(nextLevel);
				
				li = nextLevel.iterator();
				li.next();
				
				C nextBound = li.hasNext() ? li.next().getCost() : null;
				bounds.push(nextBound);
				
				System.out.println("For the level " + nextBound + " stored as bound value.");
				
				expanded = true;
				break;
			}
		}
		
		if(expanded == false) {
			C prevBound = null;
			
			do {
				levels.pop();
				level = levels.peek();
				
				prevBound = bounds.pop();
			} while(!level.isEmpty() || level.first().getCost().compareTo(prevBound) == -1);
		}

	}
	
	public void backtrack() {
	}

	public C getCost() {
		if(levels.isEmpty()) {
			return null;
		} else if(levels.peek().isEmpty()) {
			return null;
		} else {
			return levels.peek().first().getCost();
		}
	}

	public T getCursor() {
		return (levels.isEmpty() || levels.peek().isEmpty()) ? null : levels.peek().first().getValue();
	}

	public int getDepth() {
		return levels.size();
	}

	public Collection<T> getLeafs() {
		return null;
	}

	public NodeGenerator<T> getNodeGenerator() {
		return generator;
	}
	
	public CostFunction<T,C> getCostFunction() { 
		return function; 
	}

	public Enumeration<T> getPath() {
		Stack<T> result = new Stack<T>();
		
		TreeNode n = (levels.isEmpty() || levels.peek().isEmpty()) ? null : levels.peek().first();
		if(n != null) {
			do {
				result.push(n.getValue());
				n = n.getParent();
			} while(n != null);
		}
		
		return result.elements();
	}

	public boolean isEmpty() {
		return levels.isEmpty();
	}

	private NodeGenerator<T> generator;
	private CostFunction<T,C> function;
	
	private Stack<TreeSet<TreeNode>> levels = new Stack<TreeSet<TreeNode>>();
	private Stack<C> bounds = new Stack<C>();
	
	class TreeNode {
		T value;
		C cost;
		C minCost;
		TreeNode parent;
		Vector<T> childs = new Vector<T>();
		
		TreeNode(T value, C cost) {
			this.value = value;
			this.cost = cost;
			this.minCost = null;
			this.parent = null;
		}
		
		TreeNode(T value, C cost, TreeNode parent) {
			this(value, cost);
			this.parent = parent;
			if(this.parent != null) {
				this.parent.addChild(this.value);
			}
		}
		
		T getValue() { return value; }
		C getCost() { return cost; }

		void addChild(T child) { childs.add(child); }
		void removeChild(T child) { childs.remove(child); }
		
		TreeNode getParent() { return parent; }
		boolean isLeaf() { return childs.isEmpty(); }
		
		void setMinCost(C minCost) {
			if(this.minCost == null || this.minCost.compareTo(minCost) > 0) {
				this.minCost = minCost;

				if(parent != null) {
					parent.setMinCost(this.minCost);
				}
			}
		}
	}

}
