/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.coinproblem.variant1;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.app.coinproblem.variant1.CoinProblemSolver;
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
			DepthFirstCostTreeTraversal<int[],AddableInteger> tr = 
				new DepthFirstCostTreeTraversal<int[],AddableInteger>(solver);
			
			Enumeration<int[]> path = Direct.branchAndBound(tr);
			int[] r = null;
			if(path.hasMoreElements()) {
				r = path.nextElement();
				String toPrint = "";
				for(int val : r) {
					toPrint += val + ">";
				}
				System.out.println(toPrint);
			}
		} catch (DefaultValuesUnsupportedException e) {
		}
	}

}