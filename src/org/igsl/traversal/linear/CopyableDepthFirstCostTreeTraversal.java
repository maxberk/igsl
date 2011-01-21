/**
 * Implicit Graph Search Library(C), 2011 
 */
package org.igsl.traversal.linear;

import java.util.Iterator;

import org.igsl.cost.Addable;
import org.igsl.functor.CostFunction;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.linear.DepthFirstCostTreeTraversal.TreeNode;

public class CopyableDepthFirstCostTreeTraversal<T,C extends Addable<C> & Comparable<C>>
	extends DepthFirstCostTreeTraversal<T,C> implements Copyable<CopyableDepthFirstCostTreeTraversal<T,C>>
{
	/**
	 * Constructor based on a start search node and cost function interface
	 * 
	 * @param value root node value
	 * @param cost root node cost
	 * @param function cost function
	 * @throws NullPointerException thrown if cost function is null
	 * @see CostFunction
	 */
	public CopyableDepthFirstCostTreeTraversal(T value, C cost, CostFunction<T,C> function) 
		throws NullPointerException
	{
		super(value, cost, function);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a DepthFirstCostTreeTraversal with a copy of a cursor node
	 */
	public CopyableDepthFirstCostTreeTraversal<T,C> getCopyOf() {
		CopyableDepthFirstCostTreeTraversal<T,C> result = 
			new CopyableDepthFirstCostTreeTraversal<T,C>();
		
		result.function = function;
		
		Iterator<TreeNode> iterator = nodes.iterator();
		while(iterator.hasNext()) {
			TreeNode node = iterator.next();

			T value = node.getValue();
			C cost = node.getCost();
			TreeNode parent = node.getParent();
			
			if(parent != null) {
				int idx = nodes.indexOf(parent);
				parent = result.nodes.elementAt(idx);
			}
			
			result.nodes.push(new TreeNode(value, cost, parent));
		}
		
		return result;
	}
	
	private CopyableDepthFirstCostTreeTraversal() {}

}
