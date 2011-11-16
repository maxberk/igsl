/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.app.coinproblem.variant2;

import java.util.List;
import java.util.LinkedList;

import org.igsl.cost.AddableInteger;
import org.igsl.functor.CostFunction;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * Coin Problem is a problem of collecting coins of different denominations to meet
 * predefined money value. The problem is to find a minimal number of coins.	
 */
public class CoinProblemSolver implements CostFunction<Coin,AddableInteger>{
	
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
	
	public List<Coin> expand(Coin coin) {
		LinkedList<Coin> result = new LinkedList<Coin>();
		int coinSum = sum(coin);
		
		for(int i = coin.getIndex(); i < denominations.length; ++i) {
			if(coinSum + denominations[i] <= value) {
				result.add(new Coin(i, coin));
			}
		}
		
		return result;
	}
	
	public boolean isGoal(Coin coin) {
		return (sum(coin) == value);
	}
	
	public AddableInteger getTransitionCost(Coin from, Coin to) {
		return new AddableInteger(1);
	}
	
	/**
	 * Defines default root node value
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default root node value
	 */
	public Coin getDefaultRootNode() throws DefaultValuesUnsupportedException {
		return new Coin(0);
	}
	
	/**
	 * Defines zero root(start) node cost 
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default start node cost
	 */
	public AddableInteger getDefaultRootCost() throws DefaultValuesUnsupportedException {
		return new AddableInteger(0);
	}
	
	private int sum(Coin coin) {
		int result = 0;
		
		do {
			result += denominations[coin.getIndex()];
			coin = coin.getParent();
		} while(coin != null);
		
		return result;
	}

}