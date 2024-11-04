import java.util.Collection;
import java.util.Iterator;

public class ARN<E extends Comparable<E>> implements Collection<E> {

    private static final boolean ROUGE = true;
    private static final boolean NOIR = false;

    class Noeud {
        E valeur;
        Noeud gauche, droite, parent;
        boolean couleur;

        Noeud(E valeur, boolean couleur, Noeud parent) {
            this.valeur = valeur;
            this.couleur = couleur;
            this.parent = parent;
        }
    }

    Noeud racine;
    private int taille = 0;

    @Override
    public boolean add(E element) {
        if (racine == null) {
            racine = new Noeud(element, NOIR, null);
            taille++;
            return true;
        }
        Noeud parent = null;
        Noeud courant = racine;
        while (courant != null) {
            parent = courant;
            int comparaison = element.compareTo(courant.valeur);
            if (comparaison < 0) {
                courant = courant.gauche;
            } else if (comparaison > 0) {
                courant = courant.droite;
            } else {
                return false; // Pas de doublons
            }
        }

        Noeud nouveauNoeud = new Noeud(element, ROUGE, parent);
        if (element.compareTo(parent.valeur) < 0) {
            parent.gauche = nouveauNoeud;
        } else {
            parent.droite = nouveauNoeud;
        }

        taille++;
        corrigerApresInsertion(nouveauNoeud);
        return true;
    }

    private void corrigerApresInsertion(Noeud noeud) {
        while (noeud != racine && noeud.parent.couleur == ROUGE) {
            if (noeud.parent == noeud.parent.parent.gauche) {
                Noeud oncle = noeud.parent.parent.droite;
                if (oncle != null && oncle.couleur == ROUGE) {
                    noeud.parent.couleur = NOIR;
                    oncle.couleur = NOIR;
                    noeud.parent.parent.couleur = ROUGE;
                    noeud = noeud.parent.parent;
                } else {
                    if (noeud == noeud.parent.droite) {
                        noeud = noeud.parent;
                        rotationGauche(noeud);
                    }
                    noeud.parent.couleur = NOIR;
                    noeud.parent.parent.couleur = ROUGE;
                    rotationDroite(noeud.parent.parent);
                }
            } else {
                Noeud oncle = noeud.parent.parent.gauche;
                if (oncle != null && oncle.couleur == ROUGE) {
                    noeud.parent.couleur = NOIR;
                    oncle.couleur = NOIR;
                    noeud.parent.parent.couleur = ROUGE;
                    noeud = noeud.parent.parent;
                } else {
                    if (noeud == noeud.parent.gauche) {
                        noeud = noeud.parent;
                        rotationDroite(noeud);
                    }
                    noeud.parent.couleur = NOIR;
                    noeud.parent.parent.couleur = ROUGE;
                    rotationGauche(noeud.parent.parent);
                }
            }
        }
        racine.couleur = NOIR;
    }

    private void rotationGauche(Noeud noeud) {
        Noeud droite = noeud.droite;
        noeud.droite = droite.gauche;
        if (droite.gauche != null) {
            droite.gauche.parent = noeud;
        }
        droite.parent = noeud.parent;
        if (noeud.parent == null) {
            racine = droite;
        } else if (noeud == noeud.parent.gauche) {
            noeud.parent.gauche = droite;
        } else {
            noeud.parent.droite = droite;
        }
        droite.gauche = noeud;
        noeud.parent = droite;
    }

    private void rotationDroite(Noeud noeud) {
        Noeud gauche = noeud.gauche;
        noeud.gauche = gauche.droite;
        if (gauche.droite != null) {
            gauche.droite.parent = noeud;
        }
        gauche.parent = noeud.parent;
        if (noeud.parent == null) {
            racine = gauche;
        } else if (noeud == noeud.parent.droite) {
            noeud.parent.droite = gauche;
        } else {
            noeud.parent.gauche = gauche;
        }
        gauche.droite = noeud;
        noeud.parent = gauche;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Retrait non implémenté.");
    }

    @Override
    public boolean contains(Object o) {
        Noeud courant = racine;
        while (courant != null) {
            int comparaison = ((E) o).compareTo(courant.valeur);
            if (comparaison < 0) {
                courant = courant.gauche;
            } else if (comparaison > 0) {
                courant = courant.droite;
            } else {
                return true;
            }
        }
        return false;
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
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator non implémenté.");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray non implémenté.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray non implémenté.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modifie = false;
        for (E e : c) {
            if (add(e)) {
                modifie = true;
            }
        }
        return modifie;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll non implémenté.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll non implémenté.");
    }

    @Override
    public void clear() {
        racine = null;
        taille = 0;
    }
}
