package model.digits;

import java.util.*;

import model.util.Util;

import java.io.*;

public class DigitsDriver implements Comparable<DigitsDriver> {
    
    private Perceptron[] perceptrons;

    private int[] labels;

    private static final int TRAINING_COUNT = 1000;

    private List<Image> images;

    private List<Image> trainingimages;

    private double acc;

    long time;

    /**
     * The amount of data to train on. {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}
     */
    private double threshold;

    private int n;

    private int a;

    private int b;

    public DigitsDriver(int n, int a, int b, double threshold) {
        this.n = n;
        this.a = a;
        this.b = b;
        perceptrons = new Perceptron[10];
        for(int i = 0; i < perceptrons.length; i++) {
            perceptrons[i] = new Perceptron(n);
        }
        images = new ArrayList<>();
        this.threshold = threshold;
    }

    public double[][] stats(double x) {
        load();
        System.out.println(this);
        int sample_size = 5;
        double[] prediction_errors = new double[sample_size];
        for(int i = 0; i < sample_size; i++) {
            loadImages(Util.DIGIT_TRAINING_DATA);
            loadLabels(Util.DIGIT_TRAINING_LABELS);
            getTrainingSet();

            double incorrect = 0.0;
            for(Image image : trainingimages) {
                double max_f = -1.0;
                int answer = -1;
                for(int j = 0; j < perceptrons.length; j++) {
                    Perceptron p = perceptrons[j];
                    double f = p.f(image);
                    if(f > max_f) {
                        max_f = f;
                        answer = j;
                    }
                }
                if(answer != labels[image.getID()]) {
                    incorrect++;
                }
            }
            prediction_errors[i] = (incorrect) / ((double)(trainingimages.size()));
        }
        double[][] arr = new double[2][sample_size];
        arr[1] = prediction_errors;
        arr[0][0] = Util.mean(prediction_errors);
        arr[0][1] = Util.stddv(prediction_errors);
        return arr;
    }

    public void incorrect_digits() {
        loadImages(Util.DIGIT_TEST_DATA);
        loadLabels(Util.DIGIT_TEST_LABELS);
        load();
        for(int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            double max_f = -1.0;
            int answer = -1;
            for(int j = 0; j < perceptrons.length; j++) {
                Perceptron p = perceptrons[j];
                double f = p.f(image);
                if(f > max_f) {
                    max_f = f;
                    answer = j;
                }
            }
            if(answer != labels[image.getID()]) {
                System.out.println(image);
                System.out.println("Model : " + answer + " Label : " + labels[image.getID()]);                
            }
        }
    }
    public void demo() {
        loadImages(Util.DIGIT_TEST_DATA);
        loadLabels(Util.DIGIT_TEST_LABELS);
        load();
        for(int i = 0; i < 20; i++) {
            Image image = images.remove(Util.random.nextInt(images.size()));
            int answer = -1;
            double max = -1.0;
            for(int j = 0; j< perceptrons.length; j++) {
                double f = perceptrons[j].f(image);
                if(f > max) {
                    max = f;
                    answer = j;
                }
            }
            System.out.println(image);
            System.out.print("Model's answer : '" + answer + "' True Label : '" + labels[image.getID()] + "'");
        }
    }

