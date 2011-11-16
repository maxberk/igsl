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
		CoinProblemSolver solver = new CoinProblemSolver(new int[]{2, 5, 7, 10}, 80);
		
		System.out.println("=====Coin Problem Solver. Direct search.=====");
		
		try {
			DepthFirstCostTreeTraversal<Coin,AddableInteger> tr = 
				new DepthFirstCostTreeTraversal<Coin,AddableInteger>(solver);
			
			Enumeration<Coin> path = Direct.branchAndBound(tr);
	
			String toPrint = "";
			if(path.hasMoreElements()) {
				toPrint += ">" + path.nextElement();
			}
			System.out.println(toPrint);
		} catch (DefaultValuesUnsupportedException e) {
		}
	}

}