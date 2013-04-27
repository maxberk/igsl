package org.igsl.app.knighttour2;

import java.util.LinkedList;
import java.util.List;

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * Knight Tour solver presented to demonstrate constraint-satisfaction techniques.
 * The class uses a <code>Position</code> class as node instance in template initialization.
 */
public class KnightTourSolver implements FixedDepthNodeGenerator<Position> {
	
	/**
	 * Constructor for Knight Tour solver
	 */
	public KnightTourSolver(int dim) {
		this.dim = dim;
	}
	
	public int getMaxDepth() {
		return -1;
	}
	
	public ValuesIterator<Position> createValues(int idx) {
		return new ValuesIteratorImpl();
	}
	
	public boolean isValidTransition(Position pos, BackwardPathIterator<Position> bpi) {
		int i = pos.getI();
		int j = pos.getJ();
		
		return(i >= 0 && i < dim && j >= 0 && j < dim);
	}
	
	private class ValuesIteratorImpl implements ValuesIterator<Position> {
		int[] deltas = {-2, -1, 1, 2};

		private Position from, pos;
		int i, j;
		
		public ValuesIteratorImpl() {
			this.i = 0;
			this.j = -1;
			from = null;
			pos = new Position();
		}
		
		public boolean hasNext() {
			return (i != 3 && j != 2);
		}

		public Position next() {
			do {
				if(j < 3) {
					++j;
				} else {
					++i;
					j = 0;
				}
			} while((Math.abs(deltas[i]) + Math.abs(deltas[j])) != 3);

			int newI = from.getI() + deltas[i];
			int newJ = from.getJ() + deltas[j];

			pos.assign(newI, newJ);
			
			return pos;
		}
		
		public Position getValue() {
			return pos;
		}
		
		public void update(BackwardPathIterator<Position> iterator) {
			if(iterator.hasPreviousNode()) {
				from = iterator.previousNode();
			}
			
			this.i = 0;
			this.j = -1;
		}
	}
	
	private int dim; // problem dim

}
