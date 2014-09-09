package org.igsl.app.egyptianfractions;

/**
 * Implicit Graph Search Library(C), 2009, 2014 
 */

import org.igsl.functor.InfiniteDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

/**
 */
public class EgyptianFractionsProblemSolver implements InfiniteDepthNodeGenerator<MutableInteger>{
	
	private long numerator, denominator;
	
	/**
	 * 
	 * @param numerator
	 * @param denominator
	 */
	public EgyptianFractionsProblemSolver(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public ValuesIterator<MutableInteger> createValues(BackwardPathIterator<MutableInteger> iterator) {
		return new ValuesIteratorImpl(iterator);
	}
	
	public boolean isValidTransition(MutableInteger value, BackwardPathIterator<MutableInteger> iterator) {
		return true;
	}
		
	public boolean isGoal(BackwardPathIterator<MutableInteger> bpi) {
		return getNumerator(bpi) == 1;
	}
	
	private long getStartValue(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;
		
		while(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();
			
			numrest = numrest * mi.getValue() - denrest;
			denrest = denrest * mi.getValue();
		}
		
		return (long) Math.ceil( (double) denrest / (double) numrest);
	}
	
	private long getNumerator(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;
		
		while(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();
			
			numrest = numrest * mi.getValue() - denrest;
			denrest = denrest * mi.getValue();
		}
		
		return numrest;
	}
	
	private class ValuesIteratorImpl implements ValuesIterator<MutableInteger> {
		private MutableInteger i;
		
		public ValuesIteratorImpl(BackwardPathIterator<MutableInteger> bpi) {
			this.i = new MutableInteger(getStartValue(bpi));
		}
		
		public void update(BackwardPathIterator<MutableInteger> bpi) {
			i.assignValue(getStartValue(bpi));
		}

		public boolean hasNext() {
			return true;
		}

		public MutableInteger next() {
			return i.inc();
		}
		
		public MutableInteger getValue() {
			return i;
		}
	
	}
	
}
