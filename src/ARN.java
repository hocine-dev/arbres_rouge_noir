import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Arrays;

public class ARN<E extends Comparable<E>> implements Collection<E> {

    @Override
    public boolean removeAll(Collection<?> c) {
        // Initialise un indicateur pour suivre si des éléments ont été supprimés
        boolean modifié = false;
        
        // Parcourt chaque élément de la collection passée en paramètre
        for (Object e : c) {
            // Si l'élément est supprimé avec succès, mettre à jour l'indicateur
            if (remove(e)) {
                modifié = true;
            }
        }
        
        // Retourne vrai si au moins un élément a été supprimé, sinon faux
        return modifié;
    }

    private static final boolean ROUGE = true;
    private static final boolean NOIR = false;

    private class Noeud {
        // La clé stockée dans le nœud
        E clef;
        
        // Références aux nœuds gauche, droit et parent
        Noeud gauche, droite, parent;
        
        // Couleur du nœud (utilisé dans les arbres rouge-noir)
        boolean couleur;
    
        // Constructeur pour initialiser un nœud avec une clé, une couleur et un parent
        Noeud(E clef, boolean couleur, Noeud parent) {
            this.clef = clef;
            this.couleur = couleur;
            this.parent = parent;
        }
    }
    
    // La racine de l'arbre
    private Noeud racine;
    
    // La taille de l'arbre (nombre de nœuds)
    private int taille = 0;

    @Override
    public int size() {
        // Retourne la taille de l'arbre (nombre de nœuds)
        return taille;
    }
    
    @Override
    public boolean isEmpty() {
        // Vérifie si l'arbre est vide (taille égale à 0)
        return taille == 0;
    }
    
    @Override
    public boolean contains(Object o) {
        // Vérifie si l'objet est présent dans l'arbre en obtenant le nœud correspondant
        return obtenirNoeud(o) != null;
    }

