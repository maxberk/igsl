package org.igsl.app.coinproblem.variant3;

/**
 * Implicit Graph Search Library(C), 2009, 2012 
 */

import java.util.List;
import java.util.LinkedList;

import org.igsl.cost.AddableInteger;
import org.igsl.functor.CostFunction;
import org.igsl.functor.PathIterator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * Coin Problem is a problem of collecting coins of different denominations to meet
 * predefined money value. The problem is to find a minimal number of coins.	
 */
public class CoinProblemSolver implements CostFunction<Denomination,AddableInteger>{
	
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
	
	public List<Denomination> expand(PathIterator<Denomination> path) {
		LinkedList<Denomination> result = new LinkedList<Denomination>();

		if(path.hasPreviousNode()) {
			Denomination lastDenom = path.previousNode();
			int coinSum = 0;
			
			if(lastDenom.getIndex() >= 0) {
				coinSum += denominations[lastDenom.getIndex()] * lastDenom.getAmount();
			}
			
			while(path.hasPreviousNode()) {
				Denomination denom = path.previousNode();
				
				if(denom.getIndex() >= 0) {
					coinSum += denominations[denom.getIndex()] * denom.getAmount();
				}
			}
			
			if(coinSum == value) {
				return null; // null result			
			}
			
			int nextIdx = lastDenom.getIndex() + 1;
			if(nextIdx < denominations.length) {
				int maxNumOfCoins = (value - coinSum) / denominations[nextIdx];

				for(int i = maxNumOfCoins; i >= 0; --i) {
					result.add(new Denomination(nextIdx, i));
				}
			}
				
			return result;
		} else {
			return null;
		}
		
	}
	
	public AddableInteger getTransitionCost(Denomination from, Denomination to) {
		return new AddableInteger(to.getAmount());
	}
	
	public boolean isGoal(PathIterator<Denomination> path) {
		int coinSum = 0;
		
		while(path.hasPreviousNode()) {
			Denomination d = path.previousNode();
			
			if(d.getIndex() >= 0) {
				coinSum += denominations[d.getIndex()] * d.getAmount();
			}
		}
		
		return (coinSum == value);
	}
	
	/**
	 * Defines default root node value
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default root node value
	 */
	public Denomination getDefaultRootNode() throws DefaultValuesUnsupportedException {
		return new Denomination();
	}
	
	/**
	 * Defines zero root(start) node cost 
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default start node cost
	 */
	public AddableInteger getDefaultRootCost() throws DefaultValuesUnsupportedException {
		return new AddableInteger(0);
	}
	
}
