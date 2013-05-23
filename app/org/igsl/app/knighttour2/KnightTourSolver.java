package org.igsl.app.knighttour2;

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

/**
 * Knight Tour solver presented to demonstrate constraint-satisfaction techniques.
 * The class uses a <code>Position</code> class as node instance in template initialization.
 */
public class KnightTourSolver implements FixedDepthNodeGenerator<Position> {
	
	/**
	 * Constructor for Knight Tour solver
	 */
	public KnightTourSolver(int dim, Position initial) {
		this.dim = dim;
		this.initial = initial;
	}
	
	public int getMaxDepth() {
		return dim*dim;
	}
	
	public ValuesIterator<Position> createValues(int idx) {
		return (idx == 0) ? new ValuesIteratorImpl(initial) : new ValuesIteratorImpl();
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
			this.j = 0;
			from = null;
			pos = new Position();
		}
		
		public ValuesIteratorImpl(Position pos) {
			this.i = 0;
			this.j = 0;
			from = null;
			this.pos = pos;
		}
		
		public void update(BackwardPathIterator<Position> iterator) {
			if(iterator.hasPreviousNode()) {
				from = iterator.previousNode();
				//System.out.println("Updating ... from = " + from);
			}
			
			this.i = 0;
			this.j = 0;
		}
		
		public boolean hasNext() {
			return (i != 3 || j != 2);
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
		
	}
	
	private int dim; // problem dim
	private Position initial; // intial knight position

}
