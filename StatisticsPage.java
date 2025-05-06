package MyFitness;//Author: Larry O'Connor

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class StatisticsPage extends JPanel {

    static double averageSleep = 0;
    static double averageWorkoutLength = 0;
    static double averageCaloriesBurned = 0;

    private static double totalSleep = 0.0;
    private static double totalWorkoutLength = 0.0;
    private static double totalCaloriesBurned = 0.0;

    static JLabel[] statLabels = new JLabel[6];
    static boolean[] selectedStat = new boolean[6];


    private JFrame filterGUI = new JFrame();
    private JPanel infoPanel;

    public StatisticsPage(App frame) {
        Arrays.fill(selectedStat, true);
        frame.setTitle("Statistics Page");
        setLayout(new BorderLayout(50, 50));
        createStatsGUI();
        createFilterGUI();
    }

    //Create Stats UI
    public void createStatsGUI(){
        SwingUtilities.invokeLater(() -> {

            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.Y_AXIS));
            JLabel titleLabel = new JLabel("Statistics",SwingConstants.CENTER);

            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(App.titleFont);

            titlePanel.add(Box.createVerticalStrut(80));
            titlePanel.add(titleLabel);

            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            for(int i = 0; i < statLabels.length; i++){
                statLabels[i] = new JLabel();
            }
            statLabels[0] = new JLabel("Average Sleep: " + averageSleep);
            statLabels[1] = new JLabel("Average Workout: " + averageWorkoutLength);
            statLabels[2] = new JLabel("Average Calories Burned: " + averageCaloriesBurned);

            statLabels[3] = new JLabel("Total Sleep: " + totalSleep);
            statLabels[4] = new JLabel("Total Workout: " + totalWorkoutLength);
            statLabels[5] = new JLabel("Total Calories Burned: " + totalCaloriesBurned);

            for(JLabel statLabel : statLabels) {
                statLabel.setFont(App.boldLabelFontLarge);
                statLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            }


            for(int i = 0; i < statLabels.length; i++){
                if(selectedStat[i]) {
                    infoPanel.add(Box.createVerticalStrut(25));
                    infoPanel.add(statLabels[i]);
                }
            }
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(45, 45, 90, 45),  // Outer empty border for spacing
                    BorderFactory.createLineBorder(Color.BLACK, 5)    // Inner line border
            ));

            add(titlePanel,BorderLayout.NORTH);
            add(infoPanel,BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton experienceButton = new JButton("Experience");
            JButton filterButton = new JButton("Filter");
            JButton exportButton = new JButton("Export");

            buttonPanel.add(experienceButton);
            experienceButton.addActionListener(new openExperienceUI());
            buttonPanel.add(filterButton);
            filterButton.addActionListener(new filterGUIButtonListener());
            buttonPanel.add(exportButton);

            add(buttonPanel, BorderLayout.SOUTH);
            setVisible(true);

            revalidate();
            repaint();

        });
    }

    public void updateStatsGUI(){
        infoPanel.removeAll();
        for(int i = 0; i < statLabels.length; i++){
            statLabels[i] = new JLabel();
        }
        statLabels[0] = new JLabel("Average Sleep: " + averageSleep);
        statLabels[1] = new JLabel("Average Workout: " + averageWorkoutLength);
        statLabels[2] = new JLabel("Average Calories Burned: " + averageCaloriesBurned);

        statLabels[3] = new JLabel("Total Sleep: " + totalSleep);
        statLabels[4] = new JLabel("Total Workout: " + totalWorkoutLength);
        statLabels[5] = new JLabel("Total Calories Burned: " + totalCaloriesBurned);

        for(JLabel statLabel : statLabels) {
            statLabel.setFont(App.boldLabelFontLarge);
            statLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        for(int i = 0; i < statLabels.length; i++){
            if(selectedStat[i]) {
                infoPanel.add(Box.createVerticalStrut(25));
                infoPanel.add(statLabels[i]);
            }
        }
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 45, 90, 45),  // Outer empty border for spacing
                BorderFactory.createLineBorder(Color.BLACK, 5)    // Inner line border
        ));
        infoPanel.repaint();
        revalidate();
        repaint();
    }

    public void createFilterGUI(){

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel[] filterLabels = new JLabel[6];
        JCheckBox[] filterCheckBoxes = new JCheckBox[6];

        filterGUI.setTitle("Filter Stats");
        filterGUI.setSize(200,400);
        filterGUI.setResizable(false);

        JLabel titleLabel = new JLabel("Options");
        titleLabel.setFont(App.titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        for(int i = 0; i < filterLabels.length; i++){
            filterLabels[i] = new JLabel();
            filterCheckBoxes[i] = new JCheckBox();
            filterCheckBoxes[i].setName(i+"");
            filterCheckBoxes[i].addActionListener(new filterSelectStat(filterCheckBoxes[i]));
            if(selectedStat[i]){
                filterCheckBoxes[i].setSelected(true);
            }
        }

        filterLabels[0].setText("Average Sleep");
        filterLabels[1].setText("Average Workout");
        filterLabels[2].setText("Average Calories");
        filterLabels[3].setText("Total Sleep");
        filterLabels[4].setText("Total Workout");
        filterLabels[5].setText("Total Calories");

        JPanel filterBoxesPanel = new JPanel();

        for(int i = 0; i < filterLabels.length; i++) {
            JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tempPanel.add(filterLabels[i]);
            tempPanel.add(filterCheckBoxes[i]);
            filterBoxesPanel.add(tempPanel);
        }
        filterBoxesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),  // Outer empty border for spacing
                BorderFactory.createLineBorder(Color.BLACK, 2)    // Inner line border
        ));
        mainPanel.add(filterBoxesPanel);

        JPanel buttonsPanel = new JPanel();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new filterBack(filterGUI));
        buttonsPanel.add(backButton);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new filterApply(filterGUI));
        buttonsPanel.add(applyButton);
        buttonsPanel.setMaximumSize(buttonsPanel.getPreferredSize());

        mainPanel.add(buttonsPanel);


        filterGUI.add(mainPanel);
        filterGUI.setLocationRelativeTo(this);

    }
    public void showFilterGUI(){
        filterGUI.setVisible(true);
    }

    class filterGUIButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showFilterGUI();
        }
    }
    static class filterSelectStat implements ActionListener {

        JCheckBox filterbox;

        public filterSelectStat(JCheckBox filterbox){
            this.filterbox = filterbox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < selectedStat.length; i++){
                if(i == Integer.parseInt(filterbox.getName())){
                    selectedStat[i] = filterbox.isSelected();
                    System.out.println(i + ": " + selectedStat[i]);
                }
            }
        }
    }

    class filterApply implements ActionListener {

        JFrame filterGUI;

        public filterApply(JFrame filterGUI){
            this.filterGUI = filterGUI;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateStatsGUI();
            filterGUI.setVisible(false);
        }
    }
    static class filterBack implements ActionListener {

        JFrame filterGUI;

        public filterBack(JFrame filterGUI){
            this.filterGUI = filterGUI;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            filterGUI.dispose();
        }
    }

    static class openExperienceUI implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExperienceTracker xpUI = new ExperienceTracker();

        }
    }
}
