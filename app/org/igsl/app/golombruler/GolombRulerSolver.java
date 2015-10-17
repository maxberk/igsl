package org.igsl.app.golombruler;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.functor.generator.IndefiniteDepthNodeGenerator;
import org.igsl.traversal.RandomAccess;
import org.igsl.functor.iterator.values.RandomAccessValuesIterator;
import org.igsl.functor.iterator.path.BackwardPathIterator;

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
		for(int i = tr.length() - 1; i > -1; --i) {
			int d = (i == -1) ? value.getValue() : value.getValue() - tr.get(i).getValue();
			
			for(int j = 0; j < tr.length(); ++j) {
				if(d == tr.get(j).getValue()) {
					return false;
				}
				
				for(int k = j+1; k < tr.length(); ++k) {
					if(tr.get(k).getValue() - tr.get(j).getValue() == d) {
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
		return getLength(tr);
	}
	
	private int getLength(RandomAccess<MutableInteger> tr) {
		return tr.length() > 0 ? tr.get(tr.length()-1).getValue() : 0;
	}
	
	private class RandomAccessValuesIteratorImpl implements RandomAccessValuesIterator<MutableInteger> {
		private MutableInteger i;
		
		public RandomAccessValuesIteratorImpl(RandomAccess<MutableInteger> tr) {
			this.i = new MutableInteger(getStartValue(tr));
		}
		
		public void update(RandomAccess<MutableInteger> tr) {
			i.assignValue(getStartValue(tr));
		}

		public boolean hasNext() {
			return i.getValue() < maxValue;
		}

		public MutableInteger next() {
			return i.inc();
		}
		
		public MutableInteger getValue() {
			return i;
		}
	
	}
	
}