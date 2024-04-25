
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.util.Driver;;

public class Main {
    

    private static final int[][] face_dimensions = {{42, 10, 10}, {168, 5, 5}, {1050, 2, 2}, {4200, 1, 1}};


    private static final int[][] digit_dimensions = {{16,7,7}, {49,4,4}, {196,2,2}, {784, 1, 1}};

    public static void main(String[] args) {
        trainDigits();
        // trainFaces();
    }

    private static void trainDigits() {
        List<Driver> drivers = new ArrayList<>();

        for(int i = 0; i < digit_dimensions.length; i++) {
            int n = digit_dimensions[i][0];
            int a = digit_dimensions[i][1];
            int b = a;
            for(int j = 1; j <= 10; j++) {
                Driver d = new Driver(n, a, b, (j/10.0), Driver.DIGITS);
                drivers.add(d);
                d.train();
                d.validate();
                d.output();
            }
        }
        Collections.sort(drivers);
        for(Driver d : drivers) {
            System.out.println(d);
        }
        System.out.println("The best accuracy is " + drivers.get(drivers.size()-1));
    }

    private static void trainFaces() {
        List<Driver> drivers = new ArrayList<>();
        for(int i = 0; i < face_dimensions.length; i++) {
            int n = face_dimensions[i][0];
            int a = face_dimensions[i][1];
            int b = a;
            for(int j = 1; j <= 10; j++) {
                Driver d = new Driver(n, a, b, (j/10.0), Driver.FACES);
                drivers.add(d);
                d.train();
                d.validate();
                d.output();
            }
        }
        Collections.sort(drivers);
        for(Driver d : drivers) {
            System.out.println(d);
        }
        System.out.println("The best accuracy is " + drivers.get(drivers.size()-1));
    }

    
}