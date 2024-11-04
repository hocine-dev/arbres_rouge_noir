public class Main {
    public static void main(String[] args) {
        // Instanciation de l'ABR
        ABR<Integer> abr = new ABR<>();
        abr.add(10);
        abr.add(12);
        abr.add(14);
        abr.add(14);
        abr.add(0);
        
        
        // Instanciation de l'ARN
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(9);
        arn.add(120);
        arn.add(120);
        arn.add(9);
        
       
    }
}
