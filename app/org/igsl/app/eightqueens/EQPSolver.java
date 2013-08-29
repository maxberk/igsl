/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

package org.igsl.app.eightqueens;

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

/**
 * Eight Queens problem solver presented to demonstrate constraint-satisfaction techniques. The problem
 * is formulated as follows: eight queens should be placed on a chess board with a condition not to attack
 * each other. The class uses a <code>Queen</code> class as node instance in template initialization.
 */
public class EQPSolver implements FixedDepthNodeGenerator<Queen> {
	
	private int dim;
	
	public EQPSolver(int dim) {
		this.dim = dim;
	}

	public int getMaxDepth() {
		return dim;
	}
	
	public ValuesIterator<Queen> createValues(int idx) {
		return new ValuesIteratorImpl(idx+1);
	}
	
	public boolean isValidTransition(Queen queen, BackwardPathIterator<Queen> iterator) {
		while(iterator.hasPreviousNode()) {
			Queen q = iterator.previousNode();
			
			if(q.canBeat(queen)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isGoal(BackwardPathIterator<Queen> bpi) {
		if(bpi.hasPreviousNode()) {
			Queen q = bpi.previousNode();
			return (q.getI() == dim);
		} else {
			return false;
		}
	}
	
	private class ValuesIteratorImpl implements ValuesIterator<Queen> {
		private Queen q;
		
		public ValuesIteratorImpl(int idx) {
			this.q = new Queen(idx);
		}
		
		public void update(BackwardPathIterator<Queen> iterator) {
			q.firstPosition();
		}

		public boolean hasNext() {
			return q.getJ() < dim;
		}

		public Queen next() {
			return q.nextPosition();
		}
		
		public Queen getValue() {
			return q;
		}
	}
}
