import model.faces.*;

public class Main {

    private static final int[][] face_dimensions = {{42, 10, 10}, {168, 5, 5}, {1050, 2, 2}, {4200, 1, 1}};
    public static void main(String[] args) {

        double best = 0.0;
        double t = 0.0;

        Driver best_d = null;

        for(int f = 0; f < face_dimensions.length; f++) {
            int n = face_dimensions[f][0];
            int a = face_dimensions[f][1];
            int b = a;
            System.out.println("N : " + n + " A : " + a + " B : " + b);
            for(int i = 1; i<=10; i++) {
                double threshold = i/10.0;
                Driver d = new Driver(n, a, b, threshold);
                // d.train();
                // d.validation();
                d.test();
                if(d.acc() > best) {
                    best =  d.acc();
                    t = threshold;
                    best_d = d;
                }
            }
        }
        System.out.println(best_d.toString());
        System.out.println("The best accuracy is " + best + " at threshold " + t);
    }
}