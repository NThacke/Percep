package model.digits;

import java.util.*;
import java.io.*;
import model.util.Util;

public class Perceptron {
    
    double[] weights;

    private static final double alpha = 0.001;

    public Perceptron() {
        weights = new double[785];
    }

    public double f(Image image) {
        double sum = weights[0];
        int[] phi = image.phi();
        for(int i = 0; i < phi.length; i++) {
            sum += weights[i+1]*phi[i];
        }
        return Util.sigmoid(sum);
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

    /**
     * Saves weights to the specified file location.
     * @param filename
     */

    public void save(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for(int i = 0; i < weights.length; i++) {
                writer.write(String.valueOf(weights[i]) + "\n");
            }
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads weights from the specified file location.
     * @param filename
     */
    public void load(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            for(int i = 0; i< weights.length; i++) {
                weights[i] = scanner.nextDouble();
            }
            scanner.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
