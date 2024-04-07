import model.digits.Driver;

public class Main {
    public static void main(String[] args) {

        for(int i = 2; i <= 10; i++) {
            double threshold = (i)/(10.0);
            System.out.println(threshold);
            Driver driver = new Driver(threshold);
            driver.train();
            // driver.validation();
        }
    }
}