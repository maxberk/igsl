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
		if(numrest == 1) return result;
		
		long numrestprev = numrest; //ni-1
		long denrestprev = denrest; //di-1		
		
		long nextresult;
		long maxrest = Long.MAX_VALUE / result;
		if(maxrest > numrest) {
			numrest = numrest * result - denrest; //ni
			if(numrest == 0) return result;
			
			if(maxrest > denrest) {
				denrest = denrest * result; //di
				nextresult = (long) Math.ceil( (double) denrest / (double) numrest); // ai+1
			} else {
				BigInteger bNumrestNew = BigInteger.valueOf(numrest);

				BigInteger bDenrest = BigInteger.valueOf(denrest);
				BigInteger bResult = BigInteger.valueOf(result);
				
				BigInteger bDenrestNew = bDenrest.multiply(bResult);
				denrest = bDenrestNew.longValue();

				BigInteger bNextresult = bDenrestNew.divide(bNumrestNew);
				nextresult = bNextresult.longValue();
			}
		} else {
			BigInteger bNumrest = BigInteger.valueOf(numrest);
			BigInteger bResult = BigInteger.valueOf(result);
			BigInteger bDenrest = BigInteger.valueOf(denrest);
			
			BigInteger bNumrestNew = bNumrest.multiply(bResult).subtract(bDenrest);
			
			if(maxrest > denrest) {
				denrest = denrest * result; //di
				nextresult = (long) Math.ceil( (double) denrest / (double) bNumrestNew.longValue() ); // ai+1
			} else {
				BigInteger bDenrestNew = bDenrest.multiply(bResult);
				denrest = bDenrestNew.longValue();
				
				BigInteger bNextresult = bDenrestNew.divide(bNumrestNew);
				nextresult = bNextresult.longValue();
			}
		}
		
		long maxnextresult = (long) Math.ceil( (double) Long.MAX_VALUE / (double) denrest ); // A/di = a~i+1
		if(nextresult > maxnextresult) {
			long newresult = (long) Math.ceil(
				(double) denrestprev / (double) (numrestprev - maxnextresult * denrestprev));
			
			//System.out.println(" -->nextresult = " + nextresult + " maxnextresult = " + maxnextresult 
					//+ " result = " + result + " newresult = " + newresult);
			
			result = newresult;
		} else {
			//System.out.println("nextresult = " + nextresult + " maxnextresult = " + maxnextresult 
					//+ " result = " + result);
		}
		
		//return (result > minValue) ? result : minValue + 1;
		return result;
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
