/**
 * Implicit Graph Search Library(C), 2013 
 */

package org.igsl.traversal.linear.finiteset;

import java.util.EmptyStackException;
import org.igsl.functor.FiniteSetNodeGenerator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;
import org.igsl.functor.PathIterator;
import org.igsl.functor.FiniteSetPathIterator;

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
	 * @see FiniteNodeSetGenerator
	 */
	public DepthFirstTreeTraversal(FiniteSetNodeGenerator<T> generator) 
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
					
					if(generator.isValidTransition(value, getFiniteSetPathIterator())) {
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
						
						if(generator.isValidTransition(value, getFiniteSetPathIterator())) {
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
	public PathIterator<T> getPathIterator() {
		return getFiniteSetPathIterator();
	}
	
	/**
	 * Returns a list of traversal from a root node to cursor including both
	 */
	public PathIterator<T> getPath() {
		return new FiniteSetPathIteratorImpl(this);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public DepthFirstTreeTraversal<T> getCopyOf() {
		DepthFirstTreeTraversal<T> result =	new DepthFirstTreeTraversal<T>();
		return result;
	}
	
	private FiniteSetPathIterator getFiniteSetPathIterator() {
		if(pathIterator == null) {
			pathIterator = new FiniteSetPathIteratorImpl(this);
		} else {
			pathIterator.reset();
		}
		
		return pathIterator;
	}
	
	private DepthFirstTreeTraversal() {}
	
	protected FiniteSetNodeGenerator<T> generator;
	
	protected T[] values;
	protected int[] stack;
	protected int depth;
	
	private FiniteSetPathIteratorImpl pathIterator;
	
	private class FiniteSetPathIteratorImpl implements FiniteSetPathIterator<T> {
		
		private DepthFirstTreeTraversal<T> tr;
		private int idx;

		public FiniteSetPathIteratorImpl(DepthFirstTreeTraversal<T> tr) {
			this.tr = tr;
			this.idx = tr.depth;
		}

		public boolean hasPreviousNode() {
			return idx > 0;		
		}

		public T previousNode() {
			return tr.values[tr.stack[--idx]];
		}
		
		public T rootNode() {
			return tr.isEmpty() ? null : tr.values[tr.stack[0]];
		}
		
		public T leafNode() {
			return tr.isEmpty() ? null : tr.values[tr.stack[depth - 1]];
		}
		
		private FiniteSetPathIterator reset() {
			this.idx = tr.depth;
			return this;
		}
		
	}

}