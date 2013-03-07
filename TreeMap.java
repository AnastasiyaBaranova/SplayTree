import java.util.Collection;
import java.util.Map;

import java.util.Set;

public class TreeMap<K extends Comparable<K>, V> implements Map<K, V> {
  private class Node {
		public K key;
		public V value;
		public Node right = null;
		public Node left = null;
		public Node par;

		public Node(K key, V val, Node par) {
			this.key = key;
			this.value = val;
			this.par = par;
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
		int compareResult = key.compareTo(entryKey);
		if (compareResult < 0)
			return getEntry(entry.left, key);
		if (compareResult > 0)
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

	private Node Zig(Node x, Node par) {
		x.par = par.par;
		if (par.left == x) {
			par.left = x.right;
			if (par.left != null)
				par.left.par = par;
			x.right = par;
			if (x.right != null)
				x.right.par = x;
		} else {

			par.right = x.left;
			if (par.right != null)
				par.right.par = par;
			x.left = par;
			if (x.left != null)
				x.left.par = x;

		}
		return x;
	}

	private Node ZigZig(Node x, Node par, Node gpar) {
		par.par = gpar.par;
		if (par == gpar.left) {
			gpar.left = par.right;
			if (gpar.left != null)
				gpar.left.par = gpar;
			par.right = gpar;
			if (par.right != null)
				par.right.par = par;

		} else {
			gpar.right = par.left;
			if (gpar.right != null)
				gpar.right.par = gpar;
			par.left = gpar;
			if (par.left != null)
				par.left.par = par;

		}
		return Zig(x, par);
	}

	private Node ZigZag(Node x, Node par, Node gpar) {
		if (par == gpar.left) {
			gpar.left = Zig(x, par);
			gpar.left.par = gpar;

		} else {
			gpar.right = Zig(x, par);
			gpar.right.par = gpar;

		}
		return Zig(x, gpar);
	}

	private Node Splay(Node tree, Node x) {
		Node bufPar = tree.par;
		tree.par = null;
		Node par = x.par;
		Node gpar = par != null ? par.par : null;
		Node insert = gpar != null ? gpar.par : null;
		Node splay;
		if (par == null) {
			tree.par = bufPar;
			return tree;
		}
		if (par == tree)
			splay = Zig(x, par);
		else if ((x == par.left && par == gpar.left)
				|| (x == par.right && par == gpar.right))
			splay = ZigZig(x, par, gpar);
		else
			splay = ZigZag(x, par, gpar);
		if (insert == null) {
			tree = splay;

		} else {
			if (insert.left == par || insert.left == gpar)
				insert.left = splay;
			else
				insert.right = splay;
			splay.par = insert;
		}
		tree.par = bufPar;
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
		Node insert = null;
		while (current != null) {
			insert = current;
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
		if (insert == null)
			root = new Node(key, value, null);
		else {
			int cmp = key.compareTo(insert.key);
			Node x = new Node(key, value, insert);
			if (cmp < 0)
				insert.left = x;
			else
				insert.right = x;
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
		tree1.right.par = tree1;
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
			if (x == x.par.left)
				x.par.left = m;
			else
				x.par.right = m;
		}
		if (m != null)
			m.par = m.par.par;
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
