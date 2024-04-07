import model.faces.*;

public class Main {
    public static void main(String[] args) {

        double best = 0.0;
        double t = 0.0;

        for(int i = 1; i<=10; i++) {
            double threshold = i/10.0;
            Driver d = new Driver(threshold);
            // d.train();
            d.validation();
            if(d.acc() > best) {
                best =  d.acc();
                t = threshold;
            }
        }

        System.out.println("The best accuracy is " + best + " at threshold " + t);
    }
}