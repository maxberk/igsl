/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

package org.igsl.test.egyptianfractions;

import org.igsl.algorithm.Direct;
import org.igsl.app.egyptianfractions.EgyptianFractionsProblemSolver;
import org.igsl.app.egyptianfractions.MutableInteger;

import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.InfiniteDepthTreeTraversal;

public class EgyptianFractionsTest {
	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long numerator = 3;
		long denominator = 7;
		long maxdenominator = 47;
		
		EgyptianFractionsProblemSolver solver = new EgyptianFractionsProblemSolver(numerator, denominator, maxdenominator);
		
		System.out.println("=====Egyptian Fractions Problem Solver=====");
		System.out.println(numerator + "/" + denominator + "=");
		
		InfiniteDepthTreeTraversal<MutableInteger> tr = new InfiniteDepthTreeTraversal<MutableInteger>(solver);
		for(int i = 1; i < 42; ++i) {
			Direct.searchForward(tr);
			BackwardPathIterator<MutableInteger> path = tr.getPath();
			
			System.out.println();
			System.out.print(i + ": ");
			while(path.hasPreviousNode()) {
				MutableInteger mi = path.previousNode();
				if(path.hasPreviousNode()) {
					System.out.print("1/" + mi.getValue() + "+");
				} else {
					System.out.print("1/" + mi.getValue());
				}
			}
			System.out.println();

			tr.backtrack();
		}
	}

}