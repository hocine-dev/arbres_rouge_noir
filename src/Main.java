import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Instanciation de l'ABR
        ABR<Integer> abr = new ABR<>();
        System.out.println("=================================");
        System.out.println("        Test de l'ABR           ");
        System.out.println("=================================\n");

        abr.add(10);
        abr.add(12);
        abr.add(14);
        abr.add(5);
        abr.add(7);

        System.out.println("********** Résultats **********");
        System.out.println("L'ABR contient 10 : " + abr.contains(10));
        System.out.println("L'ABR contient 15 : " + abr.contains(15));
        System.out.println("Taille de l'ABR : " + abr.size());
        System.out.println("\n******** Affichage des éléments *********");
        System.out.println("Éléments de l'ABR sous forme de tableau : " + Arrays.toString(abr.toArray()));
        System.out.println("***************************************\n");

        System.out.println("*******************************");

        abr.remove(10);
        System.out.println("\n******** Suppression *********");
        System.out.println("L'ABR contient 10 après suppression : " + abr.contains(10));
        System.out.println("Taille de l'ABR après suppression : " + abr.size());
        System.out.println("*******************************");

        abr.clear();
        System.out.println("\n********* Nettoyage **********");
        System.out.println("Taille de l'ABR après nettoyage : " + abr.size());
        System.out.println("*******************************");

        // Test des méthodes supplémentaires addAll, containsAll, removeAll, retainAll
        System.out.println("\n=================================");
        System.out.println("     Test des Méthodes Supplémentaires     ");
        System.out.println("=================================\n");

        System.out.println("********** Méthodes **********");
        System.out.println("Taille de l'ABR après removeAll : " + abr.size());
        abr.retainAll(java.util.Arrays.asList(3, 4, 5));
        System.out.println("Taille de l'ABR après retainAll : " + abr.size());

        abr.add(3);
        System.out.println("\n********** Résultats **********");
        System.out.println("L'ABR contient 10 : " + abr.contains(10));
        System.out.println("L'ABR contient 15 : " + abr.contains(15));
        System.out.println("Taille de l'ABR : " + abr.size());

        abr.remove(10);
        System.out.println("\n******** Suppression *********");
        System.out.println("L'ABR contient 10 après suppression : " + abr.contains(10));
        System.out.println("Taille de l'ABR après suppression : " + abr.size());

        abr.clear();
        System.out.println("\n********* Nettoyage **********");
        System.out.println("Taille de l'ABR après nettoyage : " + abr.size());
        System.out.println("*******************************\n");
    }
}
