package model.digits;

import java.io.*;

import model.util.Util;

public class Image {
    
    private char[][] image;

    private int[] phi;

    private int id;

    public Image(RandomAccessFile file) {
        image = new char[Util.DIGIT_IMAGE_LENGTH][Util.DIGIT_IMAGE_WIDTH];
        try {
            for(int i = 0; i < image.length; i++) {
                for(int j = 0; j < image.length; j++) {
                    int c = file.read();
                    if(c != -1) {
                        image[i][j] = (char)(c);
                    }
                }
            }
            file.readLine();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        calc_phi();
    }

    /**
     * The feature fector associated with this image. This feature vector segments the image pixel by pixel, assigning each pixel a value to the feature vector.
     * @return the feature vector of this image
     */
    public int[] phi() {
        return phi;
    }

    private void calc_phi() {
        int[] arr = new int[28*28];
        int c = 0;
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                if(image[i][j] == '#') {
                    arr[c] = 1;
                }
                if(image[i][j] == '+') {
                    arr[c] = 2;
                }
                c++;
            }
        }
        this.phi = arr;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                s.append(image[i][j]);
            }
        }
        return s.toString();
    }
}
