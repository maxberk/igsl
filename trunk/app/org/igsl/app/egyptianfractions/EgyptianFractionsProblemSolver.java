package org.igsl.app.egyptianfractions;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.generator.IndefiniteDepthNodeGenerator;
import org.igsl.traversal.RandomAccess;
import org.igsl.functor.iterator.values.RandomAccessValuesIterator;
import org.igsl.functor.iterator.path.BackwardPathIterator;

/**
 */
public class EgyptianFractionsProblemSolver implements IndefiniteDepthNodeGenerator<MutableInteger>{
	
	private long numerator, denominator, maxdenominator;
	
	/**
	 * @param numerator
	 * @param denominator
	 * @param maxdenominator
	 */
	public EgyptianFractionsProblemSolver(long numerator, long denominator, long maxdenominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.maxdenominator = maxdenominator;
	}
	
	public RandomAccessValuesIterator<MutableInteger> createValues(RandomAccess<MutableInteger> tr) {
		return new RandomAccessValuesIteratorImpl(tr);
	}
	
	public boolean isValidTransition(MutableInteger value, RandomAccess<MutableInteger> tr) {
		//long maxrest = 1000000 / value.getValue();
		//return getDenominator(bpi) <= maxrest;
		return value.getValue() <= maxdenominator;
	}
		
	public boolean isGoal(RandomAccess<MutableInteger> tr) {
		return getNumerator(tr) == 0;
	}
	
	private long getStartValue(RandomAccess<MutableInteger> tr) {
		long numrest = numerator;
		long denrest = denominator;

		for(int i = tr.length() - 1; i >= 0; --i) {
			numrest = numrest * tr.get(i).getValue() - denrest;
			denrest = denrest * tr.get(i).getValue();
		}
		
		long result = (long) Math.ceil( (double) denrest / (double) numrest); // ai
		long prevresult = tr.length() > 0 ? tr.get(tr.length()-1).getValue() : 0;
		if(result <= prevresult) {
			return prevresult + 1;
		} else {
			return result;
		}
		
		/*
		long maxrest = 1000000 / result;
		if(maxrest > denrest) {
			return result;
		} else {
			double a1 = (double) numrest / (double) denrest;
			double a2 = (double) denrest / (double) 1000000;
			
			double d = a1 * a1 - 4 * a2;
			if(d > 0) {
				double x1 = (a1 - Math.sqrt(d)) / (2 * a2);
				double x2 = (a1 + Math.sqrt(d)) / (2 * a2);
				System.out.println("x1 = " + x1 + "; x2 = " + x2);
				
				if(Math.ceil(x1) < Math.floor(x2)) {
					return (long) Math.ceil(x1);
				}
			} else {
				//System.out.println("d = " + d);
				//System.out.print(".");
			}
			
			return 0;
		}
		*/
	}
	
	private long getNumerator(RandomAccess<MutableInteger> tr) {
		long numrest = numerator;
		long denrest = denominator;
		
		for(int i = tr.length() - 1; i >= 0; --i) {
			numrest = numrest * tr.get(i).getValue() - denrest;
			denrest = denrest * tr.get(i).getValue();
		}
		
		return numrest;
	}
	
	private long getDenominator(BackwardPathIterator<MutableInteger> bpi) {
		long denrest = denominator;
	
		while(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();
			denrest = denrest * mi.getValue();
		}
		
		return denrest;
	}
	
	private class RandomAccessValuesIteratorImpl implements RandomAccessValuesIterator<MutableInteger> {
		private MutableInteger startValue;
		private MutableInteger i;
		
		public RandomAccessValuesIteratorImpl(RandomAccess<MutableInteger> tr) {
			long lStartValue = getStartValue(tr);
			this.startValue = new MutableInteger(lStartValue);
			this.i = new MutableInteger(lStartValue);
		}
		
		public void update(RandomAccess<MutableInteger> tr) {
			long lStartValue = getStartValue(tr);
			startValue.assignValue(lStartValue);
			i.assignValue(lStartValue);
		}

		public boolean hasNext() {
			return (i.getValue() != 0);
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
