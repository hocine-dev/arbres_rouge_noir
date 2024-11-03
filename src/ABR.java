public class ABR<T extends Comparable<T>> implements Collection<T> {
    // Classe interne représentant un nœud de l'arbre
    private class Noeud {
        T valeur;     // Valeur stockée dans le nœud
        Noeud gauche; // Référence au sous-arbre gauche
        Noeud droit;  // Référence au sous-arbre droit

        // Constructeur pour initialiser un nouveau nœud
        Noeud(T valeur) {
            this.valeur = valeur;
            this.gauche = null;
            this.droit = null;
        }
    }

    private Noeud racine; // Référence au nœud racine de l'arbre
    private int taille;   // Nombre d'éléments dans l'arbre

    // Constructeur pour initialiser un arbre vide
    public ABR() {
        this.racine = null; // La racine est nulle pour un arbre vide
        this.taille = 0;    // Taille initiale est 0
    }

    @Override
    public void ajouter(T valeur) {
        // Si la valeur n'est pas déjà présente, nous l'ajoutons
        if (!contient(valeur)) {
            racine = ajouterRec(racine, valeur); // Appel à la méthode récursive d'ajout
            taille++; // Incrémente la taille uniquement si un nouvel élément est ajouté
        }
    }

    // Méthode récursive pour ajouter une valeur dans l'arbre
    private Noeud ajouterRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return new Noeud(valeur); // Crée un nouveau nœud si nous atteignons une position vide
        }
        // Comparer les valeurs pour déterminer où ajouter le nouveau nœud
        if (valeur.compareTo(noeud.valeur) < 0) {
            noeud.gauche = ajouterRec(noeud.gauche, valeur); // Ajoute à gauche
        } else {
            noeud.droit = ajouterRec(noeud.droit, valeur); // Ajoute à droite
        }
        return noeud; // Retourne le nœud (inchangé) en cas de non ajout
    }

    @Override
    public boolean contient(T valeur) {
        return contientRec(racine, valeur); // Appel à la méthode récursive pour vérifier la présence de la valeur
    }

    // Méthode récursive pour tester si une valeur est dans l'arbre
    private boolean contientRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return false; // Si le nœud est nul, la valeur n'est pas présente
        }
        if (valeur.equals(noeud.valeur)) {
            return true; // La valeur a été trouvée
        }
        // Vérifier dans le sous-arbre gauche ou droit selon la comparaison
        return valeur.compareTo(noeud.valeur) < 0
            ? contientRec(noeud.gauche, valeur)
            : contientRec(noeud.droit, valeur);
    }

    @Override
    public void supprimer(T valeur) {
        racine = supprimerRec(racine, valeur); // Appel à la méthode récursive pour supprimer
    }

    // Méthode récursive pour supprimer un nœud dans l'arbre
    private Noeud supprimerRec(Noeud noeud, T valeur) {
        if (noeud == null) {
            return null; // Rien à supprimer
        }

        // Comparer les valeurs pour trouver le nœud à supprimer
        if (valeur.compareTo(noeud.valeur) < 0) {
            noeud.gauche = supprimerRec(noeud.gauche, valeur); // Recherche à gauche
        } else if (valeur.compareTo(noeud.valeur) > 0) {
            noeud.droit = supprimerRec(noeud.droit, valeur); // Recherche à droite
        } else {
            // Noeud à supprimer trouvé
            // Cas 1 : Noeud sans enfant
            if (noeud.gauche == null && noeud.droit == null) {
                taille--; // Décrémenter ici
                return null; // Supprimer le nœud
            }

            // Cas 2 : Noeud avec un enfant
            if (noeud.gauche == null) {
                taille--; // Décrémenter ici
                return noeud.droit; // Remplacer par le sous-arbre droit
            } else if (noeud.droit == null) {
                taille--; // Décrémenter ici
                return noeud.gauche; // Remplacer par le sous-arbre gauche
            }

            // Cas 3 : Noeud avec deux enfants
            Noeud successeur = min(noeud.droit); // Trouver le successeur
            noeud.valeur = successeur.valeur; // Remplacer la valeur par celle du successeur
            noeud.droit = supprimerRec(noeud.droit, successeur.valeur); // Supprimer le successeur
            // Ne décrémentez pas la taille ici, car la taille a déjà été décrémentée
        }

        return noeud; // Retourner le nœud mis à jour
    }

    // Méthode pour trouver le nœud avec la valeur minimale dans un sous-arbre
    private Noeud min(Noeud noeud) {
        if (noeud.gauche == null) {
            return noeud; // Le nœud actuel est le minimum
        }
        return min(noeud.gauche); // Continuer à gauche
    }

    @Override
    public int taille() {
        return taille; // Retourner la taille actuelle de l'arbre
    }

    @Override
    public boolean estVide() {
        return taille == 0; // Vérifier si l'arbre est vide
    }
}