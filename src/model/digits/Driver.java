package model.digits;

import java.util.*;

import model.util.Util;

import java.io.*;

public class Driver {
    
    private Perceptron[] perceptrons;

    private int[] labels;

    private static final int TRAINING_COUNT = 1000;

    private List<Image> images;

    private List<Image> trainingimages;

    public double acc;

    /**
     * The amount of data to train on. {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}
     */
    private double threshold;

    public Driver(double threshold) {
        perceptrons = new Perceptron[10];
        for(int i = 0; i < perceptrons.length; i++) {
            perceptrons[i] = new Perceptron();
        }
        images = new ArrayList<>();
        this.threshold = threshold;
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
            if( cnt % 100  == 0) {
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
        long time = System.currentTimeMillis() - start;
        save();
    }

    private void save() {
        perceptrons[0].save(Util.DIGIT_0_DIRECTORY + threshold + "zero.txt");
        perceptrons[1].save(Util.DIGIT_1_DIRECTORY + threshold + "one.txt");
        perceptrons[2].save(Util.DIGIT_2_DIRECTORY + threshold + "two.txt");
        perceptrons[3].save(Util.DIGIT_3_DIRECTORY + threshold + "three.txt");
        perceptrons[4].save(Util.DIGIT_4_DIRECTORY + threshold + "four.txt");
        perceptrons[5].save(Util.DIGIT_5_DIRECTORY + threshold + "five.txt");
        perceptrons[6].save(Util.DIGIT_6_DIRECTORY + threshold + "six.txt");
        perceptrons[7].save(Util.DIGIT_7_DIRECTORY + threshold + "seven.txt");
        perceptrons[8].save(Util.DIGIT_8_DIRECTORY + threshold + "eight.txt");
        perceptrons[9].save(Util.DIGIT_9_DIRECTORY + threshold + "nine.txt");
    }
    
    private void load() {
        perceptrons[0].load(Util.DIGIT_0_DIRECTORY + threshold + "zero.txt");
        perceptrons[1].load(Util.DIGIT_1_DIRECTORY + threshold + "one.txt");
        perceptrons[2].load(Util.DIGIT_2_DIRECTORY + threshold + "two.txt");
        perceptrons[3].load(Util.DIGIT_3_DIRECTORY + threshold + "three.txt");
        perceptrons[4].load(Util.DIGIT_4_DIRECTORY + threshold + "four.txt");
        perceptrons[5].load(Util.DIGIT_5_DIRECTORY + threshold + "five.txt");
        perceptrons[6].load(Util.DIGIT_6_DIRECTORY + threshold + "six.txt");
        perceptrons[7].load(Util.DIGIT_7_DIRECTORY + threshold + "seven.txt");
        perceptrons[8].load(Util.DIGIT_8_DIRECTORY + threshold + "eight.txt");
        perceptrons[9].load(Util.DIGIT_9_DIRECTORY + threshold + "nine.txt");
    }
    
    private void loadImages(String filename) {
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            int id = 0;
            while(file.getFilePointer() < file.length()) {
                Image image = new Image(file);
                image.setID(id);
                images.add(image);
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
            labels = new int[images.size()];
            Scanner scanner = new Scanner(new File(filename));
            int i = 0;
            while(scanner.hasNext()) {
                labels[i] = scanner.nextInt();
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
}
