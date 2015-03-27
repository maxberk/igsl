package org.igsl.traversal.linear;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.ValuesIterator;
import org.igsl.functor.InfiniteDepthNodeGenerator;
import org.igsl.functor.exception.EmptyTraversalException;
import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.Copyable;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.functor.ForwardPathIterator;

import java.util.Stack;
import java.util.ListIterator;

/**
 * Depth-first search implementation for a problem graph without edge cost.
 */
public class InfiniteDepthTreeTraversal<T>
	implements TreeTraversal<T>, Copyable<InfiniteDepthTreeTraversal<T>>
{
	/**
	 * Constructor based on expansion operator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see InfiniteDepthNodeGenerator
	 */
	public InfiniteDepthTreeTraversal(InfiniteDepthNodeGenerator<T> generator) 
		throws NullPointerException
	{
		if(generator == null) {
			throw new NullPointerException();
		} else {
			this.generator = generator;

			this.stack = new Stack();
			ValuesIterator<T> iterator = generator.createValues(getPathIterator());
			iterator.next();
			this.stack.push(iterator);
			
			this.depth = 1;
		}
	}
	
	/**
	 */
	public boolean moveForward() throws EmptyTraversalException {
		if(depth == 0 || generator.isGoal(getPathIterator())) {
			return false;
		} else {
			ValuesIterator<T> iterator = null;
			
			if(stack.size() < depth + 1) {
				iterator = generator.createValues(getPathIterator());
				stack.push(iterator);
			} else {
				iterator = stack.get(depth);
				iterator.update(getPathIterator());
			}
			
			if(iterator.hasNext()) {
				T value = iterator.next();
				
				if(generator.isValidTransition(value, getPathIterator()) == false) {
					backtrack();
				} else {
					++depth;
				}
			} else {
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
		} else while(depth > 0) {
			ValuesIterator<T> iterator = stack.get(depth-1);
			
			--depth;
			
			if(iterator.hasNext()) {
				T value = iterator.next();
								
				if(generator.isValidTransition(value, getPathIterator())) {
					++depth;
					break;
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
	public InfiniteDepthTreeTraversal<T> getCopyOf() {
		InfiniteDepthTreeTraversal<T> result = new InfiniteDepthTreeTraversal<T>();
		return result;
	}
	
	private PathIteratorImpl getPathIteratorImpl() {
		if(pathIterator == null) {
			pathIterator = new PathIteratorImpl(this);
		} else {
			pathIterator.reset();
		}
		
		return pathIterator;
	}
	
	private InfiniteDepthTreeTraversal() {}
	
	protected InfiniteDepthNodeGenerator<T> generator;
	
	protected Stack<ValuesIterator<T> > stack;
	protected int depth;
	
	private PathIteratorImpl pathIterator;
	
	private class PathIteratorImpl 
		implements BackwardPathIterator<T>, ForwardPathIterator<T> {

		private InfiniteDepthTreeTraversal<T> tr;
		private ListIterator<ValuesIterator<T> > bli, fli;

		public PathIteratorImpl(InfiniteDepthTreeTraversal<T> tr) {
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