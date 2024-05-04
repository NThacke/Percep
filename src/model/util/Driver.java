package model.util;

import model.faces.FaceDriver;
import model.digits.DigitsDriver;

public class Driver implements Comparable<Driver> {
    
    public static final int FACES = 100;
    public static final int DIGITS = 101;

    private FaceDriver faceDriver;

    private DigitsDriver digitsDriver;

    private int type;

    public Driver(int n, int a, int b, double d, int type) {
        if(type == FACES) {
            faceDriver = new FaceDriver(n, a, b, d);
        }
        else if(type == DIGITS) {
            digitsDriver = new DigitsDriver(n, a, b, d);
        }
        else {
            throw new IllegalArgumentException("Incorrect type given. Must be Driver.FACES or Driver.DIGITS");
        }
        this.type = type;
    }

    public double[][] stats(double x) {
        if(type == FACES) {
            return faceDriver.stats(x);
        }
        else {
            return digitsDriver.stats(x);
        }
    }

    public void incorrect() {
        digitsDriver.incorrect_digits();
    }

    public void train() {
        if(type == FACES) {
            faceDriver.train();
        }
        else {
            digitsDriver.train();
        }
    }

    public void test() {
        if(type == FACES) {
            faceDriver.test();
        }
        else {
            digitsDriver.test();
        }
    }

    public void validate() {
        if(type == FACES) {
            faceDriver.validation();
        }
        else {
            digitsDriver.validation();
        }
    }

    public double acc() {
        if(type == FACES) {
            return faceDriver.acc();
        }
        else {
            return digitsDriver.acc();
        }
    }

    public void output() {
        if(type == FACES) {
            faceDriver.output();
        }
        else {
            digitsDriver.output();
        }
    }

    public int compareTo(Driver other) {
        if(type != other.type) {
            throw new IllegalArgumentException("Cannot compare FaceDriver to DigitDriver");
        }
        if(type == FACES) {
            return this.faceDriver.compareTo(other.faceDriver);
        }
        else {
            return this.digitsDriver.compareTo(other.digitsDriver);
        }
    }

    public String toString() {
        if(type == FACES) {
            return faceDriver.toString();
        }
        else {
            return digitsDriver.toString();
        }
    }

    


}
