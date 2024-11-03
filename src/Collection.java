public interface Collection<T> {
    void ajouter(T valeur);
    boolean contient(T valeur);
    void supprimer(T valeur);
    int taille();
    boolean estVide();
}
