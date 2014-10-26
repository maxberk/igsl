/**
 * Implicit Graph Search Library(C), 2009, 2014 
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
		long numerator = 5;
		long denominator = 8;
		
		EgyptianFractionsProblemSolver solver = new EgyptianFractionsProblemSolver(numerator, denominator);
		
		System.out.println("=====Egyptian Fractions Problem Solver=====");
		System.out.println("Numerator = " + numerator + ", denominator = " + denominator);
		
		InfiniteDepthTreeTraversal<MutableInteger> tr = new InfiniteDepthTreeTraversal<MutableInteger>(solver);
		Direct.searchForward(tr);
		BackwardPathIterator<MutableInteger> path = tr.getPath();
			
		while(path.hasPreviousNode()) {
			MutableInteger mi = path.previousNode();
			System.out.print(mi.getValue() + "->");
		}
		System.out.println();
	}

}