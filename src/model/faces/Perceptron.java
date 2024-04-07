package model.faces;

import java.util.*;
import java.io.*;

public class Perceptron {
    
    private double[] weights;

    private static final double alpha = 0.001;

    public Perceptron() {
        weights = new double[169];
    }

    public double f(Image image) {
        double f = weights[0];
        int[] phi = image.phi();
        for(int i = 0; i < phi.length; i++) {
            f += weights[i+1]*phi[i];
        }
        return f;
    }

    public void increaseWeights(Image image) {
        weights[0] += alpha;
        int[] phi = image.phi();
        for(int i = 0; i < phi.length; i++) {
            weights[i+1] += alpha * phi[i];
        }
    }

    public void decreaseWeights(Image image) {
        weights[0] -= alpha;
        int[] phi = image.phi();
        for(int i = 0; i < phi.length; i++) {
            weights[i+1] -= alpha * phi[i];
        }
    }

    public void save(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for(int i = 0; i< weights.length; i++) {
                writer.write(String.valueOf(weights[i]) + "\n");
            }
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();;
        }
    }

    public void load(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            int i = 0;
            while(scanner.hasNext()) {
                weights[i] = scanner.nextDouble();
                i++;
            }
            scanner.close();
        }
        catch(Exception e) {
            e.printStackTrace();;
        }
    }
}