    public void validation() {
        loadImages(Util.DIGIT_VALIDATION_DATA);
        loadLabels(Util.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        load();
        for(int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            double max_f = -1.0;
            int answer = -1;
            for(int j = 0; j < perceptrons.length; j++) {
                Perceptron p = perceptrons[j];
                double f = p.f(image);
                if(f > max_f) {
                    max_f = f;
                    answer = j;
                }
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
        loadImages(Util.DIGIT_TEST_DATA);
        loadLabels(Util.DIGIT_TEST_LABELS);
        int correct = 0;
        load();
        for(int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            double max_f = -1.0;
            int answer = -1;
            for(int j = 0; j < perceptrons.length; j++) {
                Perceptron p = perceptrons[j];
                double f = p.f(image);
                if(f > max_f) {
                    max_f = f;
                    answer = j;
                }
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

    public void train() {
        int cnt = 0;
        loadImages(Util.DIGIT_TRAINING_DATA);
        loadLabels(Util.DIGIT_TRAINING_LABELS);
        getTrainingSet();
        long start = System.currentTimeMillis();
        while(cnt < TRAINING_COUNT) {
            if( cnt % 10  == 0) {
                System.out.println("Training : " + cnt);
            }
            for(int i = 0; i < trainingimages.size(); i++) {
                Image image = trainingimages.get(i);
                
                double max_f = -1.0;
                int chosen_value = -1;

                for(int j = 0; j < perceptrons.length; j++) {
                    Perceptron p = perceptrons[j];
                    double f = p.f(image);
                    if(f > max_f) {
                        max_f = f;
                        chosen_value = j;
                    }
                }

                if(chosen_value != labels[image.getID()]) { //perceptron was wrong, increase weights for the correct answer (perceptrons[labels[image.id]]) and decrease weights for all others
                    perceptrons[labels[image.getID()]].increaseWeights(image);
                    perceptrons[chosen_value].decreaseWeights(image);
                }
            }
            cnt++;
        }
        this.time = System.currentTimeMillis() - start;
        save();
    }

    public void output() {
        try {
            FileWriter writer = new FileWriter(Util.DIGIT_OUTPUT_TRAINING_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_output.txt");
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

    private void save() {
        perceptrons[0].save(Util.DIGIT_0_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "zero.txt");
        perceptrons[1].save(Util.DIGIT_1_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "one.txt");
        perceptrons[2].save(Util.DIGIT_2_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "two.txt");
        perceptrons[3].save(Util.DIGIT_3_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "three.txt");
        perceptrons[4].save(Util.DIGIT_4_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "four.txt");
        perceptrons[5].save(Util.DIGIT_5_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "five.txt");
        perceptrons[6].save(Util.DIGIT_6_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" +  "six.txt");
        perceptrons[7].save(Util.DIGIT_7_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "seven.txt");
        perceptrons[8].save(Util.DIGIT_8_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "eight.txt");
        perceptrons[9].save(Util.DIGIT_9_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "nine.txt");
    }
    
    private void load() {
        perceptrons[0].load(Util.DIGIT_0_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "zero.txt");
        perceptrons[1].load(Util.DIGIT_1_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "one.txt");
        perceptrons[2].load(Util.DIGIT_2_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "two.txt");
        perceptrons[3].load(Util.DIGIT_3_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "three.txt");
        perceptrons[4].load(Util.DIGIT_4_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "four.txt");
        perceptrons[5].load(Util.DIGIT_5_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "five.txt");
        perceptrons[6].load(Util.DIGIT_6_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "six.txt");
        perceptrons[7].load(Util.DIGIT_7_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "seven.txt");
        perceptrons[8].load(Util.DIGIT_8_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "eight.txt");
        perceptrons[9].load(Util.DIGIT_9_DIRECTORY + threshold + "_n:" + n + "_a:" + a + "_b:" + b + "_" + "nine.txt");
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
                // images.get(i).setLabel(this.labels[i]);
                i++;
            }
            scanner.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getTrainingSet() {
        trainingimages = new ArrayList<>();
        if(threshold != 1.0) { //issues may arise if the threshold is the entire set and we randomly select from (size * threshold) [floating point fun]
            int n = (int)(images.size() * threshold);
            for(int i = 0; i < n; i++) {
                int r = Util.random.nextInt(images.size());
                trainingimages.add(images.remove(r));
            }
        }
        else {
            while(images.size() > 0) {
                int r = Util.random.nextInt(images.size());
                trainingimages.add(images.remove(r));
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Digits ");
        s.append("N : " + n);
        s.append(" A : " + a);
        s.append(" B : " + b);
        s.append(" D : " + threshold);
        s.append("Accuracy : " + acc);
        return s.toString();

    }

    public double acc() {
        return acc;
    }

    public int compareTo(DigitsDriver other) {
        return (int)(10000*(this.acc - other.acc));
    }
}