    private Noeud obtenirNoeud(Object o) {
        // Commence la recherche à partir de la racine de l'arbre
        Noeud courant = racine;
        
        // Parcourt l'arbre jusqu'à ce que le nœud soit trouvé ou que la fin soit atteinte
        while (courant != null) {
            // Compare l'objet avec la clé du nœud courant
            int cmp = ((Comparable<E>) o).compareTo(courant.clef);
            
            // Si l'objet est plus petit, aller à gauche
            if (cmp < 0) {
                courant = courant.gauche;
            // Si l'objet est plus grand, aller à droite
            } else if (cmp > 0) {
                courant = courant.droite;
            // Si l'objet est égal, retourner le nœud courant
            } else {
                return courant;
            }
        }
        
        // Retourne null si l'objet n'est pas trouvé
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            // Le prochain nœud à retourner par l'itérateur
            private Noeud suivant = minimum(racine);
            
            // Le dernier nœud retourné par l'itérateur
            private Noeud dernierRetourné = null;
            
            // Compteur de modifications attendu pour détecter les modifications concurrentes
            private int compteurModificationAttendu = taille;
    
            // Trouve le nœud avec la valeur minimale dans un sous-arbre
            private Noeud minimum(Noeud noeud) {
                while (noeud != null && noeud.gauche != null) {
                    noeud = noeud.gauche;
                }
                return noeud;
            }
    
            @Override
            public boolean hasNext() {
                // Vérifie s'il y a un autre nœud à retourner
                return suivant != null;
            }
    
            @Override
            public E next() {
                // Vérifie s'il y a un autre nœud à retourner
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                // Vérifie si l'arbre a été modifié de manière concurrente
                if (taille != compteurModificationAttendu) {
                    throw new ConcurrentModificationException();
                }
                // Met à jour le dernier nœud retourné et trouve le suivant
                dernierRetourné = suivant;
                suivant = successeur(suivant);
                return dernierRetourné.clef;
            }
    
            @Override
            public void remove() {
                // Vérifie si le dernier nœud retourné est valide
                if (dernierRetourné == null) {
                    throw new IllegalStateException();
                }
                // Vérifie si l'arbre a été modifié de manière concurrente
                if (taille != compteurModificationAttendu) {
                    throw new ConcurrentModificationException();
                }
                // Supprime le dernier nœud retourné et met à jour les compteurs
                supprimerNoeud(dernierRetourné);
                dernierRetourné = null;
                taille--;
                compteurModificationAttendu = taille;
            }
    
            // Trouve le successeur d'un nœud dans l'arbre
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
    // Crée un tableau de la taille de l'arbre
    Object[] tableau = new Object[taille];
    int i = 0;
    
    // Remplit le tableau avec les éléments de l'arbre
    for (E e : this) {
        tableau[i++] = e;
    }
    
    // Retourne le tableau rempli
    return tableau;
}

@Override
public <T> T[] toArray(T[] a) {
    // Si le tableau passé en paramètre est trop petit, crée un nouveau tableau
    if (a.length < taille) {
        return (T[]) Arrays.copyOf(toArray(), taille, a.getClass());
    }
    
    // Copie les éléments de l'arbre dans le tableau passé en paramètre
    System.arraycopy(toArray(), 0, a, 0, taille);
    
    // Si le tableau est plus grand que nécessaire, met l'élément suivant à null
    if (a.length > taille) {
        a[taille] = null;
    }
    
    // Retourne le tableau rempli
    return a;
}


@Override
public boolean add(E e) {
    // Initialise les références pour parcourir l'arbre
    Noeud parent = null;
    Noeud courant = racine;
    
    // Parcourt l'arbre pour trouver la position d'insertion
    while (courant != null) {
        parent = courant;
        int cmp = ((Comparable<E>) e).compareTo(courant.clef);
        if (cmp < 0) {
            courant = courant.gauche;
        } else if (cmp > 0) {
            courant = courant.droite;
        } else {
            // L'élément est déjà présent dans l'arbre
            return false;
        }
    }

    // Crée un nouveau nœud pour l'élément à ajouter
    Noeud nouveauNoeud = new Noeud(e, ROUGE, parent);
    if (parent == null) {
        // L'arbre était vide, le nouveau nœud devient la racine
        racine = nouveauNoeud;
    } else if (e.compareTo(parent.clef) < 0) {
        parent.gauche = nouveauNoeud;
    } else {
        parent.droite = nouveauNoeud;
    }

    // Corrige les propriétés de l'arbre rouge-noir après l'insertion
    corrigerAprèsInsertion(nouveauNoeud);
    taille++;
    return true;
}

private void corrigerAprèsInsertion(Noeud x) {
    x.couleur = ROUGE;

    // Boucle pour corriger les violations des propriétés de l'arbre rouge-noir
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
    // Obtient le nœud correspondant à l'objet à supprimer
    Noeud noeud = obtenirNoeud(o);
    if (noeud == null) {
        // Si le nœud n'existe pas, retourne false
        return false;
    }

    // Supprime le nœud de l'arbre
    supprimerNoeud(noeud);
    taille--;
    return true;
}

private void supprimerNoeud(Noeud noeud) {
    // Si le nœud a deux enfants, trouve son successeur
    if (noeud.gauche != null && noeud.droite != null) {
        Noeud successeur = minimum(noeud.droite);
        noeud.clef = successeur.clef;
        noeud = successeur;
    }

    // Trouve le remplacement du nœud à supprimer
    Noeud remplacement = (noeud.gauche != null) ? noeud.gauche : noeud.droite;

    if (remplacement != null) {
        // Relie le remplacement au parent du nœud à supprimer
        remplacement.parent = noeud.parent;
        if (noeud.parent == null) {
            racine = remplacement;
        } else if (noeud == noeud.parent.gauche) {
            noeud.parent.gauche = remplacement;
        } else {
            noeud.parent.droite = remplacement;
        }

        // Déconnecte le nœud à supprimer
        noeud.gauche = noeud.droite = noeud.parent = null;

        // Corrige les propriétés de l'arbre rouge-noir si nécessaire
        if (couleurDe(noeud) == NOIR) {
            corrigerAprèsSuppression(remplacement);
        }
    } else if (noeud.parent == null) {
        // Si le nœud à supprimer est la racine et n'a pas d'enfants
        racine = null;
    } else {
        // Si le nœud à supprimer n'a pas de remplacement
        if (couleurDe(noeud) == NOIR) {
            corrigerAprèsSuppression(noeud);
        }

        // Déconnecte le nœud à supprimer de son parent
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
    // Trouve le nœud avec la valeur minimale dans un sous-arbre
    while (noeud.gauche != null) {
        noeud = noeud.gauche;
    }
    return noeud;
}

private void corrigerAprèsSuppression(Noeud p) {
    // Boucle pour corriger les violations des propriétés de l'arbre rouge-noir
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
    // Initialise un indicateur pour suivre si des éléments ont été ajoutés
    boolean modifié = false;
    
    // Parcourt chaque élément de la collection passée en paramètre
    for (E e : c) {
        // Si l'élément est ajouté avec succès, mettre à jour l'indicateur
        if (add(e)) {
            modifié = true;
        }
    }
    
    // Retourne vrai si au moins un élément a été ajouté, sinon faux
    return modifié;
}

@Override
public boolean containsAll(Collection<?> c) {
    // Parcourt chaque élément de la collection passée en paramètre
    for (Object e : c) {
        // Si un élément n'est pas présent dans l'arbre, retourne faux
        if (!contains(e)) {
            return false;
        }
    }
    
    // Retourne vrai si tous les éléments sont présents dans l'arbre
    return true;
}

@Override
public boolean retainAll(Collection<?> c) {
    // Initialise un indicateur pour suivre si des éléments ont été supprimés
    boolean modifié = false;
    
    // Utilise un itérateur pour parcourir l'arbre
    Iterator<E> it = iterator();
    while (it.hasNext()) {
        // Si un élément n'est pas présent dans la collection, le supprimer
        if (!c.contains(it.next())) {
            it.remove();
            modifié = true;
        }
    }
    
    // Retourne vrai si au moins un élément a été supprimé, sinon faux
    return modifié;
}

@Override
public void clear() {
    // Réinitialise la racine de l'arbre et la taille à 0
    racine = null;
    taille = 0;
}
}