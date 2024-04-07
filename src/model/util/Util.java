package model.util;

import java.util.*;

public class Util {

    public static final int DIGIT_IMAGE_LENGTH = 28;
    
    public static final int DIGIT_IMAGE_WIDTH = 28;


    public static final String DIGIT_0_DIRECTORY = "src/data/weights/digits/0/";
    public static final String DIGIT_1_DIRECTORY = "src/data/weights/digits/1/";
    public static final String DIGIT_2_DIRECTORY = "src/data/weights/digits/2/";
    public static final String DIGIT_3_DIRECTORY = "src/data/weights/digits/3/";
    public static final String DIGIT_4_DIRECTORY = "src/data/weights/digits/4/";
    public static final String DIGIT_5_DIRECTORY = "src/data/weights/digits/5/";
    public static final String DIGIT_6_DIRECTORY = "src/data/weights/digits/6/";
    public static final String DIGIT_7_DIRECTORY = "src/data/weights/digits/7/";
    public static final String DIGIT_8_DIRECTORY = "src/data/weights/digits/8/";
    public static final String DIGIT_9_DIRECTORY = "src/data/weights/digits/9/";

    public static final String DIGIT_OUTPUT_TRAINING_DIRECTORY = "src/data/output/digits/training/";
    public static final String DIGIT_OUTPUT_DEMO_DIRECTORY = "src/data/output/digits/demo/";

    public static final String FACES_OUTPUT_TRAINING_DIRECTORY = "src/data/output/faces/training/";
    public static final String FACES_OUTPUT_DEMO_DIRECTORY = "src/data/output/faces/demo/";

    public static final String DIGIT_TRAINING_DATA = "src/data/digitdata/trainingimages";
    public static final String DIGIT_TEST_DATA = "src/data/digitdata/testimages";
    public static final String DIGIT_VALIDATION_DATA = "src/data/digitdata/validationimages";
    
    public static final String DIGIT_TRAINING_LABELS = "src/data/digitdata/traininglabels";
    public static final String DIGIT_TEST_LABELS = "src/data/digitdata/testlabels";
    public static final String DIGIT_VALIDATION_LABELS = "src/data/digitdata/validationlabels";


    public static final long seed = 1;
    public static final Random random = new Random(seed);

    
    public static double sigmoid(double x) {
        double e = Math.exp(-x);
        return 1/(1+e);
    }
}
