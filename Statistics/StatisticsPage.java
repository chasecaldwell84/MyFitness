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
import MyFitness.RyanStuff.SleepReport;

public class StatisticsPage extends JPanel {

    static final String[][] StatLabels = {
            {"Sleep", "Cardio", "Weight Lifting"},
            {"Average Sleep Per Night","Total Sleep"},
            {"Total Hours Ran", "Average Hours Ran", "Total Miles Ran","Average Miles Ran"},
            {"Workouts Best Set"}
    };

    public static boolean[][] selectedStat;
    public static List<Statistic> allStats;
    public static List<String> liftingNames = new ArrayList<>();

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
            statsList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setText((String) value); // HTML will be parsed and rendered
                    return label;
                }
            });

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

    private void updateStatsPanel(){

        List<String> metrics = new ArrayList<>();

        for (int cat = 1; cat < StatLabels.length; cat++) {
            for (int i = 0; i < StatLabels[cat].length; i++) {
                if(selectedStat[cat][i]) {
                    if(!StatLabels[cat][i].contains("Workout")) {
                        String metric = "<html><pre><b>"+ StatLabels[cat][i];
                        for(int j = metric.length(); j < 40; j++){
                            metric+=" ";
                        }
                        metric += ": </b>" + calculateStat(StatLabels[cat][i]);
                        metric += getStatTag(StatLabels[cat][i]) + "</pre></html>";
                        metrics.add(metric);
                    }
                    else{
                        for(String liftingName : liftingNames){
                            String metric = "<html><pre><b>" + liftingName + " " + getStatTag(StatLabels[cat][i]);
                            for(int j = metric.length(); j < 40; j++){
                                metric+=" ";
                            }
                            metric += ":</b> Weight (" + calculateStat(liftingName + " Best Weight") + ")" + " Reps (" + calculateStat(liftingName+ " Best Rep") + ")</pre></html>";
                            metrics.add(metric);
                        }
                    }
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
        List<SleepReport> sleepReports = new ArrayList<>();

        workouts = Database.getInstance().getAllWorkouts(frame.getUser());
        sleepReports = Database.getInstance().getAllSleepReports(frame.getUser());

        for(Workout workout : workouts){
            if(workout instanceof CardioWorkout){

                //Create Running Hour stat
                double cardioSec = ((CardioWorkout) workout).getSeconds()/3600.0;
                double cardioMin = ((CardioWorkout) workout).getMinutes()/60.0;
                double cardioHour = ((CardioWorkout) workout).getHours();
                double cardioTime = cardioSec+cardioHour+cardioMin;
                Statistic cardioHoursRan = new Statistic("Hours Ran",cardioTime);
                System.out.println(cardioHour);
                stats.add(cardioHoursRan);

                //Create Running Distance stat
                double cardioMiles = ((CardioWorkout) workout).getDistance();
                Statistic cardioMilesRan = new Statistic("Miles Ran",cardioMiles);
                stats.add(cardioMilesRan);

            }

            else if(workout instanceof LiftWorkout){

                //Create Best Rep and Best Weight stat
                String liftingName = workout.getWorkoutName();
                if(liftingName.isBlank()){
                    continue;
                }
                int bestRep = 0;
                double bestWeight = 0;
                Set<LiftWorkout.LiftSet> sets = new HashSet<>(((LiftWorkout) workout).getSets());
                for(LiftWorkout.LiftSet lift : sets){
                    System.out.println("Weightliftng: " + liftingName);
                    System.out.println("---- Reps: " + lift.getReps());
                    System.out.println("---- Weight: " + lift.getWeight());
                    if(bestWeight < lift.getWeight()){
                        bestWeight = lift.getWeight();
                    }
                    if(bestRep < lift.getReps()){
                        bestRep = lift.getReps();
                        System.out.println("Best Rep: " + bestRep);
                    }
                }

                Statistic liftBestRepStat = new Statistic(liftingName+" Best Rep",bestRep);
                Statistic liftBestWeight = new Statistic(liftingName+" Best Weight",bestWeight);
                stats.add(liftBestRepStat);
                stats.add(liftBestWeight);
                if(!liftingNames.contains(liftingName)) {
                    liftingNames.add(liftingName);
                }
            }

        }

        for(SleepReport sleepReport : sleepReports){
            double totalSleep = 0;
            totalSleep+= sleepReport.getHours();
            totalSleep+= sleepReport.getMinutes()/60.0;

            Statistic sleepStat = new Statistic("Sleep", totalSleep);
            System.out.println("Sleep: " + totalSleep);
            stats.add(sleepStat);
        }



        return stats;
    }

    public String getStatTag(String statName){
        if(statName.contains("Hours") || statName.contains("Sleep")){
            return " Hours";
        }
        else if(statName.contains("Set")) {
            return "Best Set";
        }
        return "";
    }
    public double calculateStat(String statName){
        double statValue = 0.0;
        int total = 0;
        for(Statistic stat : allStats){
            if(statName.contains(stat.getStatName())){
                statValue+=stat.getStatValue();
                total++;
            }
        }
        if(statName.contains("Average") && total!=0){
            statValue/=total;
        }
        statValue = Math.round(statValue*100.0) / 100.0;
        return statValue;
    }
}

