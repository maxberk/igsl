package org.igsl.app.eightqueens2;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.functor.generator.FiniteSetNodeGenerator;
import org.igsl.functor.iterator.path.BackwardPathIterator;

/**
 * Eight Queens problem solver presented to demonstrate constraint-satisfaction techniques. The problem
 * is formulated as follows: eight queens should be placed on a chess board with a condition not to attack
 * each other. The class uses a <code>Queen</code> class as node instance in template initialization.
 */
public class EQPSolver implements FiniteSetNodeGenerator<Integer> {
	
	private int dim;
	
	public EQPSolver(int dim) {
		this.dim = dim;
	}
	
	public Integer[] getAllValues() {
		Integer[] result = new Integer[dim];
		
		for(int i = 0; i < dim; ++i) {
			result[i] = new Integer(i+1);
		}
		
		return result;
	}
	
	public int getMaxDepth() {
		return dim;
	}
	
	public boolean isValidTransition(Integer value, BackwardPathIterator<Integer> iterator) {
		int i = 0;
		
		while(iterator.hasPreviousNode()) {
			Integer node = iterator.previousNode();
			
			if(Math.abs(value.intValue() - node.intValue()) == ++i)
			{
				return false;
			}
		}
		
		return true;
	}
	
}
