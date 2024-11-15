import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ABRTest {
    private ABR<Integer> abr;

    @BeforeEach
    public void setUp() {
        abr = new ABR<>();
    }

    @Test
    public void testAdd() {
        assertTrue(abr.add(5));
        assertTrue(abr.add(3));
        assertTrue(abr.add(7));
        assertFalse(abr.add(5)); // Duplicate
        assertEquals(3, abr.size());
    }

    @Test
    public void testContains() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        assertTrue(abr.contains(5));
        assertTrue(abr.contains(3));
        assertTrue(abr.contains(7));
        assertFalse(abr.contains(10));
    }

    @Test
    public void testRemove() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        assertTrue(abr.remove(5));
        assertFalse(abr.contains(5));
        assertEquals(2, abr.size());
        assertFalse(abr.remove(10)); // Non-existent element
    }

    @Test
    public void testIsEmpty() {
        assertTrue(abr.isEmpty());
        abr.add(5);
        assertFalse(abr.isEmpty());
    }

    @Test
    public void testClear() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        abr.clear();
        assertTrue(abr.isEmpty());
        assertEquals(0, abr.size());
    }

    @Test
    public void testIterator() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        Iterator<Integer> it = abr.iterator();
        assertTrue(it.hasNext());
        assertEquals(3, it.next());
        assertEquals(5, it.next());
        assertEquals(7, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testToArray() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        Integer[] array = abr.toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{3, 5, 7}, array);
    }

    @Test
    public void testContainsAll() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        List<Integer> list = Arrays.asList(3, 5);
        assertTrue(abr.containsAll(list));
        list = Arrays.asList(3, 5, 10);
        assertFalse(abr.containsAll(list));
    }

    @Test
    public void testAddAll() {
        List<Integer> list = Arrays.asList(3, 5, 7);
        assertTrue(abr.addAll(list));
        assertEquals(3, abr.size());
        assertTrue(abr.contains(3));
        assertTrue(abr.contains(5));
        assertTrue(abr.contains(7));
    }

    @Test
    public void testRemoveAll() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        List<Integer> list = Arrays.asList(3, 7);
        assertTrue(abr.removeAll(list));
        assertEquals(1, abr.size());
        assertFalse(abr.contains(3));
        assertFalse(abr.contains(7));
        assertTrue(abr.contains(5));
    }

    @Test
    public void testRetainAll() {
        abr.add(5);
        abr.add(3);
        abr.add(7);
        List<Integer> list = Arrays.asList(3, 7);
        assertTrue(abr.retainAll(list));
        assertEquals(2, abr.size());
        assertTrue(abr.contains(3));
        assertTrue(abr.contains(7));
        assertFalse(abr.contains(5));
    }
}




























































































































