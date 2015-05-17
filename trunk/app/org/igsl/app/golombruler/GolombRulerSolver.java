package org.igsl.app.golombruler;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.IndefiniteDepthNodeGenerator;
import org.igsl.functor.RandomAccess;
import org.igsl.functor.RandomAccessValuesIterator;
import org.igsl.functor.BackwardPathIterator;

/**
 */
public class GolombRulerSolver implements IndefiniteDepthNodeGenerator<MutableInteger>{
	
	private int maxValue;
	
	/**
	 * @param maxValue
	 */
	public GolombRulerSolver(int maxValue) {
		this.maxValue = maxValue;
	}
	
	public RandomAccessValuesIterator<MutableInteger> createValues(RandomAccess<MutableInteger> tr) {
		return new RandomAccessValuesIteratorImpl(tr);
	}
	
	public boolean isValidTransition(MutableInteger value, RandomAccess<MutableInteger> tr) {
		for(int i = -1; i < tr.length(); ++i) {
			int d = (i == -1) ? value.getValue() : value.getValue() - tr.get(i).getValue();
		
			for(int j = -1; j < tr.length(); ++j) {
				for(int k = j+1; k < tr.length(); ++k) {
					if(j == -1) {
						if(tr.get(k).getValue() == d) {
							return false;
						}
					} else if(tr.get(k).getValue() - tr.get(j).getValue() == d) {
						return false;
					}
				}
			}
		}

		return true;
	}
		
	public boolean isGoal(RandomAccess<MutableInteger> tr) {
		return getLength(tr) == maxValue;
	}
	
	private int getStartValue(RandomAccess<MutableInteger> tr) {
		return getLength(tr) + 1;
	}
	
	private int getLength(RandomAccess<MutableInteger> tr) {
		return tr.length() > 0 ? tr.get(tr.length()-1).getValue() : 0;
	}
	
	private class RandomAccessValuesIteratorImpl implements RandomAccessValuesIterator<MutableInteger> {
		private MutableInteger startValue;
		private MutableInteger i;
		
		public RandomAccessValuesIteratorImpl(RandomAccess<MutableInteger> tr) {
			int iStartValue = getStartValue(tr);
			this.startValue = new MutableInteger(iStartValue);
			this.i = new MutableInteger(iStartValue);
		}
		
		public void update(RandomAccess<MutableInteger> tr) {
			int iStartValue = getStartValue(tr);
			startValue.assignValue(iStartValue);
			i.assignValue(iStartValue);
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