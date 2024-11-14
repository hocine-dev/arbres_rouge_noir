import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Classe ABR (Arbre Binaire de Recherche) générique qui implémente l'interface Collection.
 * 
 * @param <T> Le type des éléments stockés dans l'arbre, qui doit être comparable.
 */
public class ABR<T extends Comparable<T>> implements Collection<T> {

    /**
     * Classe interne représentant un nœud de l'arbre.
     */
    private class Noeud {
        T valeur;
        Noeud gauche;
        Noeud droit;

        /**
         * Constructeur pour créer un nouveau nœud avec une valeur donnée.
         * 
         * @param valeur La valeur du nœud.
         */
        Noeud(T valeur) {
            this.valeur = valeur;
            this.gauche = null;
            this.droit = null;
        }
    }

    private Noeud racine;
    private int taille;

    /**
     * Constructeur pour créer un arbre binaire de recherche vide.
     */
    public ABR() {
        this.racine = null;
        this.taille = 0;
    }

    /**
     * Ajoute une valeur à l'arbre s'il n'est pas déjà présent.
     * 
     * @param valeur La valeur à ajouter.
     * @return true si la valeur a été ajoutée, false si elle était déjà présente.
     */
    @Override
    public boolean add(T valeur) {
        if (!contains(valeur)) {
            racine = ajouterRec(racine, valeur);
            taille++;
            return true;
        }
        return false;
    }

    /**
     * Méthode récursive pour ajouter une valeur à l'arbre.
     * 
     * @param noeud Le nœud courant.
     * @param valeur La valeur à ajouter.
     * @return Le nœud mis à jour.
     */
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

    /**
     * Vérifie si une valeur est présente dans l'arbre.
     * 
     * @param o L'objet à vérifier.
     * @return true si l'objet est présent, false sinon.
     */
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

    /**
     * Méthode récursive pour vérifier si une valeur est présente dans l'arbre.
     * 
     * @param noeud Le nœud courant.
     * @param valeur La valeur à vérifier.
     * @return true si la valeur est présente, false sinon.
     */
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

    /**
     * Supprime une valeur de l'arbre si elle est présente.
     * 
     * @param o L'objet à supprimer.
     * @return true si l'objet a été supprimé, false sinon.
     */
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

    /**
     * Méthode récursive pour supprimer une valeur de l'arbre.
     * 
     * @param noeud Le nœud courant.
     * @param valeur La valeur à supprimer.
     * @return Le nœud mis à jour.
     */
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

    /**
     * Trouve le nœud avec la valeur minimale dans un sous-arbre.
     * 
     * @param noeud Le nœud courant.
     * @return Le nœud avec la valeur minimale.
     */
    private Noeud min(Noeud noeud) {
        if (noeud.gauche == null) {
            return noeud;
        }
        return min(noeud.gauche);
    }

    /**
     * Retourne la taille de l'arbre.
     * 
     * @return Le nombre d'éléments dans l'arbre.
     */
    @Override
    public int size() {
        return taille;
    }

    /**
     * Vérifie si l'arbre est vide.
     * 
     * @return true si l'arbre est vide, false sinon.
     */
    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    /**
     * Vide l'arbre.
     */
    @Override
    public void clear() {
        racine = null;
        taille = 0;
    }

    /**
     * Retourne un itérateur pour parcourir l'arbre.
     * 
     * @return Un itérateur pour parcourir l'arbre.
     */
    @Override
    public Iterator<T> iterator() {
        return new ABRIterator();
    }

    /**
     * Classe interne pour l'itération sur l'arbre.
     */
    private class ABRIterator implements Iterator<T> {
        private Noeud current;
        private Noeud next;

        /**
         * Constructeur pour initialiser l'itérateur.
         */
        public ABRIterator() {
            next = racine;
            if (next != null) {
                while (next.gauche != null) {
                    next = next.gauche;
                }
            }
        }

        /**
         * Vérifie s'il y a un élément suivant dans l'itération.
         * 
         * @return true s'il y a un élément suivant, false sinon.
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Retourne l'élément suivant dans l'itération.
         * 
         * @return L'élément suivant.
         * @throws NoSuchElementException Si aucun élément suivant n'est présent.
         */
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

    /**
     * Retourne un tableau contenant tous les éléments de l'arbre.
     * 
     * @return Un tableau contenant tous les éléments de l'arbre.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[taille];
        int i = 0;
        for (T t : this) {
            array[i++] = t;
        }
        return array;
    }

    /**
     * Remplit un tableau donné avec les éléments de l'arbre.
     * 
     * @param <E> Le type des éléments du tableau.
     * @param a Le tableau à remplir.
     * @return Le tableau rempli.
     */
    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < taille) {
            return (E[]) toArray();
        }
        System.arraycopy(toArray(), 0, a, 0, taille);
        if (a.length > taille) {
            a[taille] = null;
        }
        return a;
    }

    /**
     * Vérifie si l'arbre contient tous les éléments d'une collection donnée.
     * 
     * @param c La collection à vérifier.
     * @return true si l'arbre contient tous les éléments de la collection, false sinon.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ajoute tous les éléments d'une collection donnée à l'arbre.
     * 
     * @param c La collection contenant les éléments à ajouter.
     * @return true si l'arbre a été modifié, false sinon.
     */
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

    /**
     * Supprime tous les éléments d'une collection donnée de l'arbre.
     * 
     * @param c La collection contenant les éléments à supprimer.
     * @return true si l'arbre a été modifié, false sinon.
     */
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

    /**
     * Retient uniquement les éléments de l'arbre qui sont contenus dans une collection donnée.
     * 
     * @param c La collection contenant les éléments à retenir.
     * @return true si l'arbre a été modifié, false sinon.
     */
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
