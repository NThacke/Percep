package model.faces;

import java.util.*;
import java.io.*;

import model.util.Util;

public class FaceDriver implements Comparable<FaceDriver> {
    
    List<Image> images;

    List<Image> trainingImages;

    Perceptron p;

    private double threshold;

    private int TRAINING_COUNT = 1000;

    private int[] labels;

    private long time;

    private double acc;

    private int n;
    private int a;
    private int b;

    public FaceDriver(int n, int a, int b, double threshold) {
        this.threshold = threshold;
        images = new ArrayList<>();
        p = new Perceptron(n);
        this.a = a;
        this.b = b;
        this.n = n;
    }

    public double[][] stats(double x) {
        p.load(Util.FACE_WEIGHTS_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_a:" + a + ".txt");
        System.out.println(this);
        int sample_size = 5;
        double[] prediction_errors = new double[sample_size];
        for(int i = 0; i < sample_size; i++) {
            loadImages(Util.FACE_TRAINING_DATA);
            loadLabels(Util.FACE_TRAINING_LABELS);
            getTrainingSet();

            double incorrect = 0.0;
            for(Image image : trainingImages) {
                double answer = p.f(image);
                if( answer >= 0 && labels[image.getID()] == 0) {
                    incorrect++;
                }
                if( answer < 0 && labels[image.getID()] == 1) {
                    incorrect++;
                }
            }
            prediction_errors[i] = (incorrect) / ((double)(trainingImages.size()));
        }
        double[][] arr = new double[2][sample_size];
        arr[1] = prediction_errors;
        arr[0][0] = Util.mean(prediction_errors);
        arr[0][1] = Util.stddv(prediction_errors);
        return arr;
    }

    public void train() {
        int cnt = 0;
        loadImages(Util.FACE_TRAINING_DATA);
        loadLabels(Util.FACE_TRAINING_LABELS);
        getTrainingSet();
        long start = System.currentTimeMillis();
        while(cnt < TRAINING_COUNT) {
            if(cnt % 10000 == 0) {
                System.out.println(cnt);
            }
            for(int i = 0; i < trainingImages.size(); i++) {
                Image image = trainingImages.get(i);
                double f = p.f(image);
                if(f >= 0 && labels[image.getID()] == 0) { 
                    p.decreaseWeights(image);
                }
                if(f < 0 && labels[image.getID()] == 1) {
                    p.increaseWeights(image);
                }
            }
            cnt++;
        }
        this.time = System.currentTimeMillis() - start;
        p.save(Util.FACE_WEIGHTS_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_a:" + a + ".txt");
    }

    public void output() {
        try {
            FileWriter writer = new FileWriter(Util.FACES_OUTPUT_TRAINING_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_output.txt");
            writer.write("Training Output File for Pereptron Model " + this.toString());
            writer.write("\n\n");
            writer.write("N:" + n + "\nA:" + a + "\nB:" + b + "\nTraining Threshold : " + threshold);
            writer.write("\n\n");
            writer.write("Trained " + TRAINING_COUNT + " epochs\n");
            writer.write("Training Time : " + time + " ms\n");
            writer.write("Training Time : " + Util.millisecondsToHMS(time) + " HH:MM:SS\n");
            String formattedAcc = String.format("%.2f", acc * 100);
            writer.write("\nValidation Accuracy : " + formattedAcc + "%");
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void validation() {
        loadImages(Util.FACE_VALIDATION_DATA);
        loadLabels(Util.FACE_VALIDATION_LABELS);
        int correct = 0;
        p.load(Util.FACE_WEIGHTS_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_a:" + a + ".txt");
        for(int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            int answer = -1;
            double f = p.f(image);
            if(f >= 0) {
                answer = 1;
            }
            else {
                answer = 0;
            }
            if(answer == labels[image.getID()]) {
                correct ++;
            }
            System.out.println(image);
            System.out.println("Model : " + answer + " Label : " + labels[image.getID()]);
        }
        double acc = (double)(correct)/(double)(images.size());
        this.acc = acc;
        System.out.println("Model was correct " + correct + " out of " + images.size() + " for an accuracy of " + acc);
    }

    public void test() {
        loadImages(Util.FACE_TEST_DATA);
        loadLabels(Util.FACE_TEST_LABELS);
        int correct = 0;
        p.load(Util.FACE_WEIGHTS_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_a:" + a + ".txt");
        for(int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            int answer = -1;
            double f = p.f(image);
            if(f >= 0) {
                answer = 1;
            }
            else {
                answer = 0;
            }
            if(answer == labels[image.getID()]) {
                correct ++;
            }
            System.out.println(image);
            System.out.println("Model : " + answer + " Label : " + labels[image.getID()]);
        }
        double acc = (double)(correct)/(double)(images.size());
        this.acc = acc;
        System.out.println("Model was correct " + correct + " out of " + images.size() + " for an accuracy of " + acc);
    }

    public double acc() {
        return acc;
    }

    private void loadImages(String filename) {
        try {
            this.images = new ArrayList<>();
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            int id = 0;
            while(file.getFilePointer() < file.length()) {
                Image image = new Image(n, a, b, file);
                image.setID(id);
                this.images.add(image);
                id++;
            }
            file.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLabels(String filename) {
        try {
            this.labels = new int[images.size()];
            Scanner scanner = new Scanner(new File(filename));
            int i = 0;
            while(scanner.hasNext()) {
                this.labels[i] = scanner.nextInt();
                i++;
            }
            scanner.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getTrainingSet() {
        trainingImages = new ArrayList<>();
        if(threshold != 1.0) { //issues may arise if the threshold is the entire set and we randomly select from (size * threshold) [floating point fun]
            int n = (int)(images.size() * threshold);
            for(int i = 0; i < n; i++) {
                int r = Util.random.nextInt(images.size());
                trainingImages.add(images.remove(r));
            }
        }
        else {
            while(images.size() > 0) {
                int r = Util.random.nextInt(images.size());
                trainingImages.add(images.remove(r));
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Faces ");
        s.append("N : " + n);
        s.append("A : " + a);
        s.append("B : " + b);
        s.append("Threshold : " + threshold);
        s.append("Accuracy : " + acc);
        return s.toString();
    }

    public int compareTo(FaceDriver other) {
        return (int)((10000000)*(this.acc - other.acc));
    }
}
