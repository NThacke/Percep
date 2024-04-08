// import model.faces.*;
import model.digits.*;

public class Main {
    

    // private static final Driver best_face = new Driver(168, 5, 5, 0.5); //91.3 % accuracy

    // private static final Driver best_digit = new Driver(0.8); //82 % accuracy

    // private static final int[][] face_dimensions = {{42, 10, 10}, {168, 5, 5}, {1050, 2, 2}, {4200, 1, 1}};


    private static final int[][] digit_dimensions = {{16,7,7}, {49,4,4}, {196,2,2}, {784, 1, 1}};

    public static void main(String[] args) {
        train_digits();
        // digits();
        // incorrect_digits();
        // digit_demo();
        // face_demo();
    
    }

    // private static void face_demo() {
    //     best_face.test();
    // }

    // private static void digit_demo() {
    //     Driver driver = new Driver(0.8);
    //     driver.demo();
    // }

    // private static void incorrect_digits() {
    //     Driver driver = new Driver(0.8);
    //     driver.incorrect_digits();
    // }

    private static void digits() {
        double best = 0.0;
        Driver bestDriver = null;
        for(int j = 0; j < digit_dimensions.length; j++) {
            int n = digit_dimensions[j][0];
            int a = digit_dimensions[j][1];
            int b = digit_dimensions[j][2];
        
            for(int i = 1; i<= 10;i++) {
                double d = i / (10.0);
                Driver driver = new Driver(n, a, b, d);
                // driver.validation();
                driver.test();
                double acc = driver.acc;
                if(acc > best) {
                    best = acc;
                    bestDriver = driver;
                }
            }
        }
        System.out.println("Best accuracy is " + best + " w/ model " + bestDriver.toString());
    }

    private static void train_digits() {
        for(int i = 1; i<= 10;i++) {
            double d = i / (10.0);
            for(int j = 0; j < digit_dimensions.length; j++) {
                int n = digit_dimensions[j][0];
                int a = digit_dimensions[j][1];
                int b = digit_dimensions[j][2];
                System.out.println(n);
                System.out.println(a);
                System.out.println(b);
                Driver driver = new Driver(n, a, b, d);
                driver.train();
            }
        }
    }

    // private static void faces() {
    //     double best = 0.0;
    //     Driver best_driver = null;

    //     for(int i = 0; i <face_dimensions.length; i++) {
    //         int n = face_dimensions[i][0];
    //         int a = face_dimensions[i][1];
    //         int b = a;
    //         for(int j = 1; j<= 10; j++) {
    //             double threshold = j / 10.0;
    //             Driver d = new Driver(n, a, b, threshold);
    //             d.test();
    //             if(d.acc() > best) {
    //                 best = d.acc();
    //                 best_driver = d;
    //             }
    //         }
    //     }
    //     System.out.println(best_driver.toString());
    //     System.out.println(best);
    // }
}