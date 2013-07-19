package org.igsl.app.coinproblem.variant3;

/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

import org.igsl.functor.FixedDepthNodeGenerator;
import org.igsl.functor.ValuesIterator;
import org.igsl.functor.BackwardPathIterator;

/**
 * Coin Problem is a problem of collecting coins of different denominations to meet
 * predefined money value.	
 */
public class CoinProblemSolver implements FixedDepthNodeGenerator<Denomination>{
	
	private int[] denominations;
	private int value;
	
	/**
	 * 
	 * @param denominations array of coins denominations (non-sorted)
	 * @param value summary value to be collected
	 */
	public CoinProblemSolver(int[] denominations, int value) {
		this.denominations = denominations;
		this.value = value;
	}
	
	public int getDenomination(int index) {
		return denominations[index];
	}
	
	public int getMaxDepth() {
		return denominations.length;
	}
	
	public ValuesIterator<Denomination> createValues(int idx) {
		return new ValuesIteratorImpl(idx, value);
	}
	
	public boolean isValidTransition(Denomination value, BackwardPathIterator<Denomination> bpi) {
		return true;
	}
	
	private class ValuesIteratorImpl implements ValuesIterator<Denomination> {
		
		private int value;
		private Denomination d;
		
		public ValuesIteratorImpl(int idx, int value) {
			this.value = value;
			this.d = new Denomination(idx);
		}
		
		public void update(BackwardPathIterator<Denomination> iterator) {
			int accValue = 0;
			
			while(iterator.hasPreviousNode()) {
				Denomination denom = iterator.previousNode();
				accValue += denominations[denom.getIndex()] * denom.getAmount();
			}
			
			d.setAmount((value - accValue) % denominations[d.getIndex()]);
		}

		public boolean hasNext() {
			return d.getAmount() > 0;
		}

		public Denomination next() {
			d.setAmount(d.getAmount() - 1);
			return d;
		}
		
		public Denomination getValue() {
			return d;
		}
	
	}
	
}
