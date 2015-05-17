package org.igsl.traversal.linear;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.RandomAccessValuesIterator;
import org.igsl.functor.IndefiniteDepthNodeGenerator;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;
import org.igsl.functor.RandomAccess;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.functor.ForwardPathIterator;

import java.util.Stack;
import java.util.ListIterator;

/**
 * Depth-first search implementation for a problem graph without edge cost.
 */
public class IndefiniteDepthTreeTraversal<T>
	implements TreeTraversal<T>, RandomAccess<T>, Copyable<IndefiniteDepthTreeTraversal<T>>
{
	/**
	 * Constructor based on expansion operator
	 * 
	 * @param generator node generator function
	 * @param backtrackIfValidationFailed if set to true traversal skips iterating for next value and performs backtrack,
	 * otherwise it continues to iterate
	 * @throws NullPointerException thrown if node generator is null
	 * @see IndefiniteDepthNodeGenerator
	 */
	public IndefiniteDepthTreeTraversal(IndefiniteDepthNodeGenerator<T> generator, boolean backtrackIfValidationFailed) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;
			this.backtrackIfValidationFailed = backtrackIfValidationFailed;

			this.stack = new Stack();
			RandomAccessValuesIterator<T> iterator = generator.createValues(this);
			iterator.next();
			this.stack.push(iterator);
			
			this.depth = 1;
		}
	}
	
	/**
	 */
	public boolean moveForward() throws EmptyTraversalException {
		if(depth == 0 || generator.isGoal(this)) {
			return false;
		} else {
			RandomAccessValuesIterator<T> iterator = null;
			
			if(stack.size() < depth + 1) {
				iterator = generator.createValues(this);
				stack.push(iterator);
			} else {
				iterator = stack.get(depth);
				iterator.update(this);
			}
			
			if(iterator.hasNext()) {
				T value = iterator.next();
				
				if(generator.isValidTransition(value, this) == false && backtrackIfValidationFailed == true) {
					try {
						backtrack();
					} catch(EmptyTraversalException ete) {
						return false;
					}
				} else if(generator.isValidTransition(value, this) == false && backtrackIfValidationFailed == false) {
					boolean validTransitionFound = false;
					
					while(iterator.hasNext()) {
						value = iterator.next();
						
						if(generator.isValidTransition(value, this)) {
							validTransitionFound = true;
							++depth;
							break;
						}
					}
					
					if(!validTransitionFound) {
						try {
							backtrack();
						} catch (EmptyTraversalException ete) {
							return false;
						}
					}
				} else {
					++depth;
				}
			} else {
				try {
					backtrack();
				} catch (EmptyTraversalException ete) {
					return false;
				}
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
		} else while(depth > 0) {
			RandomAccessValuesIterator<T> iterator = stack.get(depth-1);
			
			--depth;
			
			if(iterator.hasNext()) {
				T value = iterator.next();
								
				if(generator.isValidTransition(value, this) ) {
					++depth;
					break;
				} else if(backtrackIfValidationFailed == false) {
					boolean validTransitionFound = false;
					
					while(iterator.hasNext()) {
						value = iterator.next();
						
						if(generator.isValidTransition(value, this)) {
							validTransitionFound = true;
							++depth;
							break;
						}
					}
					
					if(validTransitionFound) {
						break;
					}
				}
			}
		}
			
		if(depth == 0) {
			throw new EmptyTraversalException();
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
	public IndefiniteDepthTreeTraversal<T> getCopyOf() {
		IndefiniteDepthTreeTraversal<T> result = new IndefiniteDepthTreeTraversal<T>();
		return result;
	}
	
	/**
	 * 
	 */
	public int length() {
		return depth;
	}
	
	/**
	 * 
	 */
	public T get(int idx) {
		return stack.get(idx).getValue();
	}
	
	private PathIteratorImpl getPathIteratorImpl() {
		if(pathIterator == null) {
			pathIterator = new PathIteratorImpl(this);
		} else {
			pathIterator.reset();
		}
		
		return pathIterator;
	}
	
	private IndefiniteDepthTreeTraversal() {}
	
	protected IndefiniteDepthNodeGenerator<T> generator;
	private boolean backtrackIfValidationFailed;
	
	protected Stack<RandomAccessValuesIterator<T> > stack;
	protected int depth;
	
	private PathIteratorImpl pathIterator;
	
	private class PathIteratorImpl 
		implements BackwardPathIterator<T>, ForwardPathIterator<T> {

		private IndefiniteDepthTreeTraversal<T> tr;
		private ListIterator<RandomAccessValuesIterator<T> > bli, fli;

		public PathIteratorImpl(IndefiniteDepthTreeTraversal<T> tr) {
			this.tr = tr;
			
			this.bli = (tr.depth == 0) ? tr.stack.listIterator() 
				: tr.stack.listIterator(tr.depth);
			this.fli = tr.stack.listIterator();
		}

		public boolean hasPreviousNode() {
			return bli.hasPrevious();
		}

		public T previousNode() {
			return bli.previous().getValue();
		}
		
		public boolean hasNextNode() {
			return fli.nextIndex() <= tr.depth-1;
		}

		public T nextNode() {
			return fli.next().getValue();
		}
		
		private PathIteratorImpl reset() {
			this.bli = tr.stack.listIterator(tr.depth);
			this.fli = tr.stack.listIterator();
			
			return this;
		}
		
	}

}