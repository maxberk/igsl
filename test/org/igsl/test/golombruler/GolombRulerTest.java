/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

package org.igsl.test.golombruler;

import org.igsl.algorithm.Direct;
import org.igsl.app.golombruler.GolombRulerSolver;
import org.igsl.app.golombruler.MutableInteger;

import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.IndefiniteDepthTreeTraversal;
import org.igsl.functor.exception.EmptyTraversalException;

public class GolombRulerTest {
	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		int maxValue = 13;
		
		GolombRulerSolver solver = new GolombRulerSolver(maxValue);
		
		System.out.println("=====Golomb Ruler Solver=====");
		System.out.println("maxValue = " + maxValue);
		
		IndefiniteDepthTreeTraversal<MutableInteger> tr = new IndefiniteDepthTreeTraversal<MutableInteger>(solver, false);
		int i = 0;
		while(i++ < 1 && !tr.isEmpty()) {
			Direct.searchForward(tr);
			BackwardPathIterator<MutableInteger> path = tr.getPath();
			
			System.out.println();
			System.out.print(i + ": ");
			while(path.hasPreviousNode()) {
				MutableInteger mi = path.previousNode();
				if(path.hasPreviousNode()) {
					System.out.print(mi.getValue() + ";");
				} else {
					System.out.print(mi.getValue());
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