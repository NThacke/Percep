package model.classes;

public abstract class AbstractImage {
    
    protected char[][] image;

    protected int[] phi;

    protected int id;

    protected int n;
    
    protected int a;

    protected int b;

    public int[] phi() {
        return phi;
    }

    protected void calc_phi(int n, int a, int b) {
        if(validDimensions(n, a, b)) {
            this.phi = phi(n, a, b);
        }
    }

    private int[] phi(int n, int a, int b) {
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

    /**
     * To be implemented by the extending class.
     * 
     * 
     * Length := Number of Rows in Image
     * Width :=  Number of Columns in Image
     * 
     * Length * Width = Number of Pixels in Image
     * 
     * Dimensions are valid if and only if the following three conditions are met :
     * 
     * 1.) n * a * b = Length * Width
     * 
     * 2.) Length % a = 0 (a divides the length)
     * 
     * 3.) Width % b = 0  (b divides the width)
     *     
     * @param n the number of cells to split the image into
     * @param a the length of each rectangular shaped region
     * @param b the width of each rectangular shaped region
     */
    abstract protected boolean validDimensions(int n, int a, int b);

    private int count(int startRow, int startCol, int a, int b) {
        int count = 0;
        for (int i = startRow; i < startRow + a; i++) {
            for (int j = startCol; j < startCol + b; j++) {
                if (image[i][j] == '#' || image[i][j] == '+') {
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
