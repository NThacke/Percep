package model.faces;

import java.util.*;
import java.io.*;

import model.util.Util;

public class Driver {
    
    List<Image> images;

    List<Image> trainingImages;

    Perceptron p;

    private double threshold;

    private int TRAINING_COUNT = 10000;

    private int[] labels;

    private long time;

    private double acc;

    public Driver(double threshold) {
        this.threshold = threshold;
        images = new ArrayList<>();
        p = new Perceptron();
    }

    public void train() {
        int cnt = 0;
        loadImages(Util.FACE_TRAINING_DATA);
        loadLabels(Util.FACE_TRAINING_LABELS);
        getTrainingSet();
        long start = System.currentTimeMillis();
        while(cnt < TRAINING_COUNT) {
            if(cnt % 1000 == 0) {
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
        p.save(Util.FACE_WEIGHTS_DIRECTORY + threshold + ".txt");
        training_output();
    }

    private void training_output() {
        try {
            FileWriter writer = new FileWriter(Util.FACES_OUTPUT_TRAINING_DIRECTORY + threshold + "output.txt");
            writer.write("Trainging Output for Faces at " + threshold + " Training Capacity\n\n");
            writer.write("Training Time : " + Util.hhmmss(time) + "\n");
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
        p.load(Util.FACE_WEIGHTS_DIRECTORY + threshold + ".txt");
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
}
