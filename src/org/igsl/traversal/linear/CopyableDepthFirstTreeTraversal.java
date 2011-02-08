/**
 * Implicit Graph Search Library(C), 2011 
 */
package org.igsl.traversal.linear;

import java.util.Iterator;

import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.linear.DepthFirstTreeTraversal.TreeNode;

/**
 * Depth-first search implementation for a problem graph without edge cost.
 * Also provides a <code>Copyable</code> functionality.
 */
public class CopyableDepthFirstTreeTraversal<T> extends DepthFirstTreeTraversal<T>
	implements Copyable<CopyableDepthFirstTreeTraversal<T>>
{
	/**
	 * Constructor based on a start search node and expansion operator
	 * 
	 * @param value root node value
	 * @param generator node generator function
	 * @throws NullPointerException thrown if node generator is null
	 * @see NodeGenerator
	 */
	public CopyableDepthFirstTreeTraversal(T value, NodeGenerator<T> generator) 
		throws NullPointerException
	{
		super(value, generator);
	}
	
	/**
	 * Implementation details of Copyable interface.
	 * Returns a TreeTraversal with a copy of a cursor node
	 */
	public CopyableDepthFirstTreeTraversal<T> getCopyOf() {
		CopyableDepthFirstTreeTraversal<T> result = 
			new CopyableDepthFirstTreeTraversal<T>();
		
		result.generator = generator;
		
		Iterator<TreeNode> iterator = nodes.iterator();
		while(iterator.hasNext()) {
			TreeNode node = iterator.next();

			T value = node.getValue();
			TreeNode parent = node.getParent();
			
			if(parent != null) {
				int idx = nodes.indexOf(parent);
				parent = result.nodes.elementAt(idx);
			}
			
			result.nodes.push(new TreeNode(value, parent));
		}
		
		return result;
	}
	
	private CopyableDepthFirstTreeTraversal() {}

}
