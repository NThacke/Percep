package model.faces;

import java.io.*;

public class Image {

    char[][] image;
    
    private int[] phi;

    private int id;

    public Image(RandomAccessFile file) {
        image = new char[70][60];
        init(file);
        calc_phi();
    }

    private void init(RandomAccessFile file) {
        try {
            file.readLine();
            for(int i = 0; i < image.length; i++) {
                for(int j = 0; j < image[i].length; j++) {
                    int character = file.read();
                    if(character != -1) {
                        image[i][j] = (char)(character);
                    }
                }
            }
            file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] phi() {
        return phi;
    }

    private void calc_phi() {
        this.phi = phi(168, 5, 5);
    }

    private int[] phi(int n, int a, int b) throws IllegalArgumentException {

        if(n * a * b != 60 * 70) {
            throw new IllegalArgumentException("n*a*b must equal 4200");
        }
        if(70%a != 0) {
            throw new IllegalArgumentException("a must divide 70");
        }
        if(60%b != 0) {
            throw  new IllegalArgumentException("b must divide 60");
        }
        int[] arr = new int[n];

        //We need to split the image into *n* regions, each of dimension a*b

        //If we are on position (i,j), then we know that our region can be located at

        int cnt = 0;
        for (int i = 0; i <= image.length - a; i += a) {
            for (int j = 0; j <= image[i].length - b; j += b) {
                arr[cnt] = count(i, j, a, b);
                cnt++;
            }
        }
        return arr;
    }

    private int count(int startRow, int startCol, int a, int b) {
        int count = 0;
        for (int i = startRow; i < startRow + a; i++) {
            for (int j = startCol; j < startCol + b; j++) {
                if (image[i][j] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
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
