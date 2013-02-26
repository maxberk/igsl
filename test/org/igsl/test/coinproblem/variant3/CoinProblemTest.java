/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013 
 */

package org.igsl.test.coinproblem.variant3;

import org.igsl.algorithm.Direct;
import org.igsl.app.coinproblem.variant3.CoinProblemSolver;
import org.igsl.app.coinproblem.variant3.Denomination;
import org.igsl.cost.AddableInteger;
import org.igsl.functor.PathIterator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.traversal.linear.DepthFirstCostTreeTraversal;

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
		
		try {
			DepthFirstCostTreeTraversal<Denomination, AddableInteger> tr2 = 
					new DepthFirstCostTreeTraversal<Denomination, AddableInteger>(solver);

			long startTime2 = System.currentTimeMillis();
			Direct.searchForward(tr2);
			long finishTime2 = System.currentTimeMillis();
			System.out.println("Work time for greedy search = " + (finishTime2 - startTime2));

			PathIterator<Denomination> path2 = tr2.getPath();
				
			int amount2 = 0;
			while(path2.hasPreviousNode()) {
				Denomination d = path2.previousNode();
			
				if(d.getIndex() >= 0 && d.getAmount() > 0) {
					System.out.print(d.getAmount() + " * " + denominations[d.getIndex()] + " + ");
				}
				amount2 += d.getAmount();
			}
			System.out.println();
			System.out.println("Result for greedy search is " + amount2);

			DepthFirstCostTreeTraversal<Denomination, AddableInteger> tr = 
				new DepthFirstCostTreeTraversal<Denomination, AddableInteger>(solver);

			long startTime = System.currentTimeMillis();
			PathIterator<Denomination> path = Direct.branchAndBound(tr);
			long finishTime = System.currentTimeMillis();
			System.out.println("Work time for optimal search is = " + (finishTime - startTime));
			
			int amount = 0;
			while(path.hasPreviousNode()) {
				Denomination d = path.previousNode();
				
				if(d.getIndex() >= 0 && d.getAmount() > 0) {
					System.out.print(d.getAmount() + " * " + denominations[d.getIndex()] + " + ");
				}
				amount += d.getAmount();
			}
			System.out.println();
			System.out.println("Result for optimal search is " + amount);
			
		} catch (DefaultValuesUnsupportedException e) {
		}
	}

}