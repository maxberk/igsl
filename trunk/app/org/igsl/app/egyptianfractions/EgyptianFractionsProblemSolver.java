package org.igsl.app.egyptianfractions;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.InfiniteDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

import java.math.BigInteger;

/**
 */
public class EgyptianFractionsProblemSolver implements InfiniteDepthNodeGenerator<MutableInteger>{
	
	private static int idx = 0;
	
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
	
	public boolean isValidTransition(MutableInteger value, BackwardPathIterator<MutableInteger> bpi) {
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
				if(maxrest > numrest) {
					numrest = numrest * mi.getValue() - denrest;
					
					if(maxrest > denrest) {
						denrest = denrest * mi.getValue();
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		long maxrest = Long.MAX_VALUE / value.getValue();
		if(maxrest > numrest) {
			numrest = numrest * value.getValue() - denrest;
			
			if(maxrest > denrest) {
				denrest = denrest * value.getValue();
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
		
	public boolean isGoal(BackwardPathIterator<MutableInteger> bpi) {
		return getNumerator(bpi) == 0;
	}
	
	private long getStartValue(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;

		if(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();
			
			numrest = numrest * mi.getValue() - denrest;
			denrest = denrest * mi.getValue();

			while(bpi.hasPreviousNode()) {
				mi = bpi.previousNode();
				
				long maxrest = Long.MAX_VALUE / mi.getValue();
				if(maxrest > denrest) {
					numrest = numrest * mi.getValue() - denrest;
					denrest = denrest * mi.getValue();
				} else {
					throw new RuntimeException("Overflow occured: maxrest < denrest: "
							+ maxrest + " < " + denrest);
				}
			}
		}

		long result = (long) Math.ceil( (double) denrest / (double) numrest); // ai
		
		long maxrest = Long.MAX_VALUE / result;
		if(maxrest > denrest) {
			return result;
		} else {
			double a1 = (double) numrest / (double) denrest;
			double a2 = (double) denrest / (double) Long.MAX_VALUE;
			
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
			}
			
			return 0;
		}
	}
	
	private long getNumerator(BackwardPathIterator<MutableInteger> bpi) {
		long numrest = numerator;
		long denrest = denominator;
		
		while(bpi.hasPreviousNode()) {
			MutableInteger mi = bpi.previousNode();

			long maxrest = Long.MAX_VALUE / mi.getValue();
			
			if(maxrest > numrest) {
				numrest = numrest * mi.getValue() - denrest;
				
				if(maxrest > denrest) {
					denrest = denrest * mi.getValue();
				} else {
					throw new RuntimeException("Overflow occured: maxrest < denrest: "
						+ maxrest + " < " + denrest);
	
				}
			} else {
				throw new RuntimeException("Overflow occured: maxrest < numrest: "
					+ maxrest + " < " + numrest + " " + mi.getValue());
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
