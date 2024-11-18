import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import static org.junit.jupiter.api.Assertions.*;

public class ARNTest {
    private ARN<Integer> arn;

    @BeforeEach
    public void setUp() {
        arn = new ARN<>();
    }

    @Test
    public void testAdd() {
        assertTrue(arn.add(10));
        assertTrue(arn.add(20));
        assertTrue(arn.add(5));
        assertFalse(arn.add(10)); // Duplicate element
        assertEquals(3, arn.size());
    }

    @Test
    public void testRemove() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.remove(10));
        assertFalse(arn.remove(10)); // Element already removed
        assertEquals(2, arn.size());
    }

    @Test
    public void testContains() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.contains(10));
        assertFalse(arn.contains(15));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(arn.isEmpty());
        arn.add(10);
        assertFalse(arn.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, arn.size());
        arn.add(10);
        arn.add(20);
        assertEquals(2, arn.size());
    }

    @Test
    public void testClear() {
        arn.add(10);
        arn.add(20);
        arn.clear();
        assertTrue(arn.isEmpty());
        assertEquals(0, arn.size());
    }

    @Test
    public void testIterator() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Iterator<Integer> it = arn.iterator();
        assertTrue(it.hasNext());
        assertEquals(5, it.next());
        assertEquals(10, it.next());
        assertEquals(20, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testToArray() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Object[] array = arn.toArray();
        assertArrayEquals(new Object[]{5, 10, 20}, array);
    }

    @Test
    public void testToArrayWithType() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Integer[] array = arn.toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{5, 10, 20}, array);
    }

    @Test
    public void testAddAll() {
        assertTrue(arn.addAll(Arrays.asList(10, 20, 5)));
        assertEquals(3, arn.size());
    }

    @Test
    public void testRemoveAll() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.removeAll(Arrays.asList(10, 5)));
        assertEquals(1, arn.size());
    }

    @Test
    public void testContainsAll() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.containsAll(Arrays.asList(10, 5)));
        assertFalse(arn.containsAll(Arrays.asList(10, 15)));
    }

    @Test
    public void testRetainAll() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.retainAll(Arrays.asList(10, 5)));
        assertEquals(2, arn.size());
    }

    @Test
    public void testIteratorRemove() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Iterator<Integer> it = arn.iterator();
        while (it.hasNext()) {
            if (it.next() == 10) {
                it.remove();
            }
        }
        assertFalse(arn.contains(10));
        assertEquals(2, arn.size());
    }

    @Test
    public void testIteratorConcurrentModification() {
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Iterator<Integer> it = arn.iterator();
        arn.add(15);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void testIteratorNoSuchElementException() {
        arn.add(10);
        Iterator<Integer> it = arn.iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }
}