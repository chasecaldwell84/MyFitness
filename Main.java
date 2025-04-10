package MyFitness;

import MyFitness.LoginSignUp.LandingPage;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        LandingPage lp = new LandingPage(app);
        lp.setVisible(true);
        /*NOTE: Thinking about trainer problem
        *  We could have it where you can sign up to be a trainer on the landing page
        *  Or we could have it where you login as a user and inside of the app there is an option to become a trainer
        */


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
