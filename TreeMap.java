import java.util.Collection;
import java.util.Map;

import java.util.Set;

public class TreeMap<K extends Comparable<K>, V> implements Map<K, V> {
	private class Node {
		public K key;
		public V value;
		public Node right = null;
		public Node left = null;
		public Node parent;

		public Node(K key, V value, Node parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}

	private Node getEntry(Node entry, K key) {
		if (entry == null)
			return null;
		K entryKey = entry.getKey();
		int comparenteResult = key.compareTo(entryKey);
		if (comparenteResult < 0)
			return getEntry(entry.left, key);
		if (comparenteResult > 0)
			return getEntry(entry.right, key);
		return entry;
	}

	private Node root = null;
	private int size = 0;

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {

		return root == null;
	}

	@Override
	public boolean containsKey(Object o) {
		K key = (K) o;
		return getEntry(root, key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	private Node search(K key) {
		Node node = root;
		while (node != null) {
			int l = key.compareTo(node.key);
			if (l == 0)
				return node;
			if (l < 0)
				node = node.left;
			else
				node = node.right;

		}
		return null;
	}

	private Node Zig(Node x, Node parent) {
		x.parent = parent.parent;
		if (parent.left == x) {
			parent.left = x.right;
			if (parent.left != null)
				parent.left.parent = parent;
			x.right = parent;
			if (x.right != null)
				x.right.parent = x;
		} else {

			parent.right = x.left;
			if (parent.right != null)
				parent.right.parent = parent;
			x.left = parent;
			if (x.left != null)
				x.left.parent = x;

		}
		return x;
	}

	private Node ZigZig(Node x, Node parent, Node gpandParent) {
		parent.parent = gpandParent.parent;
		if (parent == gpandParent.left) {
			gpandParent.left = parent.right;
			if (gpandParent.left != null)
				gpandParent.left.parent = gpandParent;
			parent.right = gpandParent;
			if (parent.right != null)
				parent.right.parent = parent;

		} else {
			gpandParent.right = parent.left;
			if (gpandParent.right != null)
				gpandParent.right.parent = gpandParent;
			parent.left = gpandParent;
			if (parent.left != null)
				parent.left.parent = parent;

		}
		return Zig(x, parent);
	}

	private Node ZigZag(Node x, Node parent, Node gpandParent) {
		if (parent == gpandParent.left) {
			gpandParent.left = Zig(x, parent);
			gpandParent.left.parent = gpandParent;

		} else {
			gpandParent.right = Zig(x, parent);
			gpandParent.right.parent = gpandParent;

		}
		return Zig(x, gpandParent);
	}

	private boolean IsBothLeft(Node x, Node parent, Node grandParent) {
		return x == parent.left && parent == grandParent.left;
	}

	private boolean IsBothRight(Node x, Node parent, Node grandParent) {
		return x == parent.right && parent == grandParent.right;
	}

	private Node Splay(Node tree, Node x) {
		Node bufparent = tree.parent;
		tree.parent = null;
		Node parent = x.parent;
		Node gpandParent = parent != null ? parent.parent : null;
		Node insert = gpandParent != null ? gpandParent.parent : null;
		Node splay;
		if (parent == null) {
			tree.parent = bufparent;
			return tree;
		}
		if (parent == tree)
			splay = Zig(x, parent);
		else if (IsBothLeft(x, parent, gpandParent)
				|| IsBothRight(x, parent, gpandParent))
			splay = ZigZig(x, parent, gpandParent);
		else
			splay = ZigZag(x, parent, gpandParent);
		if (insert == null) {
			tree = splay;

		} else {
			if (insert.left == parent || insert.left == gpandParent)
				insert.left = splay;
			else
				insert.right = splay;
			splay.parent = insert;
		}
		tree.parent = bufparent;
		return tree;

	}

	@Override
	public V get(Object key) {
		K k = (K) key;
		Node x = search(k);
		if (x == null)
			return null;
		root = Splay(root, x);
		return x.value;

	}

	public V put(K key, V value) {
		Node current = root;
		Node insertion = null;
		while (current != null) {
			insertion = current;
			int cmp = key.compareTo(current.key);
			if (cmp == 0) {
				current.value = value;
				return value;
			}
			if (cmp < 0)
				current = current.left;
			else
				current = current.right;
		}
		if (insertion == null)
			root = new Node(key, value, null);
		else {
			int cmp = key.compareTo(insertion.key);
			Node x = new Node(key, value, insertion);
			if (cmp < 0)
				insertion.left = x;
			else
				insertion.right = x;
			root = Splay(root, x);
		}

		size++;
		return null;
	}

	private Node findMax(Node tree) {
		Node node = tree;
		Node result = null;
		K max;
		while (node != null) {
			max = node.key;
			result = node;
			node = node.right;
		}
		return result;
	}

	private Node Merge(Node tree1, Node tree2) {
		if (tree2 == null)
			return tree1;
		if (tree1 == null)
			return tree2;
		tree1 = Splay(tree1, findMax(tree1));
		tree1.right = tree2;
		tree1.right.parent = tree1;
		return tree1;
	}

	@Override
	public V remove(Object key) {
		Node x = search((K) key);
		if (x == null)
			return null;
		Node m = Merge(x.left, x.right);
		if (x == root) {
			root = m;
		} else {
			if (x == x.parent.left)
				x.parent.left = m;
			else
				x.parent.right = m;
		}
		if (m != null)
			m.parent = m.parent.parent;
		size--;
		return x.value;

	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
