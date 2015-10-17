package org.igsl.test.coinproblem.variant3;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.algorithm.Direct;
import org.igsl.app.coinproblem.variant3.CoinProblemSolver;
import org.igsl.app.coinproblem.variant3.Denomination;
import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.traversal.linear.finite.FiniteDepthTreeTraversal;

public class CoinProblemTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] denominations = new int[] {7, 5, 3};
		int value = 93;
			
		CoinProblemSolver solver = new CoinProblemSolver(denominations, value);
		
		System.out.println("=====Coin Problem for FiniteDepthTreeTraversal=====");
		System.out.print("Denominations of coins are : ");
		for(int d : denominations) {
			System.out.print(d + " ");
		}
		System.out.println();
		System.out.println("Money value is " + value);
		
		FiniteDepthTreeTraversal<Denomination> tr = new FiniteDepthTreeTraversal<Denomination>(solver);
		Direct.searchForward(tr);
		BackwardPathIterator<Denomination> path = tr.getPath();
			
		int amount = 0;
		while(path.hasPreviousNode()) {
			Denomination d = path.previousNode();
		
			if(d.getIndex() >= 0) {
				System.out.print(d.getAmount() + " coin(s) with denomination " + denominations[d.getIndex()] + " + ");
			}
			
			amount += d.getAmount();
		}
		System.out.println();
		System.out.println("Total number of coins for greedy search: " + amount);
	}

}