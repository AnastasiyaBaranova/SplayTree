import junit.framework.TestCase;

public class Tests extends TestCase {

	public void testRemove1() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		Tree.put(1, 9);
		Tree.put(2, 1);
		Tree.put(3, 5);
		Tree.remove(2);
		Tree.remove(3);
		Tree.remove(1);
		assertFalse(!Tree.isEmpty());
	}

	public void testRemoveFromEmpty() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		assertEquals(null, Tree.remove(2));
		assertEquals(0, Tree.size());
	}

	public void testRemove2() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		Tree.put(1, 9);
		Tree.put(2, 1);
		Tree.put(3, 5);
		Tree.remove(2);
		assertTrue(!Tree.containsKey(2));
		assertEquals(2, Tree.size());
	}

	public void testIsEmpty() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		assertTrue(Tree.isEmpty());
		Tree.put(1, 2);
		Tree.put(2, 3);
		Tree.remove(1);
		Tree.remove(2);
		assertTrue(Tree.isEmpty());
	}

	public void testPut() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		Tree.put(1, 2);
		Tree.put(2, 2);
		Tree.put(3, 7);
		Tree.put(4, 1);
		assertEquals(Tree.size(), 4);
	}

	public void testSplay1() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();

		Tree.put(1, 7);
		Tree.put(2, 9);
		Tree.put(3, 6);
		Tree.put(4, 1);
		Tree.put(5, 3);
		assertEquals(5, Tree.size());
		Tree.get(3);
		assertEquals(5, Tree.size());

	}

	public void testSplay2() {
		TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();

		Tree.put(1, 7);
		Tree.put(2, 9);
		Tree.put(3, 6);
		Tree.put(4, 1);
		Tree.put(5, 3);
		assertEquals(5, Tree.size());
		Tree.remove(3);
		assertEquals(4, Tree.size());

	}
}
