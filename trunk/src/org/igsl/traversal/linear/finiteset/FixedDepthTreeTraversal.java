/**
 * Implicit Graph Search Library(C), 2013 
 */

package org.igsl.traversal.linear.finiteset;

import java.util.EmptyStackException;
import java.util.ArrayList;
import java.util.Comparator;

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;

import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.functor.exception.EmptyTraversalException;

import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;

import org.igsl.functor.BackwardPathIterator;
import org.igsl.functor.ForwardPathIterator;



/**
 * Depth-first search implementation for a problem graph without edge cost.
 */
public class FixedDepthTreeTraversal<T>
	implements TreeTraversal<T>, Copyable<FixedDepthTreeTraversal<T>>
{
	/**
	 * Constructor based on a start search node and expansion operator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see FiniteNodeSetGenerator
	 */
	public FixedDepthTreeTraversal(FixedDepthNodeGenerator<T> generator) 
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
		}
	}
	
	/**
	 * Constructor based on a start search node, expansion operator and comparator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @param comparator node comparator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see FiniteNodeSetGenerator
	 */
	public FixedDepthTreeTraversal(FixedDepthNodeGenerator<T> generator, Comparator<T> comparator) 
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
		}
	}	
	
	/**
	 * Expands nodes base on "last found - first expanded" technique.
	 * For an empty traversal returns without any action.
	 * If the result of node expansion is empty list of nodes then
	 * searches for other nodes in the tree performing pruning procedure.
	 */
	public boolean moveForward() throws EmptyTraversalException {
		if(depth == stack.size()) { // terminal node
			return false;
		} else { // depth < stack.length
			//System.out.println("depth = " + depth);

			ValuesIterator<T> iterator = stack.get(depth);
			iterator.update(getPathIterator());
			
			while(iterator.hasNext()) {
				
				T value = iterator.next();
				
				boolean isFound = false;
				
				for(int i = 0; i < depth; ++i) {
					if(value.equals(stack.get(i).getValue())) {
						isFound = true;
						break;
					}
				}
				
				if(!isFound && generator.isValidTransition(value, getPathIterator())) {
					++depth;
					return true;
				}
				
			}
			
			backtrack();
			
			return true;
		} // depth < stack.length
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
				ValuesIterator<T> iterator = stack.get(depth);
	
				while(iterator.hasNext()) {
					T value = iterator.next();
					
					boolean isFound = false;
					
					for(int i = 0; i < depth; ++i) {
						if(value.equals(stack.get(i).getValue())) {
							isFound = true;
							break;
						}
					}
					
					if(!isFound && generator.isValidTransition(value, getPathIterator())) {
						++depth;
						return;
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
		return new PathIteratorImpl(this, depth);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public FixedDepthTreeTraversal<T> getCopyOf() {
		FixedDepthTreeTraversal<T> result =	new FixedDepthTreeTraversal<T>();
		return result;
	}
	
	private PathIteratorImpl getPathIteratorImpl() {
		if(pathIterator == null) {
			pathIterator = new PathIteratorImpl(this, depth);
		} else {
			pathIterator.reset(depth);
		}
		
		return pathIterator;
	}
	
	private FixedDepthTreeTraversal() {}
	
	protected FixedDepthNodeGenerator<T> generator;
	
	protected ArrayList<ValuesIterator<T>> stack;
	protected int depth;
	
	private PathIteratorImpl pathIterator;
	
	private class PathIteratorImpl 
		implements BackwardPathIterator<T>, ForwardPathIterator<T> {
		
		private FixedDepthTreeTraversal<T> tr;
		private int idx;

		public PathIteratorImpl(FixedDepthTreeTraversal<T> tr, int depth) {
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