package MyFitness;

public class AppConfig {
    public static final int WIDTH = 1179; //suppose iphone 16 screen
    public static final int HEIGHT = 2556;
    public static final String LOGO_PATH = "resources/images/MyFitnessLogoChose1.jpg";
    public static final int LOHO_WIDTH = WIDTH / 5;
    public static final int LOGO_HEIGHT = WIDTH / 5;

    public static void printAppInfo() {//for debug
        System.out.println("App running on iPhone 16 screen size: " + WIDTH + "x" + HEIGHT);
        System.out.println("Logo location: " + LOGO_PATH);
        System.out.println("Logo display size: " + LOGO_WIDTH + "x" + LOGO_HEIGHT);
    }
}

