package org.igsl.traversal.linear.finite;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import java.util.EmptyStackException;
import java.util.ArrayList;

import org.igsl.functor.generator.FiniteDepthNodeGenerator;
import org.igsl.functor.iterator.values.ValuesIterator;

import org.igsl.functor.exception.EmptyTraversalException;

import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;

import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.functor.iterator.path.ForwardPathIterator;

/**
 * Depth-first search implementation for a problem graph without edge cost.
 */
public class FiniteDepthTreeTraversal<T>
	implements TreeTraversal<T>, Copyable<FiniteDepthTreeTraversal<T>>
{
	/**
	 * Constructor based on a start search node and expansion operator
	 * 
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see FiniteDepthNodeGenerator
	 */
	public FiniteDepthTreeTraversal(FiniteDepthNodeGenerator<T> generator) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
			
			int maxDepth = generator.getMaxDepth();
			this.stack = new ArrayList<ValuesIterator<T>>(maxDepth);
			for(int i = 0; i < maxDepth; ++i) {
				this.stack.add(generator.createValues(i));
			}
			
			this.depth = 1;
			this.stack.get(this.depth-1).update(null);
		}
	}
	
	/**
	 * Expands nodes base on "last found - first expanded" technique.
	 * For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 */
	public boolean moveForward() throws EmptyTraversalException {
		if(depth == 0 || generator.isGoal(getPathIterator())) { // empty
			return false;
		} else {
			ValuesIterator<T> iterator = stack.get(depth-1);
			boolean valid = false;
			
			while(iterator.hasNext()) {
				T value = iterator.next();
				
				valid = generator.isValidTransition(value, getPathIterator(depth-1));
				
				if(valid) {
					if(depth < stack.size()) {
						iterator = stack.get((++depth) - 1);
						iterator.update(getPathIterator());
					}

					break;
				}
			}
			
			if(!valid) {
				backtrack();
			}
				
			return true;			
		}
	}
	
	/**
	 * Simply prunes the cursor node and its predecessors if necessary
	 * till a ready-for-expansion node is found.
	 */
	public void backtrack() throws EmptyTraversalException {
		if(depth == 0) {
			throw new EmptyTraversalException();
		} else { // depth > 0
			do {
				ValuesIterator<T> iterator = stack.get(depth-1);
				boolean valid = false;
				
				while(iterator.hasNext()) {
					T value = iterator.next();

					valid = generator.isValidTransition(value, getPathIterator(depth-1));
					
					if(valid) {
						break;
					}
				}
				
				if(valid) {
					break;
				}
			} while(--depth > 0);
		}
	}
	
	/**
	 * Check if traversal has no nodes to expand
	 */
	public boolean isEmpty() { return depth == 0; }
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public BackwardPathIterator<T> getPathIterator() {
		return getPathIteratorImpl(depth);
	}
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public BackwardPathIterator<T> getPathIterator(int depthValue) {
		return getPathIteratorImpl(depthValue);
	}	
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public BackwardPathIterator<T> getPath() {
		return new PathIteratorImpl(this, depth);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public FiniteDepthTreeTraversal<T> getCopyOf() {
		FiniteDepthTreeTraversal<T> result =	new FiniteDepthTreeTraversal<T>();
		return result;
	}
	
	private PathIteratorImpl getPathIteratorImpl(int depthValue) {
		if(pathIterator == null) {
			pathIterator = new PathIteratorImpl(this, depthValue);
		} else {
			pathIterator.reset(depthValue);
		}
		
		return pathIterator;
	}
	
	private FiniteDepthTreeTraversal() {}
	
	protected FiniteDepthNodeGenerator<T> generator;
	
	protected ArrayList<ValuesIterator<T>> stack;
	protected int depth;
	protected boolean initialized;
	
	private PathIteratorImpl pathIterator;
	
	private class PathIteratorImpl 
		implements BackwardPathIterator<T>, ForwardPathIterator<T> {
		
		private FiniteDepthTreeTraversal<T> tr;
		private int idx;

		public PathIteratorImpl(FiniteDepthTreeTraversal<T> tr, int depth) {
			this.tr = tr;
			this.idx = depth;
		}

		public boolean hasPreviousNode() {
			return idx > 0;		
		}

		public T previousNode() {
			return tr.stack.get(--idx).getValue();
		}
		
		public boolean hasNextNode() {
			return idx < (depth - 1);		
		}

		public T nextNode() {
			return tr.stack.get(++idx).getValue();
		}
		
		
		private PathIteratorImpl reset(int depth) {
			this.idx = depth;
			return this;
		}
		
	}

}