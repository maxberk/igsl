package org.igsl.app.knighttour2;

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

import java.util.Comparator;

/**
 * Knight Tour solver presented to demonstrate constraint-satisfaction techniques.
 * The class uses a <code>Position</code> class as node instance in template initialization.
 */
public class KnightTourSolver implements FixedDepthNodeGenerator<Position>, Comparator<Position> {
	
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
	
	public int compare(Position p1, Position p2) {
		int d1Imin = Math.abs(p1.getI());
		int d1Imax = Math.abs(dim - p1.getI());
		
		int d1Jmin = Math.abs(p1.getJ());
		int d1Jmax = Math.abs(dim - p1.getJ());
		
		int d1MinMin = d1Imin + d1Jmin;
		int d1MinMax = d1Imin + d1Jmax;
		int d1MaxMin = d1Imax + d1Jmin;
		int d1MaxMax = d1Imax + d1Jmax;
		
		int d1 = Math.min(Math.min(d1MinMin, d1MinMax), Math.min(d1MaxMin, d1MaxMax));
		
		int d2Imin = Math.abs(p2.getI());
		int d2Imax = Math.abs(dim - p2.getI());
		
		int d2Jmin = Math.abs(p2.getJ());
		int d2Jmax = Math.abs(dim - p2.getJ());
		
		int d2MinMin = d2Imin + d2Jmin;
		int d2MinMax = d2Imin + d2Jmax;
		int d2MaxMin = d2Imax + d2Jmin;
		int d2MaxMax = d2Imax + d2Jmax;
		
		int d2 = Math.min(Math.min(d2MinMin, d2MinMax), Math.min(d2MaxMin, d2MaxMax));		
		
		if(d1 < d2) {
			return -1;
		} else if(d1 > d2) {
			return 1;
		} else {
			return 0;
		}
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
