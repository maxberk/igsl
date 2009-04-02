package org.igsl.traversal;

/**
 * Extension for TreeTraversal with copy functionality
 *
 * @param <T> - type of node
 * @see TreeTraversal
 * @see Copyable
 */

public interface CopyableTreeTraversal<T>
	extends TreeTraversal<T>, Copyable<TreeTraversal<T>>
{
}
