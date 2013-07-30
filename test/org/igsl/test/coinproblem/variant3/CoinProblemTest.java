/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

package org.igsl.test.coinproblem.variant3;

import org.igsl.algorithm.Direct;
import org.igsl.app.coinproblem.variant3.CoinProblemSolver;
import org.igsl.app.coinproblem.variant3.Denomination;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.finiteset.FixedDepthTreeTraversal;

public class CoinProblemTest {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] denominations = new int[]{7, 5, 1};
		int value = 94;
			
		CoinProblemSolver solver = new CoinProblemSolver(denominations, value);
		
		System.out.println("=====Coin Problem Solver. Variant 3. Greedy and branch-and bound algorithms.=====");
		System.out.print("Denominations are : ");
		for(int d : denominations) {
			System.out.print(d + " ");
		}
		System.out.println();
		System.out.println("Value is " + value);
		
		FixedDepthTreeTraversal<Denomination> tr2 = new FixedDepthTreeTraversal<Denomination>(solver);
		Direct.searchForward(tr2);
		BackwardPathIterator<Denomination> path2 = tr2.getPath();
			
		int amount2 = 0;
		while(path2.hasPreviousNode()) {
			Denomination d = path2.previousNode();
		
			if(d.getIndex() >= 0) {
				System.out.print(d.getAmount() + " * " + denominations[d.getIndex()] + " + ");
			}
			
			amount2 += d.getAmount();
		}
		System.out.println();
		System.out.println("Total number of coins for greedy search: " + amount2);
	}

}