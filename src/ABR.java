import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ABR<T extends Comparable<T>> implements Collection<T> {
    private class Noeud {
        T valeur;
        Noeud gauche;
        Noeud droit;

        Noeud(T valeur) {
            this.valeur = valeur;
            this.gauche = null;
            this.droit = null;
        }
    }

    private Noeud racine;
    private int taille;

    public ABR() {
        this.racine = null;
        this.taille = 0;
    }

    @Override
    public boolean add(T valeur) {
        if (!contains(valeur)) {
            racine = ajouterRec(racine, valeur);
            taille++;
            return true;
        }
        return false;
    }

    private Noeud ajouterRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return new Noeud(valeur);
        }
        if (valeur.compareTo(noeud.valeur) < 0) {
            noeud.gauche = ajouterRec(noeud.gauche, valeur);
        } else {
            noeud.droit = ajouterRec(noeud.droit, valeur);
        }
        return noeud;
    }

    @Override
    public boolean contains(Object o) {
        try {
            @SuppressWarnings("unchecked")
            T valeur = (T) o;
            return contientRec(racine, valeur);
        } catch (ClassCastException e) {
            return false;
        }
    }

    private boolean contientRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return false;
        }
        if (valeur.equals(noeud.valeur)) {
            return true;
        }
        return valeur.compareTo(noeud.valeur) < 0
            ? contientRec(noeud.gauche, valeur)
            : contientRec(noeud.droit, valeur);
    }

    @Override
    public boolean remove(Object o) {
        try {
            @SuppressWarnings("unchecked")
            T valeur = (T) o;
            if (contains(valeur)) {
                racine = supprimerRec(racine, valeur);
                taille--;
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private Noeud supprimerRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return null;
        }

        if (valeur.compareTo(noeud.valeur) < 0) {
            noeud.gauche = supprimerRec(noeud.gauche, valeur);
        } else if (valeur.compareTo(noeud.valeur) > 0) {
            noeud.droit = supprimerRec(noeud.droit, valeur);
        } else {
            if (noeud.gauche == null && noeud.droit == null) {
                return null;
            }
            if (noeud.gauche == null) {
                return noeud.droit;
            } else if (noeud.droit == null) {
                return noeud.gauche;
            }
            Noeud successeur = min(noeud.droit);
            noeud.valeur = successeur.valeur;
            noeud.droit = supprimerRec(noeud.droit, successeur.valeur);
        }
        return noeud;
    }

    private Noeud min(Noeud noeud) {
        if (noeud.gauche == null) {
            return noeud;
        }
        return min(noeud.gauche);
    }

    @Override
    public int size() {
        return taille;
    }

    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    @Override
    public void clear() {
        racine = null;
        taille = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ABRIterator();
    }

    private class ABRIterator implements Iterator<T> {
        private Noeud current;
        private Noeud next;

        public ABRIterator() {
            next = racine;
            if (next != null) {
                while (next.gauche != null) {
                    next = next.gauche;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = next;

            // Traverser l'arbre pour trouver le prochain nœud en ordre croissant
            if (next.droit != null) {
                next = next.droit;
                while (next.gauche != null) {
                    next = next.gauche;
                }
            } else {
                Noeud parent = racine;
                Noeud child = next;
                while (parent != null && child == parent.droit) {
                    child = parent;
                    parent = parent.gauche;
                }
                next = parent;
            }
            return current.valeur;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[taille];
        int i = 0;
        for (T t : this) {
            array[i++] = t;
        }
        return array;
    }

    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < taille) {
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), taille);
        }
        int i = 0;
        for (T t : this) {
            a[i++] = (E) t;
        }
        if (a.length > taille) {
            a[taille] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T elem = it.next();
            if (!c.contains(elem)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }
}
