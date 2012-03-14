/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.coinproblem.variant2;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.app.coinproblem.variant2.Coin;
import org.igsl.app.coinproblem.variant2.CoinProblemSolver;
import org.igsl.cost.AddableInteger;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.traversal.linear.DepthFirstCostTreeTraversal;

public class CoinProblemTest {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] denominations = new int[]{2, 5, 7, 10};
		int value = 78;
			
		CoinProblemSolver solver = new CoinProblemSolver(denominations, value);
		
		System.out.println("=====Coin Problem Solver. Variant 2. Direct search.=====");
		System.out.print("denominations are : ");
		for(int d : denominations) {
			System.out.print(d + " ");
		}
		System.out.println();
		System.out.println("value is " + value);
		System.out.print("result is : ");
		
		try {
			DepthFirstCostTreeTraversal<Coin,AddableInteger> tr = 
				new DepthFirstCostTreeTraversal<Coin,AddableInteger>(solver);
			
			Enumeration<Coin> path = Direct.branchAndBound(tr);
			
			if(path.hasMoreElements()) {
				String toPrint = "";
				
				int oldIndex = path.nextElement().getIndex();
				int coinsNumber = 1;
				
				while(path.hasMoreElements()) {
					int newIndex = path.nextElement().getIndex();
					
					if(oldIndex != newIndex) {
						if(toPrint.length() == 0) {
							toPrint += solver.getDenomination(oldIndex) + "*" + coinsNumber;
						} else {
							toPrint += "+" + solver.getDenomination(oldIndex) + "*" + coinsNumber;
						}
						
						oldIndex = newIndex;
						coinsNumber = 1;
					} else {
						++coinsNumber;
					}
				}
				
				System.out.println(toPrint);
			} else {
				System.out.println("No solution found");
			}
		} catch (DefaultValuesUnsupportedException e) {
		}
	}

}