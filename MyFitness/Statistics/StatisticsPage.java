// Author: Larry O'Connor
package MyFitness.Statistics;

import MyFitness.App;
import MyFitness.ExerciseSession.Workout.CardioWorkout;
import MyFitness.ExerciseSession.Workout.LiftWorkout;
import MyFitness.ExerciseSession.Workout.Workout;
import MyFitness.ExperienceTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import MyFitness.Database;

public class StatisticsPage extends JPanel {

    static final String[][] StatLabels = {
            {"Sleep & Calories", "Workout", "Classes & Goals"},
            {"Average Sleep","Total Sleep","Total Calories Consumed", "Average Calories Consumed"},
            {"Total Hours Ran", "Average Hours Ran", "Total Miles Ran","Average Miles Ran"},
            {"Classes Attended","Total Class Hours","Average Class Hours","Goals Started","Goals Completed"}
    };

    public static boolean[][] selectedStat;
    public static List<Statistic> allStats;

    private JList<String> statsList;
    private JScrollPane statsScrollPane;
    private JPanel infoPanel;
    private JPanel titlePanel;
    private JPanel buttonPanel;
    private final App frame;

    private final FilterPage filterPage;

    public StatisticsPage(App frame) {
        this.frame = frame;
        initializeStats();
        frame.setTitle("Statistics Page");
        setLayout(new BorderLayout(50, 50));
        createStatsGUI();
        filterPage = new FilterPage(frame, this);
    }

    private void initializeStats(){
        allStats = initializeAllStats();
        int categories = StatLabels.length;
        selectedStat = new boolean[categories][];
        for (int cat = 1; cat < categories; cat++) {
            int metrics = StatLabels[cat].length;
            selectedStat[cat] = new boolean[metrics];
            for (int i = 0; i < metrics; i++) {
                selectedStat[cat][i] = true;
            }
        }
    }

    private void createStatsGUI(){
        SwingUtilities.invokeLater(() -> {

            titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            JLabel titleLabel = new JLabel("Statistics", SwingConstants.CENTER);

            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(App.titleFont);

            titlePanel.add(Box.createVerticalStrut(80));
            titlePanel.add(titleLabel);

            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            statsList = new JList<>();
            statsList.setFont(App.statsFont);
            statsScrollPane = new JScrollPane(statsList);
            infoPanel.add(statsScrollPane,BorderLayout.CENTER);
            updateStatsPanel();
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(45, 45, 90, 45),
                    BorderFactory.createLineBorder(Color.BLACK, 5)
            ));

            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton experienceButton = new JButton("Experience");
            experienceButton.addActionListener(new openExperienceUI());
            JButton filterButton = new JButton("Filter");
            filterButton.addActionListener(new openFilterUI(this));

            buttonPanel.add(filterButton);
            buttonPanel.add(experienceButton);

            add(titlePanel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            revalidate();
            repaint();
        });
    }

    public void updateStatsGUI(){
        updateStatsPanel();
        infoPanel.repaint();
        revalidate();
        repaint();
    }

    private void updateStatsPanel() {

        List<String> metrics = new ArrayList<>();

        for (int cat = 1; cat < StatLabels.length; cat++) {
            for (int i = 0; i < StatLabels[cat].length; i++) {
                if(selectedStat[cat][i]) {
                    String metric = StatLabels[cat][i];
                    metric+= " " + calculateStat(StatLabels[cat][i]) + ":";
                    metric+= getStatTag(StatLabels[cat][i]);
                    metrics.add(metric);
                }
            }
        }
        statsList.setListData(metrics.toArray(new String[0]));
    }

    class openFilterUI implements ActionListener {
        private final StatisticsPage statsPage;
        public openFilterUI(StatisticsPage statsPage) { this.statsPage = statsPage; }
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().remove(statsPage);
            frame.getContentPane().add(filterPage);
            frame.revalidate();
            frame.repaint();
        }
    }

    static class openExperienceUI implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           ExperienceTracker xpUI = new ExperienceTracker();
        }
    }

    public List<Statistic> initializeAllStats(){
        List<Statistic> stats = new ArrayList<>();
        Set<Workout> workouts = new HashSet<>();
        workouts = Database.getInstance().getAllWorkouts(frame.getUser());
        for(Workout workout : workouts){
            if(workout instanceof CardioWorkout){

                //Create Running Hour stat
                double cardioSec = ((CardioWorkout) workout).getSeconds()/360.0;
                double cardioMin = ((CardioWorkout) workout).getMinutes()/60.0;
                double cardioHour = ((CardioWorkout) workout).getHours();
                double cardioTime = cardioSec+cardioHour+cardioMin;
                Statistic cardioHoursRan = new Statistic("Cardio Hours",cardioTime);
                System.out.println(cardioHour);
                stats.add(cardioHoursRan);

                //Create Running Distance stat
                double cardioMiles = ((CardioWorkout) workout).getDistance();
                Statistic cardioMilesRan = new Statistic("Cardio Miles",cardioMiles);
                stats.add(cardioMilesRan);

            }
        }
        if (workouts.isEmpty()) {
            System.out.println("No workouts");
        }
        return stats;
    }

    public String getStatTag(String statName){
        if(statName.contains("Hours")){
            return " Hours";
        }
        return "";
    }
    public double calculateStat(String statName){
        double statValue = 0.0;
        int total = 0;
        for(Statistic stat : allStats){
            if(statName.contains("Hours Ran")){
                if(stat.getStatName().equals("Cardio Hours")){
                    statValue+=stat.getStatValue();
                    total++;
                }
            }
            if(statName.contains("Miles Ran")){
                if(stat.getStatName().equals("Cardio Miles")){
                    statValue+=stat.getStatValue();
                    total++;
                }
            }
        }
        if(statName.contains("Average") && total!=0){
            statValue/=total;
        }
        return statValue;
    }
}

