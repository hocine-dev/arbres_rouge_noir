import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import static org.junit.jupiter.api.Assertions.*;

public class ARNTest {
    private ARN<Integer> arbre;

    @BeforeEach
    public void setUp() {
        arbre = new ARN<>();
    }

    @Test
    public void testAdd() {
        assertTrue(arbre.add(10));
        assertTrue(arbre.add(20));
        assertTrue(arbre.add(5));
        assertFalse(arbre.add(10)); // Duplicate element
        assertEquals(3, arbre.size());
    }

    @Test
    public void testRemove() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        assertTrue(arbre.remove(10));
        assertFalse(arbre.remove(10)); // Element already removed
        assertEquals(1, arbre.size());
    }

    @Test
    public void testContains() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        assertTrue(arbre.contains(10));
        assertFalse(arbre.contains(15));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(arbre.isEmpty());
        arbre.add(10);
        assertFalse(arbre.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, arbre.size());
        arbre.add(10);
        arbre.add(20);
        assertEquals(2, arbre.size());
    }

    @Test
    public void testClear() {
        arbre.add(10);
        arbre.add(20);
        arbre.clear();
        assertTrue(arbre.isEmpty());
        assertEquals(0, arbre.size());
    }

    @Test
    public void testIterator() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        Integer[] expected = {5, 10, 20};
        Integer[] actual = new Integer[3];
        int i = 0;
        for (Integer value : arbre) {
            actual[i++] = value;
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToArray() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        Object[] expected = {5, 10, 20};
        assertArrayEquals(expected, arbre.toArray());
    }

    @Test
    public void testToArrayWithParameter() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        Integer[] expected = {5, 10, 20};
        Integer[] actual = arbre.toArray(new Integer[0]);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        assertTrue(arbre.addAll(Arrays.asList(10, 20, 5)));
        assertEquals(3, arbre.size());
    }

    @Test
    public void testRemoveAll() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        assertTrue(arbre.removeAll(Arrays.asList(10, 5)));
        assertEquals(1, arbre.size());
    }

    @Test
    public void testContainsAll() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        assertTrue(arbre.containsAll(Arrays.asList(10, 5)));
        assertFalse(arbre.containsAll(Arrays.asList(10, 15)));
    }

    @Test
    public void testRetainAll() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        assertTrue(arbre.retainAll(Arrays.asList(10, 5)));
        assertEquals(2, arbre.size());
    }

    @Test
    public void testIteratorRemove() {
        arbre.add(10);
        arbre.add(20);
        arbre.add(5);
        var iterator = arbre.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == 10) {
                iterator.remove();
            }
        }
        assertFalse(arbre.contains(10));
        assertEquals(2, arbre.size());
    }

    @Test
    public void testIteratorNoSuchElementException() {
        var iterator = arbre.iterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIteratorConcurrentModificationException() {
        arbre.add(10);
        var iterator = arbre.iterator();
        arbre.add(20);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }
}