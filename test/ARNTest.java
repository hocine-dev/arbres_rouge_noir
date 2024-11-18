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
        // Teste l'ajout d'éléments
        assertTrue(arn.add(10));
        assertTrue(arn.add(20));
        assertTrue(arn.add(5));
        assertFalse(arn.add(10)); // Élément dupliqué
        assertEquals(3, arn.size());
    }

    @Test
    public void testRemove() {
        // Teste la suppression d'éléments
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.remove(10));
        assertFalse(arn.remove(10)); // Élément déjà supprimé
        assertEquals(2, arn.size());
    }

    @Test
    public void testContains() {
        // Teste la présence d'éléments
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.contains(10));
        assertFalse(arn.contains(15));
    }

    @Test
    public void testIsEmpty() {
        // Teste si l'arbre est vide
        assertTrue(arn.isEmpty());
        arn.add(10);
        assertFalse(arn.isEmpty());
    }

    @Test
    public void testSize() {
        // Teste la taille de l'arbre
        assertEquals(0, arn.size());
        arn.add(10);
        arn.add(20);
        assertEquals(2, arn.size());
    }

    @Test
    public void testClear() {
        // Teste la suppression de tous les éléments
        arn.add(10);
        arn.add(20);
        arn.clear();
        assertTrue(arn.isEmpty());
        assertEquals(0, arn.size());
    }

    @Test
    public void testIterator() {
        // Teste l'itérateur
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
        // Teste la conversion en tableau
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Object[] array = arn.toArray();
        assertArrayEquals(new Object[]{5, 10, 20}, array);
    }

    @Test
    public void testToArrayWithType() {
        // Teste la conversion en tableau avec type
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Integer[] array = arn.toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{5, 10, 20}, array);
    }

    @Test
    public void testAddAll() {
        // Teste l'ajout de tous les éléments d'une collection
        assertTrue(arn.addAll(Arrays.asList(10, 20, 5)));
        assertEquals(3, arn.size());
    }

    @Test
    public void testRemoveAll() {
        // Teste la suppression de tous les éléments d'une collection
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.removeAll(Arrays.asList(10, 5)));
        assertEquals(1, arn.size());
    }

    @Test
    public void testContainsAll() {
        // Teste la présence de tous les éléments d'une collection
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.containsAll(Arrays.asList(10, 5)));
        assertFalse(arn.containsAll(Arrays.asList(10, 15)));
    }

    @Test
    public void testRetainAll() {
        // Teste la conservation de tous les éléments d'une collection
        arn.add(10);
        arn.add(20);
        arn.add(5);
        assertTrue(arn.retainAll(Arrays.asList(10, 5)));
        assertEquals(2, arn.size());
    }

    @Test
    public void testIteratorRemove() {
        // Teste la suppression d'éléments via l'itérateur
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
        // Teste la modification concurrente via l'itérateur
        arn.add(10);
        arn.add(20);
        arn.add(5);
        Iterator<Integer> it = arn.iterator();
        arn.add(15);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void testIteratorNoSuchElementException() {
        // Teste l'exception NoSuchElementException via l'itérateur
        arn.add(10);
        Iterator<Integer> it = arn.iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }
}