package MyFitness;

import MyFitness.LoginSignUp.LandingPage;
import MyFitness.User.Admin;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        //NOTE Creating admins in main
        db.saveUser(new Admin("Michael", "1234"));
        db.saveUser(new Admin("Jeremy", "1234"));
        db.saveUser(new Admin("Ryan", "1234"));
        db.saveUser(new Admin("LarryAdmin", "321"));

        App app = new App();
        LandingPage lp = new LandingPage(app);
        lp.setVisible(true);
        /*NOTE: Thinking about trainer problem
        *  We could have it where you can sign up to be a trainer on the landing page
        *  Or we could have it where you login as a user and inside of the app there is an option to become a trainer
        */
        //System.out.println("Working directory: " + System.getProperty("user.dir"));
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");

            while (rs.next()) {
                System.out.println("Username: " + rs.getString("USERNAME"));
                System.out.println("Password: " + rs.getString("PASSWORD"));
                System.out.println("Role: " + rs.getString("USERTYPE"));
                System.out.println("----------------------------");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



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
