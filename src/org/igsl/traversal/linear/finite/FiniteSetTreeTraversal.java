/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

package org.igsl.traversal.linear.finite;

import org.igsl.functor.generator.FiniteSetNodeGenerator;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;
import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.functor.iterator.path.ForwardPathIterator;

/**
 * Depth-first search implementation for problems with finite number
 * of nodes in the resulted path, e.g. permutation.
 */
public class FiniteSetTreeTraversal<T>
	implements TreeTraversal<T>, Copyable<FiniteSetTreeTraversal<T>>
{
	/**
	 * Constructor based on an expansion operator
	 * 
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see FiniteSetNodeGenerator
	 */
	public FiniteSetTreeTraversal(FiniteSetNodeGenerator<T> generator) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
			this.values = generator.getAllValues();
			this.stack = new int[this.values.length];
			this.stack[0] = 0;
			this.depth = 1;
		}
	}
	
	/**
	 * Expands nodes base on "last found - first expanded" technique.
	 * For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 */
	public boolean moveForward() throws EmptyTraversalException {
		if(depth == values.length) { // terminal node
			return false;
		} else { // depth < values.length
			for(int i = 0; i < values.length; ++i) {
				boolean isFound = false;
				
				for(int j = 0; j < depth; ++j) {
					if(stack[j] == i) {
						isFound = true;
						break;
					}
				}
				
				if(!isFound) {
					T value = values[i];
					
					if(generator.isValidTransition(value, getPathIteratorImpl())) {
						stack[depth++] = i;
						return true;
					}
				}
			}
			
			backtrack();
			
			return true;
		} // depth < values.length
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
				for(int i = stack[depth - 1] + 1; i < values.length; ++i) {
					boolean isFound = false;
					
					for(int j = 0; j < depth-1; ++j) {
						if(stack[j] == i) {
							isFound = true;
							break;
						}
					}
					
					if(!isFound) {
						T value = values[i];
						
						if(generator.isValidTransition(value, getPathIteratorImpl())) {
							stack[depth-1] = i;
							return;
						}
					}
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
		return getPathIteratorImpl();
	}
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public BackwardPathIterator<T> getPath() {
		return new PathIteratorImpl(this);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public FiniteSetTreeTraversal<T> getCopyOf() {
		FiniteSetTreeTraversal<T> result =	new FiniteSetTreeTraversal<T>();
		return result;
	}
	
	private PathIteratorImpl getPathIteratorImpl() {
		return (pathIterator == null) ?
			new PathIteratorImpl(this) : pathIterator.reset();
	}
	
	private FiniteSetTreeTraversal() {}
	
	protected FiniteSetNodeGenerator<T> generator;
	
	protected T[] values;
	protected int[] stack;
	protected int depth;
	
	private PathIteratorImpl pathIterator;
	
	private class PathIteratorImpl 
		implements BackwardPathIterator<T>, ForwardPathIterator<T> {
		
		private FiniteSetTreeTraversal<T> tr;
		private int start, idx;

		public PathIteratorImpl(FiniteSetTreeTraversal<T> tr) {
			this.tr = tr;
			this.start = tr.depth;
			this.idx = this.start;
		}

		public boolean hasPreviousNode() {
			return idx > 0;		
		}

		public T previousNode() {
			return tr.values[tr.stack[--idx]];
		}
		
		public boolean hasNextNode() {
			return idx < (depth - 1);		
		}

		public T nextNode() {
			return tr.values[tr.stack[++idx]];
		}
		
		
		private PathIteratorImpl reset() {
			this.start = tr.depth;
			this.idx = this.start;
			return this;
		}
		
	}

}