package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.NavBar;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlansPage extends JPanel {
    private Database db = Database.getInstance();

    public PlansPage(App frame, NavBar navBar, User user) {
        frame.setTitle("Plans");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Plans Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridy = 0;
        add(titleLabel, c);

        if(frame.getUser() instanceof Trainer) {
            Trainer trainer = (Trainer) frame.getUser();
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.WEST;

            JButton createPlanButton = new JButton("Create Plan");
            createPlanButton.addActionListener(e -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(navBar);
                frame.add(new TrainerPage(frame, navBar, trainer));
                frame.revalidate();
                frame.repaint();
            });
            c.gridy = 1;
            add(createPlanButton, c);

            JButton modifyPlanButton = new JButton("Modify Plan");
            modifyPlanButton.addActionListener(e -> {
                List<String> planNames = db.getTrainerPlanNames(trainer.getUserName());

                if(planNames.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "No plans found");
                    return;
                }

                String selectedPlan = (String) JOptionPane.showInputDialog(
                        frame,
                        "Select a plan to modify:",
                        "Modify Plan",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        planNames.toArray(),
                        planNames.get(0)
                );

                if(selectedPlan != null){
                    JPanel moddifyPanel = new ModifyPlanPanel(frame, navBar, trainer, selectedPlan);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(navBar);
                    frame.add(moddifyPanel);
                    frame.revalidate();
                    frame.repaint();
                }
            });

            c.gridy = 2;
            add(modifyPlanButton, c);
        }
    }
}
