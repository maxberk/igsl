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
		return getNumerator(bpi) == 0;
	}
	
	private long getStartValue(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;

		long minValue = 0;
		
		if(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();
			minValue = mi.getValue();
			
			numrest = numrest * mi.getValue() - denrest;
			denrest = denrest * mi.getValue();

			while(bpi.hasPreviousNode()) {
				mi = bpi.previousNode();
				
				long maxrest = Long.MAX_VALUE / mi.getValue();
				if(maxrest > numrest && maxrest > denrest) {
					numrest = numrest * mi.getValue() - denrest;
					denrest = denrest * mi.getValue();
				} else {
					throw new RuntimeException("Overflow occured!");
				}
			}
		}
		
		long result = (long) Math.ceil( (double) denrest / (double) numrest);
		return (result > minValue) ? result : minValue + 1;
	}
	
	private long getNumerator(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;
		
		while(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();

			long maxrest = Long.MAX_VALUE / mi.getValue();
			if(maxrest > numrest && maxrest > denrest) {
				numrest = numrest * mi.getValue() - denrest;
				denrest = denrest * mi.getValue();
			} else {
				throw new RuntimeException("Overflow occured!");
			}
		}
		
		return numrest;
	}
	
	private class ValuesIteratorImpl implements ValuesIterator<MutableInteger> {
		private MutableInteger startValue;
		private MutableInteger i;
		
		public ValuesIteratorImpl(BackwardPathIterator<MutableInteger> bpi) {
			long lStartValue = getStartValue(bpi);
			this.startValue = new MutableInteger(lStartValue);
			this.i = new MutableInteger(lStartValue);
		}
		
		public void update(BackwardPathIterator<MutableInteger> bpi) {
			long lStartValue = getStartValue(bpi);
			startValue.assignValue(lStartValue);
			i.assignValue(lStartValue);
		}

		public boolean hasNext() {
			return true;
		}

		public MutableInteger next() {
			if(i.getValue() == startValue.getValue()) {
				startValue.assignValue(0);
				return i;
			} else {
				return i.inc();
				
			}
		}
		
		public MutableInteger getValue() {
			return i;
		}
	
	}
	
}
