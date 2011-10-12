/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.app.coinproblem.variant1;

import java.util.List;
import java.util.LinkedList;

import org.igsl.cost.AddableInteger;
import org.igsl.functor.CostFunction;

/**
 * Coin Problem is a problem of collecting coins of different denominations to meet
 * predefined money value. The problem is to find a minimal number of coins.	
 */
public class CoinProblemSolver implements CostFunction<int[],AddableInteger>{
	
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
	
	public List<int[]> expand(int[] coins) {
		LinkedList<int[]> result = new LinkedList<int[]>();
		
		int lower = (coins.length == 0) ? 0 : coins.length - 1;
		
		for(int i = lower; i < denominations.length; ++i) {
			int[] newarr = new int[i + 1];
			
			for(int j = 0; j < coins.length; ++j) {
				newarr[j] = coins[j];
			}
			
			newarr[i]++;

			if(sum(newarr) <= value ) {
				result.add(newarr);
			}
		}
		
		return result;
	}
	
	public boolean isGoal(int[] coins) {
		return (sum(coins) == value);
	}
	
	public AddableInteger getTransitionCost(int[] from, int[] to) {
		return new AddableInteger(1);
	}
	
	private int sum(int[] coins) {
		int result = 0;
		
		for(int i = 0; i < coins.length; ++i) {
			result += coins[i] * denominations[i];
		}
		
		return result;
	}
	
	public void printArray(int[] a) {
		System.out.print("Values: ");
		
		for(int v : a) {
			System.out.print(v + ">");
		}
		
		System.out.println();
	}

}