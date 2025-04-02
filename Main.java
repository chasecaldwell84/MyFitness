package MyFitness;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        LandingPage lp = new LandingPage(app);
        lp.setVisible(true);

        //NOTE testing
        /*Scanner sc = new Scanner(System.in);
        String input;
        input = sc.nextLine();
        if(input.equalsIgnoreCase("exit")) {
            app.setSize(1000, 800);
        }*/
    }
}
