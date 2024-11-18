import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.IOException;

public class EtudeExperimentale {
    public static void main(String[] args) {
        // Création d'un fichier texte pour exporter les résultats
        try (PrintWriter writer = new PrintWriter("resultats.txt")) {
            // Exécution des tests avec différentes tailles de données
            runTest(1_000_000, writer);
            runTest(10_000_000, writer);
            runTest(100_000_000, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runTest(int size, PrintWriter writer) {
        // Affichage et écriture de la taille du test en cours
        System.out.println("Exécution du test avec " + size + " valeurs");
        writer.println("Exécution du test avec " + size + " valeurs");

        // Initialisation des arbres ARN et ABR
        ARN<Integer> arn = new ARN<>();
        ABR<Integer> abr = new ABR<>();

        // Génération de valeurs aléatoires
        ArrayList<Integer> valeursAleatoires = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            valeursAleatoires.add(rand.nextInt(100));
        }

        // Cas moyen : Ajout des valeurs en ordre aléatoire
        Collections.shuffle(valeursAleatoires);
        long startTime = System.nanoTime();
        for (Integer valeur : valeursAleatoires) {
            arn.add(valeur);
        }
        long endTime = System.nanoTime();
        double arnTempsAleatoire = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Temps d'ajout pour ARN (ordre aléatoire) : " + arnTempsAleatoire + " s");
        writer.println("Temps d'ajout pour ARN (ordre aléatoire) : " + arnTempsAleatoire + " s");

        startTime = System.nanoTime();
        for (Integer valeur : valeursAleatoires) {
            abr.add(valeur);
        }
        endTime = System.nanoTime();
        double abrTempsAleatoire = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Temps d'ajout pour ABR (ordre aléatoire) : " + abrTempsAleatoire + " s");
        writer.println("Temps d'ajout pour ABR (ordre aléatoire) : " + abrTempsAleatoire + " s");

        // Cas le plus défavorable pour ABR : Ajout des valeurs en ordre croissant
        ArrayList<Integer> valeursCroissantes = new ArrayList<>(valeursAleatoires);
        Collections.sort(valeursCroissantes);

        // Réinitialisation des arbres avant le test suivant
        arn.clear();
        abr.clear();

        // Ajout des valeurs en ordre croissant dans ARN
        startTime = System.nanoTime();
        for (Integer valeur : valeursCroissantes) {
            arn.add(valeur);
        }
        endTime = System.nanoTime();
        double arnTempsCroissant = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Temps d'ajout pour ARN (ordre croissant) : " + arnTempsCroissant + " s");
        writer.println("Temps d'ajout pour ARN (ordre croissant) : " + arnTempsCroissant + " s");

        // Ajout des valeurs en ordre croissant dans ABR
        startTime = System.nanoTime();
        for (Integer valeur : valeursCroissantes) {
            abr.add(valeur);
        }
        endTime = System.nanoTime();
        double abrTempsCroissant = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Temps d'ajout pour ABR (ordre croissant) : " + abrTempsCroissant + " s");
        writer.println("Temps d'ajout pour ABR (ordre croissant) : " + abrTempsCroissant + " s");

        // Mesure du temps de recherche des clés 0,...,2n-1
        int n = size;
        long arnSearchTime = 0;
        long abrSearchTime = 0;

        // Recherche des clés dans ARN
        startTime = System.nanoTime();
        for (int i = 0; i < 2 * n; i++) {
            arn.contains(i);
        }
        endTime = System.nanoTime();
        arnSearchTime = endTime - startTime;
        System.out.println("Temps de recherche pour ARN : " + arnSearchTime / 1_000_000_000.0 + " s");
        writer.println("Temps de recherche pour ARN : " + arnSearchTime / 1_000_000_000.0 + " s");

        // Recherche des clés dans ABR
        startTime = System.nanoTime();
        for (int i = 0; i < 2 * n; i++) {
            abr.contains(i);
        }
        endTime = System.nanoTime();
        abrSearchTime = endTime - startTime;
        System.out.println("Temps de recherche pour ABR : " + abrSearchTime / 1_000_000_000.0 + " s");
        writer.println("Temps de recherche pour ABR : " + abrSearchTime / 1_000_000_000.0 + " s");

        // Ligne vide pour séparer les résultats des différents tests
        System.out.println();
        writer.println();
    }
}