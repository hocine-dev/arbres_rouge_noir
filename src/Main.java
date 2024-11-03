public class Main {
    public static void main(String[] args) {
        // Créer une instance de l'arbre binaire de recherche
        ABR<Integer> arbre = new ABR<>();

        // Tester l'ajout d'éléments
        System.out.println("Ajout des éléments : 5, 3, 7, 2, 4, 6, 8");
        arbre.ajouter(5);
        arbre.ajouter(3);
        arbre.ajouter(7);
        arbre.ajouter(2);
        arbre.ajouter(4);
        arbre.ajouter(6);
        arbre.ajouter(8);

        // Afficher la taille de l'arbre
        System.out.println("Taille après ajout : " + arbre.taille()); // Devrait être 7

        // Tester la vérification de la présence d'éléments
        System.out.println("Contient 4 ? " + arbre.contient(4)); // Devrait être true
        System.out.println("Contient 10 ? " + arbre.contient(10)); // Devrait être false

        // Tester la suppression d'éléments
        System.out.println("Suppression de l'élément 3");
        arbre.supprimer(3);
        System.out.println("Taille après suppression : " + arbre.taille()); // Devrait être 6
        System.out.println("Contient 3 ? " + arbre.contient(3)); // Devrait être false

        System.out.println("Suppression de l'élément 5 (racine)");
        arbre.supprimer(5);
        System.out.println("Taille après suppression : " + arbre.taille()); // Devrait être 5
        System.out.println("Contient 5 ? " + arbre.contient(5)); // Devrait être false

        System.out.println("Suppression de l'élément 6");
        arbre.supprimer(6);
        System.out.println("Taille après suppression : " + arbre.taille()); // Devrait être 4

        System.out.println("Suppression de l'élément 2 (feuille)");
        arbre.supprimer(2);
        System.out.println("Taille après suppression : " + arbre.taille()); // Devrait être 3

        System.out.println("Suppression de l'élément 7 (un enfant)");
        arbre.supprimer(7);
        System.out.println("Taille après suppression : " + arbre.taille()); // Devrait être 2

    }
}