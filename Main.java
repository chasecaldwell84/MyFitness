package MyFitness;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        LandingPage lp = new LandingPage(app);
        lp.setVisible(true);

        //testing CalorieTracker
        //CalorieTracker ct = new CalorieTracker();
        //ct.setVisible(true);

        //testing CreateGoals
        //CreateGoals cg = new CreateGoals();
        //cg.setVisible(true);

        //NOTE testing
        /*App app2 = new App();
        app2.setVisible(true);*/
        /*Scanner sc = new Scanner(System.in);
        String input;
        input = sc.nextLine();
        if(input.equalsIgnoreCase("exit")) {
            app.setSize(1000, 800);
        }*/
    }
}
