import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Arrays;

public class ARN<E extends Comparable<E>> implements Collection<E> {

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modifié = false;
        for (Object e : c) {
            if (remove(e)) {
                modifié = true;
            }
        }
        return modifié;
    }

    private static final boolean ROUGE = true;
    private static final boolean NOIR = false;

    private class Noeud {
        E clef;
        Noeud gauche, droite, parent;
        boolean couleur;

        Noeud(E clef, boolean couleur, Noeud parent) {
            this.clef = clef;
            this.couleur = couleur;
            this.parent = parent;
        }
    }

    private Noeud racine;
    private int taille = 0;

    @Override
    public int size() {
        return taille;
    }

    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    @Override
    public boolean contains(Object o) {
        return obtenirNoeud(o) != null;
    }

    private Noeud obtenirNoeud(Object o) {
        Noeud courant = racine;
        while (courant != null) {
            int cmp = ((Comparable<E>) o).compareTo(courant.clef);
            if (cmp < 0) {
                courant = courant.gauche;
            } else if (cmp > 0) {
                courant = courant.droite;
            } else {
                return courant;
            }
        }
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Noeud suivant = minimum(racine);
            private Noeud dernierRetourné = null;
            private int compteurModificationAttendu = taille;

            private Noeud minimum(Noeud noeud) {
                while (noeud != null && noeud.gauche != null) {
                    noeud = noeud.gauche;
                }
                return noeud;
            }

                    @Override
                    public boolean hasNext() {
                        return suivant != null;
                    }
        
                    @Override
                    public E next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        if (taille != compteurModificationAttendu) {
                            throw new ConcurrentModificationException();
                        }
                        dernierRetourné = suivant;
                        suivant = successeur(suivant);
                        return dernierRetourné.clef;
                    }
        
                    @Override
                    public void remove() {
                        if (dernierRetourné == null) {
                            throw new IllegalStateException();
                        }
                        if (taille != compteurModificationAttendu) {
                            throw new ConcurrentModificationException();
                        }
                        supprimerNoeud(dernierRetourné);
                        dernierRetourné = null;
                        taille--;
                        compteurModificationAttendu = taille;
                        
                    }
        
                    private Noeud successeur(Noeud noeud) {
                        if (noeud == null) {
                            return null;
                        } else if (noeud.droite != null) {
                            Noeud p = noeud.droite;
                            while (p.gauche != null) {
                                p = p.gauche;
                            }
                            return p;
                        } else {
                            Noeud p = noeud.parent;
                            Noeud ch = noeud;
                            while (p != null && ch == p.droite) {
                                ch = p;
                                p = p.parent;
                            }
                            return p;
                        }
                    }
                };
            }
    @Override
    public Object[] toArray() {
        Object[] tableau = new Object[taille];
        int i = 0;
        for (E e : this) {
            tableau[i++] = e;
        }
        return tableau;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < taille) {
            return (T[]) Arrays.copyOf(toArray(), taille, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, taille);
        if (a.length > taille) {
            a[taille] = null;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        Noeud parent = null;
        Noeud courant = racine;
        while (courant != null) {
            parent = courant;
            int cmp = ((Comparable<E>) e).compareTo(courant.clef);
            if (cmp < 0) {
                courant = courant.gauche;
            } else if (cmp > 0) {
                courant = courant.droite;
            } else {
                return false;
            }
        }

        Noeud nouveauNoeud = new Noeud(e, ROUGE, parent);
        if (parent == null) {
            racine = nouveauNoeud;
        } else if (e.compareTo(parent.clef) < 0) {
            parent.gauche = nouveauNoeud;
        } else {
            parent.droite = nouveauNoeud;
        }

        corrigerAprèsInsertion(nouveauNoeud);
        taille++;
        return true;
    }

    private void corrigerAprèsInsertion(Noeud x) {
        x.couleur = ROUGE;

        while (x != null && x != racine && x.parent.couleur == ROUGE) {
            if (parentDe(x) == gaucheDe(parentDe(parentDe(x)))) {
                Noeud y = droiteDe(parentDe(parentDe(x)));
                if (couleurDe(y) == ROUGE) {
                    setColor(parentDe(x), NOIR);
                    setColor(y, NOIR);
                    setColor(parentDe(parentDe(x)), ROUGE);
                    x = parentDe(parentDe(x));
                } else {
                    if (x == droiteDe(parentDe(x))) {
                        x = parentDe(x);
                        tournerGauche(x);
                    }
                    setColor(parentDe(x), NOIR);
                    setColor(parentDe(parentDe(x)), ROUGE);
                    tournerDroite(parentDe(parentDe(x)));
                }
            } else {
                Noeud y = gaucheDe(parentDe(parentDe(x)));
                if (couleurDe(y) == ROUGE) {
                    setColor(parentDe(x), NOIR);
                    setColor(y, NOIR);
                    setColor(parentDe(parentDe(x)), ROUGE);
                    x = parentDe(parentDe(x));
                } else {
                    if (x == gaucheDe(parentDe(x))) {
                        x = parentDe(x);
                        tournerDroite(x);
                    }
                    setColor(parentDe(x), NOIR);
                    setColor(parentDe(parentDe(x)), ROUGE);
                    tournerGauche(parentDe(parentDe(x)));
                }
            }
        }
        racine.couleur = NOIR;
    }

    private boolean couleurDe(Noeud p) {
        return (p == null ? NOIR : p.couleur);
    }

    private Noeud parentDe(Noeud p) {
        return (p == null ? null : p.parent);
    }

    private void setColor(Noeud p, boolean c) {
        if (p != null) {
            p.couleur = c;
        }
    }

    private Noeud gaucheDe(Noeud p) {
        return (p == null) ? null : p.gauche;
    }

    private Noeud droiteDe(Noeud p) {
        return (p == null) ? null : p.droite;
    }

    private void tournerGauche(Noeud p) {
        if (p != null) {
            Noeud r = p.droite;
            p.droite = r.gauche;
            if (r.gauche != null) {
                r.gauche.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                racine = r;
            } else if (p.parent.gauche == p) {
                p.parent.gauche = r;
            } else {
                p.parent.droite = r;
            }
            r.gauche = p;
            p.parent = r;
        }
    }

    private void tournerDroite(Noeud p) {
        if (p != null) {
            Noeud l = p.gauche;
            p.gauche = l.droite;
            if (l.droite != null) {
                l.droite.parent = p;
            }
            l.parent = p.parent;
            if (p.parent == null) {
                racine = l;
            } else if (p.parent.droite == p) {
                p.parent.droite = l;
            } else {
                p.parent.gauche = l;
            }
            l.droite = p;
            p.parent = l;
        }
    }

    @Override
    public boolean remove(Object o) {
        Noeud noeud = obtenirNoeud(o);
        if (noeud == null) {
            return false;
        }

        supprimerNoeud(noeud);
        taille--;
        return true;
    }

    private void supprimerNoeud(Noeud noeud) {
        if (noeud.gauche != null && noeud.droite != null) {
            Noeud successeur = minimum(noeud.droite);
            noeud.clef = successeur.clef;
            noeud = successeur;
        }

        Noeud remplacement = (noeud.gauche != null) ? noeud.gauche : noeud.droite;

        if (remplacement != null) {
            remplacement.parent = noeud.parent;
            if (noeud.parent == null) {
                racine = remplacement;
            } else if (noeud == noeud.parent.gauche) {
                noeud.parent.gauche = remplacement;
            } else {
                noeud.parent.droite = remplacement;
            }

            noeud.gauche = noeud.droite = noeud.parent = null;

            if (couleurDe(noeud) == NOIR) {
                corrigerAprèsSuppression(remplacement);
            }
        } else if (noeud.parent == null) {
            racine = null;
        } else {
            if (couleurDe(noeud) == NOIR) {
                corrigerAprèsSuppression(noeud);
            }

            if (noeud.parent != null) {
                if (noeud == noeud.parent.gauche) {
                    noeud.parent.gauche = null;
                } else if (noeud == noeud.parent.droite) {
                    noeud.parent.droite = null;
                }
                noeud.parent = null;
            }
        }
    }

    private Noeud minimum(Noeud noeud) {
        while (noeud.gauche != null) {
            noeud = noeud.gauche;
        }
        return noeud;
    }

    private void corrigerAprèsSuppression(Noeud p) {
        while (p != racine && couleurDe(p) == NOIR) {
            if (p == gaucheDe(parentDe(p))) {
                Noeud frere = droiteDe(parentDe(p));
                if (couleurDe(frere) == ROUGE) {
                    setColor(frere, NOIR);
                    setColor(parentDe(p), ROUGE);
                    tournerGauche(parentDe(p));
                    frere = droiteDe(parentDe(p));
                }
                if (couleurDe(gaucheDe(frere)) == NOIR && couleurDe(droiteDe(frere)) == NOIR) {
                    setColor(frere, ROUGE);
                    p = parentDe(p);
                } else {
                                                    if (couleurDe(droiteDe(frere)) == NOIR) {
                                                        setColor(gaucheDe(frere), NOIR);
                                                        setColor(frere, ROUGE);
                                                        tournerDroite(frere);
                                                        frere = droiteDe(parentDe(p));
                                                    }
                                                    setColor(frere, couleurDe(parentDe(p)));
                                                    setColor(parentDe(p), NOIR);
                                                    setColor(droiteDe(frere), NOIR);
                                                    tournerGauche(parentDe(p));
                                                    p = racine;
                                                }
                                            } else {
                                                Noeud frere = gaucheDe(parentDe(p));
                                                if (couleurDe(frere) == ROUGE) {
                                                    setColor(frere, NOIR);
                                                    setColor(parentDe(p), ROUGE);
                                                    tournerDroite(parentDe(p));
                                                    frere = gaucheDe(parentDe(p));
                                                }
                                                if (couleurDe(droiteDe(frere)) == NOIR && couleurDe(gaucheDe(frere)) == NOIR) {
                                                    setColor(frere, ROUGE);
                                                    p = parentDe(p);
                                                } else {
                                                    if (couleurDe(gaucheDe(frere)) == NOIR) {
                                                        setColor(droiteDe(frere), NOIR);
                                                        setColor(frere, ROUGE);
                                                        tournerGauche(frere);
                                                        frere = gaucheDe(parentDe(p));
                                                    }
                                                    setColor(frere, couleurDe(parentDe(p)));
                                                    setColor(parentDe(p), NOIR);
                                                    setColor(gaucheDe(frere), NOIR);
                                                    tournerDroite(parentDe(p));
                                                    p = racine;
                                                }
                                            }
                                        }
                                        setColor(p, NOIR);
                                    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modifié = false;
        for (E e : c) {
            if (add(e)) {
                modifié = true;
            }
        }
        return modifié;
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
    public boolean retainAll(Collection<?> c) {
        boolean modifié = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modifié = true;
            }
        }
        return modifié;
    }

    @Override
    public void clear() {
        racine = null;
        taille = 0;
    }
}