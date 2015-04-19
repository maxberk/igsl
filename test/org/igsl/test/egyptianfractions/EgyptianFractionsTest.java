/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

package org.igsl.test.egyptianfractions;

import org.igsl.algorithm.Direct;
import org.igsl.app.egyptianfractions.EgyptianFractionsProblemSolver;
import org.igsl.app.egyptianfractions.MutableInteger;

import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.IndefiniteDepthTreeTraversal;
import org.igsl.functor.exception.EmptyTraversalException;

public class EgyptianFractionsTest {
	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long numerator = 4;
		long denominator = 5;
		long maxdenominator = 65;
		
		EgyptianFractionsProblemSolver solver = new EgyptianFractionsProblemSolver(numerator, denominator, maxdenominator);
		
		System.out.println("=====Egyptian Fractions Problem Solver=====");
		System.out.println(numerator + "/" + denominator + "=");
		
		IndefiniteDepthTreeTraversal<MutableInteger> tr = new IndefiniteDepthTreeTraversal<MutableInteger>(solver);
		int i = 0;
		while(i++ < 50 && !tr.isEmpty()) {
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
			
			try {
				tr.backtrack();
			} catch(EmptyTraversalException ete) {
				break;
			}
		}
	}

}